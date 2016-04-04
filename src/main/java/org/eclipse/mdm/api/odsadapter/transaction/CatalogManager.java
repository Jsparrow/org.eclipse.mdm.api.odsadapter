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
import java.util.stream.Collectors;

import org.asam.ods.AoException;
import org.asam.ods.ApplicationAttribute;
import org.asam.ods.ApplicationElement;
import org.asam.ods.ApplicationRelation;
import org.asam.ods.BaseAttribute;
import org.asam.ods.BaseElement;
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

	/*
	 * TODO: collect all accessed CORBA references and release them as soon as this instance is cleared!
	 */

	private final ODSTransaction transaction;

	public CatalogManager(ODSTransaction transaction) {
		this.transaction = transaction;
	}

	public void createCatalogComponents(Collection<CatalogComponent> catalogComponents) throws AoException {
		Map<ContextType, List<CatalogComponent>> catalogComponentsByContextType = catalogComponents.stream()
				.collect(Collectors.groupingBy(CatalogComponent::getContextType));

		for(Entry<ContextType, List<CatalogComponent>> entry : catalogComponentsByContextType.entrySet()) {
			String odsContextTypeName = ODSUtils.CONTEXTTYPES.convert(entry.getKey());
			ApplicationElement contextRootApplicationElement = transaction.getApplicationStructure().getElementByName(odsContextTypeName);
			BaseElement contextRootBaseElement = contextRootApplicationElement.getBaseElement();
			ApplicationElement contextTemplateComponentApplicationElement = transaction.getApplicationStructure().getElementByName("Tpl" + odsContextTypeName + "Comp");
			BaseElement baseElement = transaction.getBaseStructure().getElementByType("Ao" + odsContextTypeName + "Part");

			for(CatalogComponent catalogComponent : entry.getValue()) {
				ApplicationElement applicationElement = createApplicationElement(baseElement, catalogComponent);

				// relation context root to context component
				ApplicationRelation applicationRelation = transaction.getApplicationStructure().createRelation();
				applicationRelation.setElem1(contextRootApplicationElement);
				applicationRelation.setElem2(applicationElement);
				applicationRelation.setRelationName(catalogComponent.getName());
				applicationRelation.setInverseRelationName(odsContextTypeName);
				applicationRelation.setBaseRelation(transaction.getBaseStructure().getRelation(contextRootBaseElement, baseElement));

				// relation template component to context component
				applicationRelation = transaction.getApplicationStructure().createRelation();
				applicationRelation.setElem1(contextTemplateComponentApplicationElement);
				applicationRelation.setElem2(applicationElement);
				applicationRelation.setRelationName(catalogComponent.getName());
				applicationRelation.setInverseRelationName("Tpl" + odsContextTypeName + "Comp");
				applicationRelation.setRelationRange(new RelationRange((short)0, (short) -1));
				applicationRelation.setInverseRelationRange(new RelationRange((short) 1, (short) 1));
			}
		}
	}

	public void createCatalogAttributes(Collection<CatalogAttribute> catalogAttributes) throws AoException {
		for(CatalogAttribute catalogAttribute : catalogAttributes) {
			// TODO implement a cache...
			ApplicationElement applicationElement = transaction.getApplicationStructure().getElementByName(catalogAttribute.getParent().getName());

			ApplicationAttribute applicationAttribute = applicationElement.createAttribute();
			ValueType valueType = catalogAttribute.getScalarType().toValueType();
			DataType dataType = ODSUtils.VALUETYPES.convert(catalogAttribute.isSequence() ? valueType : valueType.toSingleType());
			applicationAttribute.setDataType(dataType);
			applicationAttribute.setName(catalogAttribute.getName());
			if (dataType == DataType.DT_ENUM) {
				applicationAttribute.setEnumerationDefinition(transaction.getApplicationStructure().getEnumerationDefinition(ODSEnumerations.getEnumName(catalogAttribute.getEnumerationClass())));
			}
		}
	}

	public void deleteCatalogComponents(Collection<CatalogComponent> catalogComponents) throws AoException, DataAccessException {
		List<CatalogAttribute> attributes = new ArrayList<>();
		List<CatalogSensor> sensors = new ArrayList<>();
		for (CatalogComponent catalogComponent : catalogComponents) {
			// TODO VERY IMPORTANT! If not fixed this might break the
			//      application model in rare cases!
			// make sure it is safe to delete this catalog component in
			// compliance with the application model's meta data
			//
			// TODO for now we assume given catalog components are unchanged!

			attributes.addAll(catalogComponent.getCatalogAttributes());
			sensors.addAll(catalogComponent.getCatalogSensors());
		}
		transaction.delete(attributes);
		transaction.delete(sensors);

		if(areReferencedInTemplates(catalogComponents)) {
			throw new DataAccessException("Unable to delete given catalog components since at least one is used in templates.");
		}

		Map<ContextType, List<CatalogComponent>> catalogComponentsByContextType = catalogComponents.stream()
				.collect(Collectors.groupingBy(CatalogComponent::getContextType));

		for(Entry<ContextType, List<CatalogComponent>> entry : catalogComponentsByContextType.entrySet()) {
			for(CatalogComponent catalogComponent : entry.getValue()) {
				ApplicationElement applicationElement = transaction.getApplicationStructure().getElementByName(catalogComponent.getName());
				for(ApplicationRelation applicationRelation : applicationElement.getAllRelations()) {
					transaction.getApplicationStructure().removeRelation(applicationRelation);
				}
				transaction.getApplicationStructure().removeElement(applicationElement);
			}
		}
	}

	public void deleteCatalogAttributes(Collection<CatalogAttribute> catalogAttributes) throws AoException, DataAccessException {
		if(areReferencedInTemplates(catalogAttributes)) {
			throw new DataAccessException("Unable to delete given catalog attributes since at least one is used in templates.");
		}

		Map<String, List<CatalogAttribute>> catalogAttributesByParent = catalogAttributes.stream()
				.collect(Collectors.groupingBy(c -> c.getParent().getName()));

		for(Entry<String, List<CatalogAttribute>> entry : catalogAttributesByParent.entrySet()) {
			ApplicationElement applicationElement = transaction.getApplicationStructure().getElementByName(entry.getKey());

			for(CatalogAttribute catalogAttribute : entry.getValue()) {
				ApplicationAttribute applicationAttribute = applicationElement.getAttributeByName(catalogAttribute.getName());
				applicationElement.removeAttribute(applicationAttribute);
			}
		}
	}

	private ApplicationElement createApplicationElement(BaseElement baseElement,
			/* TODO this may be used for CatalogSensors as well! */CatalogComponent catalogComponent) throws AoException {
		ApplicationElement applicationElement = transaction.getApplicationStructure().createElement(baseElement);
		applicationElement.setName(catalogComponent.getName());

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

		return applicationElement;
	}

	private boolean areReferencedInTemplates(Collection<? extends Entity> entities) throws DataAccessException {
		Map<EntityType, List<Entity>> entitiesByEntityType = entities.stream()
				.collect(Collectors.groupingBy(transaction.getModelManager()::getEntityType));

		for(Entry<EntityType, List<Entity>> entry : entitiesByEntityType.entrySet()) {
			EntityType source = entry.getKey();
			EntityType target = transaction.getModelManager().getEntityType(source.getName().replace("Cat", "Tpl"));

			Query query = transaction.getModelManager().createQuery().selectID(target).join(source.getRelation(target));

			long[] instanceIDs = collectInstanceIDs(entry.getValue());
			List<Result> results = query.fetch(Filter.and().add(Operation.IN_SET.create(source.getIDAttribute(), instanceIDs)));
			if(results.size() > 0) {
				// TODO add debug logging with detailed informations about conflicts that were found
				return true;
			}
		}

		return false;
	}

	private long[] collectInstanceIDs(List<Entity> entities) {
		long[] ids = new long[entities.size()];

		for(int i = 0; i < ids.length; i++) {
			ids[i] = entities.get(i).getURI().getID();
		}

		return ids;
	}

}
