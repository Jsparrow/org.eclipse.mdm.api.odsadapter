/*
 * Copyright (c) 2016 Gigatronik Ingolstadt GmbH
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.mdm.api.odsadapter.transaction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import org.asam.ods.AoException;
import org.asam.ods.ApplicationAttribute;
import org.asam.ods.ApplicationElement;
import org.asam.ods.ApplicationRelation;
import org.asam.ods.ApplicationStructure;
import org.asam.ods.BaseAttribute;
import org.asam.ods.BaseElement;
import org.asam.ods.BaseStructure;
import org.asam.ods.DataType;
import org.asam.ods.RelationRange;
import org.eclipse.mdm.api.base.model.ContextType;
import org.eclipse.mdm.api.base.model.Entity;
import org.eclipse.mdm.api.base.model.ValueType;
import org.eclipse.mdm.api.base.query.DataAccessException;
import org.eclipse.mdm.api.base.query.EntityType;
import org.eclipse.mdm.api.base.query.Filter;
import org.eclipse.mdm.api.base.query.Operation;
import org.eclipse.mdm.api.base.query.Query;
import org.eclipse.mdm.api.base.query.Result;
import org.eclipse.mdm.api.dflt.model.CatalogAttribute;
import org.eclipse.mdm.api.dflt.model.CatalogComponent;
import org.eclipse.mdm.api.dflt.model.CatalogSensor;
import org.eclipse.mdm.api.odsadapter.utils.ODSEnumerations;
import org.eclipse.mdm.api.odsadapter.utils.ODSUtils;

final class CatalogManager {

	private final ODSTransaction transaction;

	private ApplicationStructure applicationStructure;
	private BaseStructure baseStructure;

	CatalogManager(ODSTransaction transaction) {
		this.transaction = transaction;
	}

	public void createCatalogComponents(Collection<CatalogComponent> catalogComponents) throws AoException {
		Map<ContextType, List<CatalogComponent>> catalogComponentsByContextType = catalogComponents.stream()
				.collect(Collectors.groupingBy(CatalogComponent::getContextType));

		for(Entry<ContextType, List<CatalogComponent>> entry : catalogComponentsByContextType.entrySet()) {
			String odsContextTypeName = ODSUtils.CONTEXTTYPES.convert(entry.getKey());
			ApplicationElement contextRootApplicationElement = getApplicationStructure().getElementByName(odsContextTypeName);
			BaseElement contextRootBaseElement = contextRootApplicationElement.getBaseElement();
			ApplicationElement contextTemplateComponentApplicationElement = getApplicationStructure().getElementByName("Tpl" + odsContextTypeName + "Comp");
			BaseElement baseElement = getBaseStructure().getElementByType("Ao" + odsContextTypeName + "Part");

			for(CatalogComponent catalogComponent : entry.getValue()) {
				ApplicationElement applicationElement = createApplicationElement(catalogComponent.getName(), baseElement);

				// relation context root to context component
				ApplicationRelation applicationRelation = getApplicationStructure().createRelation();
				applicationRelation.setElem1(contextRootApplicationElement);
				applicationRelation.setElem2(applicationElement);
				applicationRelation.setRelationName(catalogComponent.getName());
				applicationRelation.setInverseRelationName(odsContextTypeName);
				applicationRelation.setBaseRelation(getBaseStructure().getRelation(contextRootBaseElement, baseElement));
				applicationRelation._release();

				// relation template component to context component
				applicationRelation = getApplicationStructure().createRelation();
				applicationRelation.setElem1(contextTemplateComponentApplicationElement);
				applicationRelation.setElem2(applicationElement);
				applicationRelation.setRelationName(catalogComponent.getName());
				applicationRelation.setInverseRelationName("Tpl" + odsContextTypeName + "Comp");
				applicationRelation.setRelationRange(new RelationRange((short)0, (short) -1));
				applicationRelation.setInverseRelationRange(new RelationRange((short) 1, (short) 1));

				// release resources
				applicationElement._release();
				applicationRelation._release();
			}

			// release resources
			contextTemplateComponentApplicationElement._release();
			contextRootApplicationElement._release();
			contextRootBaseElement._release();
			baseElement._release();
		}
	}

	public void createCatalogAttributes(Collection<CatalogAttribute> catalogAttributes) throws AoException {
		for(CatalogAttribute catalogAttribute : catalogAttributes) {
			ApplicationElement applicationElement = getApplicationStructure().getElementByName(getParentName(catalogAttribute));

			ApplicationAttribute applicationAttribute = applicationElement.createAttribute();
			ValueType valueType = catalogAttribute.getScalarType().toValueType();
			DataType dataType = ODSUtils.VALUETYPES.convert(catalogAttribute.isSequence() ? valueType : valueType.toSingleType());
			applicationAttribute.setDataType(dataType);
			applicationAttribute.setName(catalogAttribute.getName());
			if (dataType == DataType.DT_ENUM) {
				applicationAttribute.setEnumerationDefinition(getApplicationStructure().getEnumerationDefinition(ODSEnumerations.getEnumName(catalogAttribute.getEnumerationClass())));
			}

			// release resources
			applicationElement._release();
			applicationAttribute._release();
		}
	}

	public void updateCatalogAttributes(List<CatalogAttribute> catalogAttributes) throws DataAccessException {
		/*
		 * TODO: in case of CatalogAttribute we should update the Unit (changes the application model!)
		 */
	}

	public void deleteCatalogComponents(Collection<CatalogComponent> catalogComponents) throws AoException, DataAccessException {
		List<CatalogAttribute> attributes = new ArrayList<>();
		List<CatalogSensor> sensors = new ArrayList<>();
		for (CatalogComponent catalogComponent : catalogComponents) {
			attributes.addAll(catalogComponent.getCatalogAttributes());
			sensors.addAll(catalogComponent.getCatalogSensors());
		}
		transaction.delete(sensors);
		transaction.delete(attributes);

		if(areReferencedInTemplates(catalogComponents)) {
			throw new DataAccessException("Unable to delete given catalog components since at least one is used in templates.");
		}

		Map<ContextType, List<CatalogComponent>> catalogComponentsByContextType = catalogComponents.stream()
				.collect(Collectors.groupingBy(CatalogComponent::getContextType));

		for(Entry<ContextType, List<CatalogComponent>> entry : catalogComponentsByContextType.entrySet()) {
			for(CatalogComponent catalogComponent : entry.getValue()) {
				ApplicationElement applicationElement = getApplicationStructure().getElementByName(catalogComponent.getName());
				for(ApplicationRelation applicationRelation : applicationElement.getAllRelations()) {
					getApplicationStructure().removeRelation(applicationRelation);

					// release resources
					applicationRelation._release();
				}
				getApplicationStructure().removeElement(applicationElement);

				// release resources
				applicationElement._release();
			}
		}
	}

	public void deleteCatalogAttributes(Collection<CatalogAttribute> catalogAttributes) throws AoException, DataAccessException {
		if(areReferencedInTemplates(catalogAttributes)) {
			throw new DataAccessException("Unable to delete given catalog attributes since at least one is used in templates.");
		}

		Map<String, List<CatalogAttribute>> catalogAttributesByParent = catalogAttributes.stream()
				.collect(Collectors.groupingBy(CatalogManager::getParentName));

		for(Entry<String, List<CatalogAttribute>> entry : catalogAttributesByParent.entrySet()) {
			ApplicationElement applicationElement = getApplicationStructure().getElementByName(entry.getKey());

			for(CatalogAttribute catalogAttribute : entry.getValue()) {
				ApplicationAttribute applicationAttribute = applicationElement.getAttributeByName(catalogAttribute.getName());
				applicationElement.removeAttribute(applicationAttribute);

				// release resources
				applicationAttribute._release();
			}

			// release resources
			applicationElement._release();
		}
	}

	public void clear() {
		if(applicationStructure != null) {
			applicationStructure._release();
		}

		if(baseStructure != null) {
			baseStructure._release();
		}
	}

	private ApplicationElement createApplicationElement(String applicationElementName, BaseElement baseElement) throws AoException {
		ApplicationElement applicationElement = getApplicationStructure().createElement(baseElement);
		applicationElement.setName(applicationElementName);

		// Id
		ApplicationAttribute idApplicationAttribute = applicationElement.getAttributeByBaseName("id");
		idApplicationAttribute.setName("Id");
		idApplicationAttribute.setDataType(DataType.DT_LONGLONG);
		idApplicationAttribute.setIsAutogenerated(true);

		// Name
		ApplicationAttribute nameApplicationAttribute = applicationElement.getAttributeByBaseName("name");
		nameApplicationAttribute.setName("Name");
		nameApplicationAttribute.setLength(50);

		// MimeType
		BaseAttribute mimeTypeBaseAttribute = baseElement.getAttributes("mime_type")[0];
		ApplicationAttribute mimeTypeApplicationAttribute = applicationElement.createAttribute();
		mimeTypeApplicationAttribute.setBaseAttribute(mimeTypeBaseAttribute);
		mimeTypeApplicationAttribute.setName("MimeType");
		mimeTypeApplicationAttribute.setDataType(DataType.DT_STRING);
		mimeTypeApplicationAttribute.setLength(256);
		mimeTypeApplicationAttribute.setIsObligatory(true);

		// release resources
		idApplicationAttribute._release();
		nameApplicationAttribute._release();
		mimeTypeApplicationAttribute._release();
		mimeTypeBaseAttribute._release();

		return applicationElement;
	}

	ApplicationStructure getApplicationStructure() throws AoException {
		if(applicationStructure == null) {
			applicationStructure = transaction.getModelManager().getAoSession().getApplicationStructure();
		}

		return applicationStructure;
	}

	BaseStructure getBaseStructure() throws AoException {
		if(baseStructure == null) {
			baseStructure = transaction.getModelManager().getAoSession().getBaseStructure();
		}

		return baseStructure;
	}

	private boolean areReferencedInTemplates(Collection<? extends Entity> entities) throws AoException, DataAccessException {
		Map<EntityType, List<Entity>> entitiesByEntityType = entities.stream()
				.collect(Collectors.groupingBy(transaction.getModelManager()::getEntityType));

		for(Entry<EntityType, List<Entity>> entry : entitiesByEntityType.entrySet()) {
			EntityType source = entry.getKey();
			EntityType target = transaction.getModelManager().getEntityType(source.getName().replace("Cat", "Tpl"));

			Query query = transaction.getModelManager().createQuery().selectID(target).join(source, target);

			long[] instanceIDs = collectInstanceIDs(entry.getValue());
			List<Result> results = query.fetch(Filter.and().add(Operation.IN_SET.create(source.getIDAttribute(), instanceIDs)));
			if(results.size() > 0) {
				// TODO add debug logging with detailed informations about conflicts that were found
				return true;
			}
		}

		return false;
	}

	private static long[] collectInstanceIDs(List<Entity> entities) {
		long[] ids = new long[entities.size()];

		for(int i = 0; i < ids.length; i++) {
			ids[i] = entities.get(i).getID();
		}

		return ids;
	}

	private static String getParentName(CatalogAttribute catalogAttribute) {
		Optional<CatalogComponent> catalogComponent = catalogAttribute.getCatalogComponent();
		if(catalogComponent.isPresent()) {
			return catalogComponent.get().getName();
		}

		return catalogAttribute.getCatalogSensor().orElseThrow(() -> new IllegalStateException("Parent entity is unknown.")).getName();
	}

}
