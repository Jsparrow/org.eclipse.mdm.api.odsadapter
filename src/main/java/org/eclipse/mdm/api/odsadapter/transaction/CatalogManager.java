/*
 * Copyright (c) 2016 Gigatronik Ingolstadt GmbH and others
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
import org.eclipse.mdm.api.base.ServiceNotProvidedException;
import org.eclipse.mdm.api.base.adapter.EntityType;
import org.eclipse.mdm.api.base.model.ContextType;
import org.eclipse.mdm.api.base.model.Entity;
import org.eclipse.mdm.api.base.model.Unit;
import org.eclipse.mdm.api.base.query.DataAccessException;
import org.eclipse.mdm.api.base.query.Filter;
import org.eclipse.mdm.api.base.query.ComparisonOperator;
import org.eclipse.mdm.api.base.query.Query;
import org.eclipse.mdm.api.base.query.QueryService;
import org.eclipse.mdm.api.base.query.Result;
import org.eclipse.mdm.api.dflt.model.CatalogAttribute;
import org.eclipse.mdm.api.dflt.model.CatalogComponent;
import org.eclipse.mdm.api.dflt.model.CatalogSensor;
import org.eclipse.mdm.api.odsadapter.utils.ODSConverter;
import org.eclipse.mdm.api.odsadapter.utils.ODSEnumerations;
import org.eclipse.mdm.api.odsadapter.utils.ODSUtils;

/**
 * Used to create, update or delete {@link CatalogComponent},
 * {@link CatalogSensor} and {@link CatalogAttribute} entities. Modifications of
 * the listed types results in modifications of the application model.
 *
 * @since 1.0.0
 * @author Viktor Stoehr, Gigatronik Ingolstadt GmbH
 */
final class CatalogManager {

	// ======================================================================
	// Instance variables
	// ======================================================================

	private final ODSTransaction transaction;

	private ApplicationStructure applicationStructure;
	private BaseStructure baseStructure;

	// ======================================================================
	// Constructors
	// ======================================================================

	/**
	 * Constructor.
	 *
	 * @param transaction
	 *            The {@link ODSTransaction}.
	 */
	CatalogManager(ODSTransaction transaction) {
		this.transaction = transaction;
	}

	// ======================================================================
	// Public methods
	// ======================================================================

	/**
	 * Creates for each given {@link CatalogComponent} a corresponding
	 * application element including all required application relations.
	 *
	 * @param catalogComponents
	 *            The {@code CatalogComponent}s.
	 * @throws AoException
	 *             Thrown in case of errors.
	 */
	public void createCatalogComponents(Collection<CatalogComponent> catalogComponents) throws AoException {
		Map<ContextType, List<CatalogComponent>> catalogComponentsByContextType = catalogComponents.stream()
				.collect(Collectors.groupingBy(CatalogComponent::getContextType));

		for (Entry<ContextType, List<CatalogComponent>> entry : catalogComponentsByContextType.entrySet()) {
			String odsContextTypeName = ODSUtils.CONTEXTTYPES.get(entry.getKey());
			ApplicationElement contextRootApplicationElement = getApplicationStructure()
					.getElementByName(odsContextTypeName);
			BaseElement contextRootBaseElement = contextRootApplicationElement.getBaseElement();
			ApplicationElement contextTemplateComponentApplicationElement = getApplicationStructure()
					.getElementByName("Tpl" + odsContextTypeName + "Comp");
			BaseElement baseElement = getBaseStructure().getElementByType("Ao" + odsContextTypeName + "Part");

			for (CatalogComponent catalogComponent : entry.getValue()) {
				ApplicationElement applicationElement = createApplicationElement(catalogComponent.getName(),
						baseElement);

				// relation context root to context component
				ApplicationRelation applicationRelation = getApplicationStructure().createRelation();
				applicationRelation.setElem1(contextRootApplicationElement);
				applicationRelation.setElem2(applicationElement);
				applicationRelation.setRelationName(catalogComponent.getName());
				applicationRelation.setInverseRelationName(odsContextTypeName);
				applicationRelation
						.setBaseRelation(getBaseStructure().getRelation(contextRootBaseElement, baseElement));
				applicationRelation._release();

				// relation template component to context component
				applicationRelation = getApplicationStructure().createRelation();
				applicationRelation.setElem1(contextTemplateComponentApplicationElement);
				applicationRelation.setElem2(applicationElement);
				applicationRelation.setRelationName(catalogComponent.getName());
				applicationRelation.setInverseRelationName("Tpl" + odsContextTypeName + "Comp");
				applicationRelation.setRelationRange(new RelationRange((short) 0, (short) -1));
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

	/**
	 * Creates for each given {@link CatalogSensor} a corresponding application
	 * element including all required application relations.
	 *
	 * @param catalogSensors
	 *            The {@code CatalogSensor}s.
	 * @throws AoException
	 *             Thrown in case of errors.
	 */
	public void createCatalogSensors(Collection<CatalogSensor> catalogSensors) throws AoException {
		Map<String, List<CatalogSensor>> catalogSensorsByCatalogComponent = catalogSensors.stream()
				.collect(Collectors.groupingBy(cs -> cs.getCatalogComponent().getName()));

		ApplicationElement channelApplicationElement = getApplicationStructure().getElementByName("MeaQuantity");
		BaseElement channelBaseElement = channelApplicationElement.getBaseElement();

		for (Entry<String, List<CatalogSensor>> entry : catalogSensorsByCatalogComponent.entrySet()) {
			ApplicationElement contextComponentApplicationElement = getApplicationStructure()
					.getElementByName(entry.getKey());
			BaseElement contextComponentBaseElement = contextComponentApplicationElement.getBaseElement();
			ApplicationElement contextTemplateSensorApplicationElement = getApplicationStructure()
					.getElementByName("TplSensor");
			BaseElement baseElement = getBaseStructure().getElementByType("AoTestEquipmentPart");

			for (CatalogSensor catalogSensor : entry.getValue()) {
				ApplicationElement applicationElement = createApplicationElement(catalogSensor.getName(), baseElement);

				// relation context component to context sensor
				ApplicationRelation applicationRelation = getApplicationStructure().createRelation();
				applicationRelation.setElem1(contextComponentApplicationElement);
				applicationRelation.setElem2(applicationElement);
				applicationRelation.setRelationName(catalogSensor.getName());
				applicationRelation.setInverseRelationName(entry.getKey());
				applicationRelation
						.setBaseRelation(getBaseStructure().getRelation(contextComponentBaseElement, baseElement));
				applicationRelation._release();

				// relation template sensor to context sensor
				applicationRelation = getApplicationStructure().createRelation();
				applicationRelation.setElem1(contextTemplateSensorApplicationElement);
				applicationRelation.setElem2(applicationElement);
				applicationRelation.setRelationName(catalogSensor.getName());
				applicationRelation.setInverseRelationName("TplSensor");
				applicationRelation.setRelationRange(new RelationRange((short) 0, (short) -1));
				applicationRelation.setInverseRelationRange(new RelationRange((short) 1, (short) 1));
				applicationRelation._release();

				// relation channel to context sensor
				applicationRelation = getApplicationStructure().createRelation();
				applicationRelation.setElem1(channelApplicationElement);
				applicationRelation.setElem2(applicationElement);
				applicationRelation.setRelationName(catalogSensor.getName());
				applicationRelation.setInverseRelationName("MeaQuantity");
				applicationRelation.setBaseRelation(getBaseStructure().getRelation(channelBaseElement, baseElement));

				// release resources
				applicationElement._release();
				applicationRelation._release();
			}

			// release resources
			contextComponentApplicationElement._release();
			contextTemplateSensorApplicationElement._release();
			contextComponentBaseElement._release();
			baseElement._release();
		}

		// release resources
		channelApplicationElement._release();
		channelBaseElement._release();
	}

	/**
	 * Creates for each given {@link CatalogAttribute} a corresponding
	 * application attribute.
	 *
	 * @param catalogAttributes
	 *            The {@code CatalogAttribute}s.
	 * @throws AoException
	 *             Thrown in case of errors.
	 */
	public void createCatalogAttributes(Collection<CatalogAttribute> catalogAttributes) throws AoException {
		Map<String, List<CatalogAttribute>> catalogAttributesByCatalogComponent = catalogAttributes.stream()
				.collect(Collectors.groupingBy(CatalogManager::getParentName));

		for (Entry<String, List<CatalogAttribute>> entry : catalogAttributesByCatalogComponent.entrySet()) {
			ApplicationElement applicationElement = getApplicationStructure().getElementByName(entry.getKey());

			for (CatalogAttribute catalogAttribute : entry.getValue()) {

				ApplicationAttribute applicationAttribute = applicationElement.createAttribute();
				DataType dataType = ODSUtils.VALUETYPES.get(catalogAttribute.getValueType());
				applicationAttribute.setDataType(dataType);
				applicationAttribute.setName(catalogAttribute.getName());
				if (dataType == DataType.DT_ENUM) {
					applicationAttribute.setEnumerationDefinition(getApplicationStructure().getEnumerationDefinition(
							ODSEnumerations.getEnumName(catalogAttribute.getEnumerationObject())));
				}
				Optional<Unit> unit = catalogAttribute.getUnit();
				if (unit.isPresent()) {
					applicationAttribute.setUnit(ODSConverter.toODSID(unit.get().getID()));
				}

				// release resources
				applicationAttribute._release();
			}

			// release resources
			applicationElement._release();
		}
	}

	/**
	 * Updates the application attribute for each given
	 * {@link CatalogAttribute}.
	 *
	 * @param catalogAttributes
	 *            The {@code CatalogAttribute}s.
	 * @throws AoException
	 *             Thrown in case of errors.
	 */
	public void updateCatalogAttributes(List<CatalogAttribute> catalogAttributes) throws AoException {
		Map<String, List<CatalogAttribute>> catalogAttributesByCatalogComponent = catalogAttributes.stream()
				.collect(Collectors.groupingBy(CatalogManager::getParentName));

		for (Entry<String, List<CatalogAttribute>> entry : catalogAttributesByCatalogComponent.entrySet()) {
			ApplicationElement applicationElement = getApplicationStructure().getElementByName(entry.getKey());

			for (CatalogAttribute catalogAttribute : entry.getValue()) {

				ApplicationAttribute applicationAttribute = applicationElement
						.getAttributeByName(catalogAttribute.getName());

				Optional<Unit> unit = catalogAttribute.getUnit();
				if (unit.isPresent()) {
					applicationAttribute.setUnit(ODSConverter.toODSID(unit.get().getID()));
				}

				// release resources
				applicationAttribute._release();
			}

			// release resources
			applicationElement._release();
		}
	}

	/**
	 * Deletes the corresponding application element for each given
	 * {@link CatalogComponent}. Deleting a {@code CatalogComponent} is only
	 * allowed if it is not used in templates and all of its children could be
	 * deleted. So at first it is tried to delete its {@link CatalogAttribute}s
	 * and {@link CatalogSensor}s. On success it is ensured none of the given
	 * {@code
	 * CatalogComponent}s is used in templates. Finally the corresponding
	 * application elements are deleted.
	 *
	 * @param catalogComponents
	 *            The {@code CatalogComponent}s.
	 * @throws AoException
	 *             Thrown in case of errors.
	 * @throws DataAccessException
	 *             Thrown in case of errors.
	 */
	public void deleteCatalogComponents(Collection<CatalogComponent> catalogComponents)
			throws AoException, DataAccessException {
		List<CatalogAttribute> attributes = new ArrayList<>();
		List<CatalogSensor> sensors = new ArrayList<>();
		for (CatalogComponent catalogComponent : catalogComponents) {
			attributes.addAll(catalogComponent.getCatalogAttributes());
			sensors.addAll(catalogComponent.getCatalogSensors());
		}
		transaction.delete(sensors);
		transaction.delete(attributes);

		if (areReferencedInTemplates(catalogComponents)) {
			throw new DataAccessException(
					"Unable to delete given catalog components since at least " + "one is used in templates.");
		}

		for (CatalogComponent catalogComponent : catalogComponents) {
			ApplicationElement applicationElement = getApplicationStructure()
					.getElementByName(catalogComponent.getName());
			for (ApplicationRelation applicationRelation : applicationElement.getAllRelations()) {
				getApplicationStructure().removeRelation(applicationRelation);

				// release resources
				applicationRelation._release();
			}
			getApplicationStructure().removeElement(applicationElement);

			// release resources
			applicationElement._release();
		}
	}

	/**
	 * Deletes the corresponding application element for each given
	 * {@link CatalogSensor}. Deleting a {@code CatalogSensor} is only allowed
	 * if it is not used in templates and all of its children could be deleted.
	 * So at first it is tried to delete its {@link CatalogAttribute}s. On
	 * success it is ensured none of the given {@code CatalogSensor}s is used in
	 * templates. Finally the corresponding application elements are deleted.
	 *
	 * @param catalogSensors
	 *            The {@code CatalogSensor}s.
	 * @throws AoException
	 *             Thrown in case of errors.
	 * @throws DataAccessException
	 *             Thrown in case of errors.
	 */
	public void deleteCatalogSensors(Collection<CatalogSensor> catalogSensors) throws AoException, DataAccessException {
		List<CatalogAttribute> attributes = new ArrayList<>();
		for (CatalogSensor catalogSensor : catalogSensors) {
			attributes.addAll(catalogSensor.getCatalogAttributes());
		}
		transaction.delete(attributes);

		if (areReferencedInTemplates(catalogSensors)) {
			throw new DataAccessException(
					"Unable to delete given catalog sensors since at " + "least one is used in templates.");
		}

		for (CatalogSensor catalogSensor : catalogSensors) {
			ApplicationElement applicationElement = getApplicationStructure().getElementByName(catalogSensor.getName());
			for (ApplicationRelation applicationRelation : applicationElement.getAllRelations()) {
				getApplicationStructure().removeRelation(applicationRelation);

				// release resources
				applicationRelation._release();
			}
			getApplicationStructure().removeElement(applicationElement);

			// release resources
			applicationElement._release();
		}

	}

	/**
	 * Deletes the corresponding application attributes for each given
	 * {@link CatalogAttribute}. Deleting a {@code CatalogAttribute} is only
	 * allowed if it is not used in templates. So at first it is ensured none of
	 * the given {@code CatalogAttribute}s is used in templates and finally the
	 * corresponding application attributes are deleted.
	 *
	 * @param catalogAttributes
	 *            The {@code CatalogAttribute}s.
	 * @throws AoException
	 *             Thrown in case of errors.
	 * @throws DataAccessException
	 *             Thrown in case of errors.
	 */
	public void deleteCatalogAttributes(Collection<CatalogAttribute> catalogAttributes)
			throws AoException, DataAccessException {
		if (areReferencedInTemplates(catalogAttributes)) {
			throw new DataAccessException(
					"Unable to delete given catalog attributes since at least " + "one is used in templates.");
		}

		Map<String, List<CatalogAttribute>> catalogAttributesByParent = catalogAttributes.stream()
				.collect(Collectors.groupingBy(CatalogManager::getParentName));

		for (Entry<String, List<CatalogAttribute>> entry : catalogAttributesByParent.entrySet()) {
			ApplicationElement applicationElement = getApplicationStructure().getElementByName(entry.getKey());

			for (CatalogAttribute catalogAttribute : entry.getValue()) {
				ApplicationAttribute applicationAttribute = applicationElement
						.getAttributeByName(catalogAttribute.getName());
				applicationElement.removeAttribute(applicationAttribute);

				// release resources
				applicationAttribute._release();
			}

			// release resources
			applicationElement._release();
		}
	}

	/**
	 * Releases cached resources.
	 */
	public void clear() {
		if (applicationStructure != null) {
			applicationStructure._release();
		}

		if (baseStructure != null) {
			baseStructure._release();
		}
	}

	// ======================================================================
	// Private methods
	// ======================================================================

	/**
	 * Creates a new {@link ApplicationElement} with given name and
	 * {@link BaseElement}. The returned {@code ApplicationElement} will be
	 * created with the three mandatory {@link ApplicationAttribute}s for 'Id',
	 * 'Name' and 'MimeType'.
	 *
	 * @param name
	 *            The name of the application element.
	 * @param baseElement
	 *            The {@code BaseElement} the created {@code
	 * 		ApplicationElement} will be derived from.
	 * @return The created {@code ApplicationElement} is returned.
	 * @throws AoException
	 *             Thrown in case of errors.
	 */
	private ApplicationElement createApplicationElement(String name, BaseElement baseElement) throws AoException {
		ApplicationElement applicationElement = getApplicationStructure().createElement(baseElement);
		applicationElement.setName(name);

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

	/**
	 * Returns the cached {@link ApplicationStructure}.
	 *
	 * @return The {@code ApplicationStructure} is returned.
	 * @throws AoException
	 *             Thrown if unable to access the {@code
	 * 		ApplicationStructure}.
	 */
	private ApplicationStructure getApplicationStructure() throws AoException {
		if (applicationStructure == null) {
			applicationStructure = transaction.getModelManager().getAoSession().getApplicationStructure();
		}

		return applicationStructure;
	}

	/**
	 * Returns the cached {@link BaseStructure}.
	 *
	 * @return The {@code BaseStructure} is returned.
	 * @throws AoException
	 *             Thrown if unable to access the {@code
	 * 		BaseStructure}.
	 */
	private BaseStructure getBaseStructure() throws AoException {
		if (baseStructure == null) {
			baseStructure = transaction.getModelManager().getAoSession().getBaseStructure();
		}

		return baseStructure;
	}

	/**
	 * Checks whether given {@link Entity}s are referenced in templates.
	 *
	 * @param entities
	 *            The checked entities ({@link CatalogComponent},
	 *            {@link CatalogSensor} or {@link CatalogAttribute}).
	 * @return Returns {@code true} if at least one entity is referenced in a
	 *         template.
	 * @throws AoException
	 *             Thrown on errors.
	 * @throws DataAccessException
	 *             Thrown on errors.
	 */
	private boolean areReferencedInTemplates(Collection<? extends Entity> entities)
			throws AoException, DataAccessException {
		Map<EntityType, List<Entity>> entitiesByEntityType = entities.stream()
				.collect(Collectors.groupingBy(transaction.getModelManager()::getEntityType));

		for (Entry<EntityType, List<Entity>> entry : entitiesByEntityType.entrySet()) {
			EntityType source = entry.getKey();
			EntityType target = transaction.getModelManager().getEntityType(source.getName().replace("Cat", "Tpl"));

			Query query = transaction.getContext().getQueryService()
					.orElseThrow(() -> new ServiceNotProvidedException(QueryService.class))
					.createQuery().selectID(target).join(source, target);

			List<Result> results = query.fetch(Filter.and()
					.add(ComparisonOperator.IN_SET.create(source.getIDAttribute(), collectInstanceIDs(entry.getValue()))));
			if (results.size() > 0) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Collect the instance IDs of all given {@link Entity}s.
	 *
	 * @param entities
	 *            The {@link Entity}s.
	 * @return The instance IDs a {@code String[]} are turned.
	 */
	private static String[] collectInstanceIDs(List<Entity> entities) {
		String[] ids = new String[entities.size()];

		for (int i = 0; i < ids.length; i++) {
			ids[i] = entities.get(i).getID();
		}

		return ids;
	}

	/**
	 * Returns the parent name for given {@link CatalogAttribute}.
	 *
	 * @param catalogAttribute
	 *            The {@code CatalogAttribute}.
	 * @return The parent name is returned.
	 */
	private static String getParentName(CatalogAttribute catalogAttribute) {
		Optional<CatalogComponent> catalogComponent = catalogAttribute.getCatalogComponent();
		if (catalogComponent.isPresent()) {
			return catalogComponent.get().getName();
		}

		return catalogAttribute.getCatalogSensor()
				.orElseThrow(() -> new IllegalStateException("Parent entity is unknown.")).getName();
	}

}
