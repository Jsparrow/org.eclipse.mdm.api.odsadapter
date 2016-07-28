package org.eclipse.mdm.api.odsadapter.lookup;

import static org.eclipse.mdm.api.dflt.model.CatalogAttribute.VATTR_ENUMERATION_CLASS;
import static org.eclipse.mdm.api.dflt.model.CatalogAttribute.VATTR_SCALAR_TYPE;
import static org.eclipse.mdm.api.dflt.model.CatalogAttribute.VATTR_SEQUENCE;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.eclipse.mdm.api.base.model.Core;
import org.eclipse.mdm.api.base.model.Deletable;
import org.eclipse.mdm.api.base.model.Entity;
import org.eclipse.mdm.api.base.model.ScalarType;
import org.eclipse.mdm.api.base.model.Value;
import org.eclipse.mdm.api.base.model.ValueType;
import org.eclipse.mdm.api.base.query.Attribute;
import org.eclipse.mdm.api.base.query.DefaultCore;
import org.eclipse.mdm.api.base.query.EntityType;
import org.eclipse.mdm.api.base.query.Record;
import org.eclipse.mdm.api.dflt.model.CatalogAttribute;

final class EntityResult<T extends Entity> {

	/*
	 * TODO: getter & setter for fields
	 * TODO hide field!
	 */

	private final Map<Long, EntityRecord<T>> entityRecords = new HashMap<>();
	private final List<T> entities = new ArrayList<>();

	@Deprecated final EntityRequest<T> request;

	EntityResult(EntityRequest<T> request) {
		this.request = request;
	}

	public Optional<EntityRecord<T>> get(Long id) {
		return Optional.ofNullable(entityRecords.get(id));
	}

	public EntityRecord<T> add(Record record) {
		return create(new DefaultCore(record));
	}

	public EntityRecord<T> add(EntityRecord<?> parentRecord, Record record) {
		Core core = new DefaultCore(record);

		if(CatalogAttribute.class.equals(request.entityConfig.getEntityClass())) {
			// add read only properties from application model
			adjustCatalogAttributeCore(parentRecord.entity, core);
		}

		EntityRecord<T> childRecord = create(core);
		childRecord.core.getPermanentStore().set(parentRecord.entity);
		parentRecord.core.getChildrenStore().add((Deletable) childRecord.entity);
		return childRecord;
	}

	private void adjustCatalogAttributeCore(Entity catalogComponent, Core catalogAttributeCore) {
		EntityType entityType = request.modelManager.getEntityType(catalogComponent.getName());
		Attribute attribute = entityType.getAttribute(catalogAttributeCore.getValues().get(Entity.ATTR_NAME).extract());

		Map<String, Value> values = catalogAttributeCore.getValues();
		Value enumerationClass = ValueType.STRING.create(VATTR_ENUMERATION_CLASS);
		values.put(VATTR_ENUMERATION_CLASS, enumerationClass);
		if(attribute.getValueType().isEnumerationType()) {
			enumerationClass.set(attribute.getEnumClass().getName());
		}

		Value scalarType = ValueType.ENUMERATION.create(ScalarType.class, VATTR_SCALAR_TYPE);
		scalarType.set(ScalarType.valueOf(attribute.getValueType().toSingleType().name()));
		values.put(VATTR_SCALAR_TYPE, scalarType);

		values.put(VATTR_SEQUENCE, ValueType.BOOLEAN.create(VATTR_SEQUENCE, attribute.getValueType().isSequence()));

		// TODO relation to Unit
		//		Unit unit = null;
		//		if(unit != null) {
		//			catalogAttributeCore.setInfoRelation(unit);
		//		}
	}

	public List<T> getEntities() {
		return entities;
	}

	public List<T> getSortedEntities() {
		return entities.stream().sorted(request.entityConfig.getComparator()).collect(Collectors.toList());
	}

	public Collection<Long> getIDs() {
		return entityRecords.keySet();
	}

	public boolean isEmpty() {
		return entities.isEmpty();
	}

	private EntityRecord<T> create(Core core) {
		Constructor<T> constructor = null;
		boolean isAccessible = false;
		try {
			constructor = request.entityConfig.getEntityClass().getDeclaredConstructor(Core.class);
			isAccessible = constructor.isAccessible();
			constructor.setAccessible(true);
			EntityRecord<T> entityRecord = new EntityRecord<>(constructor.newInstance(core), core);
			entityRecords.put(core.getID(), entityRecord);
			entities.add(entityRecord.entity);
			return entityRecord;
		} catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
			throw new IllegalStateException(e.getMessage(), e);
		} finally {
			if(constructor != null) {
				constructor.setAccessible(isAccessible);
			}
		}
	}

}
