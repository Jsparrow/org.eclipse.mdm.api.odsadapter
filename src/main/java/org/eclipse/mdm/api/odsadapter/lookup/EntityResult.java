package org.eclipse.mdm.api.odsadapter.lookup;

import static org.eclipse.mdm.api.dflt.model.CatalogAttribute.VATTR_ENUMERATION_CLASS;
import static org.eclipse.mdm.api.dflt.model.CatalogAttribute.VATTR_SCALAR_TYPE;
import static org.eclipse.mdm.api.dflt.model.CatalogAttribute.VATTR_SEQUENCE;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
import org.eclipse.mdm.api.dflt.model.CatalogComponent;

/**
 * Container for entities by executing an {@link EntityRequest}.
 *
 * @param <T> The entity type.
 * @since 1.0.0
 * @author Viktor Stoehr, Gigatronik Ingolstadt GmbH
 */
final class EntityResult<T extends Entity> {

	// ======================================================================
	// Instance variables
	// ======================================================================

	private final Map<Long, EntityRecord<T>> entityRecords = new HashMap<>();
	private final List<T> entities = new ArrayList<>();

	final EntityRequest<T> request;

	// ======================================================================
	// Constructors
	// ======================================================================

	/**
	 * Constructor.
	 *
	 * @param request The associated {@link EntityRequest}.
	 */
	EntityResult(EntityRequest<T> request) {
		this.request = request;
	}

	// ======================================================================
	// Public methods
	// ======================================================================

	/**
	 * Returns the {@link EntityRecord} identified by given instance ID.
	 *
	 * @param id The instance ID.
	 * @return {@code Optional} is empty if {@code EntityRecord} not found.
	 */
	public Optional<EntityRecord<T>> get(Long id) {
		return Optional.ofNullable(entityRecords.get(id));
	}

	/**
	 * Creates an {@link EntityRecord} for given {@link Record} and mapps it
	 * internally by its instance ID.
	 *
	 * @param record The {@code Record}.
	 * @return The created {@code EntityRecord} is returned.
	 */
	public EntityRecord<T> add(Record record) {
		return create(new DefaultCore(record));
	}

	/**
	 * Creates an {@link EntityRecord} for given {@link Record} using given
	 * parent {@code EntityRecord} and mapps it internally by its instance ID.
	 *
	 * @param parentRecord The created {@code EntityRecord} will be related as
	 * 		a child with this one.
	 * @param record The {@code Record}.
	 * @return The created {@code EntityRecord} is returned.
	 */
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

	/**
	 * Returns the {@link Entity}s of this entity result.
	 *
	 * @return Returned {@code Collection} is unmodifiable.
	 */
	public List<T> getEntities() {
		return Collections.unmodifiableList(entities);
	}

	/**
	 * Returns the {@link Entity}s of this entity result sorted.
	 *
	 * @return Returned {@code Collection} is unmodifiable.
	 */
	public List<T> getSortedEntities() {
		return Collections.unmodifiableList(entities.stream()
				.sorted(request.entityConfig.getComparator()).collect(Collectors.toList()));
	}

	/**
	 * Returns the instance IDs of the entities held by this entity result.
	 *
	 * @return Returned {@code Collection} is unmodifiable.
	 */
	public Collection<Long> getIDs() {
		return Collections.unmodifiableCollection(entityRecords.keySet());
	}

	/**
	 * Checks whether this entity result holds entities or is empty.
	 *
	 * @return Returns {@code true} if this entity result has no entities.
	 */
	public boolean isEmpty() {
		return entities.isEmpty();
	}

	// ======================================================================
	// Private methods
	// ======================================================================

	/**
	 * Adds further meta data to the given {@link CatalogAttribute} {@link
	 * Core}.
	 *
	 * @param catalogComponent The parent {@link CatalogComponent}.
	 * @param catalogAttributeCore The {@code CatalogAttribute} {@code Core}.
	 */
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
	}

	/**
	 * Creates a new {@link EntityRecord} instance for given {@link Core}.
	 * The {@link EntityRecord} is internally mapped the its instance ID.
	 *
	 * @param core The {@code Core}.
	 * @return The created {@link EntityRecord} is returned.
	 */
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
		} catch (InstantiationException | IllegalAccessException |
				NoSuchMethodException | InvocationTargetException e) {
			throw new IllegalStateException(e.getMessage(), e);
		} finally {
			if(constructor != null) {
				constructor.setAccessible(isAccessible);
			}
		}
	}

}
