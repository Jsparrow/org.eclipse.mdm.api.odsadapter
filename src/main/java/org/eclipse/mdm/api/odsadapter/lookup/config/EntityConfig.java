package org.eclipse.mdm.api.odsadapter.lookup.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.eclipse.mdm.api.base.model.ContextType;
import org.eclipse.mdm.api.base.model.Deletable;
import org.eclipse.mdm.api.base.model.Entity;
import org.eclipse.mdm.api.base.model.StatusAttachable;
import org.eclipse.mdm.api.base.query.EntityType;

/**
 * Describes the composition of an {@link Entity} with its mandatory, optional
 * and child relations.
 *
 * @param <T> The entity type.
 * @since 1.0.0
 * @author Viktor Stoehr, Gigatronik Ingolstadt GmbH
 */
public class EntityConfig<T extends Entity> {

	// ======================================================================
	// Instance variables
	// ======================================================================

	private final List<EntityConfig<? extends Deletable>> childConfigs = new ArrayList<>();

	private final List<EntityConfig<?>> inheritedConfigs = new ArrayList<>();
	private final List<EntityConfig<?>> mandatoryConfigs = new ArrayList<>();
	private final List<EntityConfig<?>> optionalConfigs = new ArrayList<>();

	private Comparator<? super T> comparator = Entity.COMPARATOR;

	private final Key<T> key;

	private EntityType entityType;

	private boolean reflexive;
	private String mimeType;

	// ======================================================================
	// Constructors
	// ======================================================================

	/**
	 * Constructor.
	 *
	 * @param key The {@link Key} this entity config is bound to.
	 */
	public EntityConfig(Key<T> key) {
		this.key = key;
	}

	// ======================================================================
	// Public methods
	// ======================================================================

	/**
	 * Returns the {@link Key} this entity config is bound to.
	 *
	 * @return The {@code Key} is returned.
	 */
	public Key<T> getKey() {
		return key;
	}

	/**
	 * Returns the {@link Entity} class.
	 *
	 * @return The {@code Entity} class is returned.
	 */
	public Class<T> getEntityClass() {
		return key.entityClass;
	}

	/**
	 * Returns the {@link StatusAttachable} class.
	 *
	 * @return Optional is empty if {@code StatusAttachable} is undefined.
	 */
	public Optional<Class<? extends StatusAttachable>> getStatusAttachableClass() {
		return Optional.ofNullable(key.statusAttachableClass);
	}

	/**
	 * Returns the {@link ContextType}.
	 *
	 * @return Optional is empty {@code ContextType} is undefined.
	 */
	public Optional<ContextType> getContextType() {
		return Optional.ofNullable(key.contextType);
	}

	/**
	 * Returns the {@link Comparator} for {@link Entity}s associated with this
	 * entity config.
	 *
	 * @return The {@code Comparator} is returned.
	 */
	public Comparator<? super T> getComparator() {
		return comparator;
	}

	/**
	 * Returns the {@link EntityType} of this entity config.
	 *
	 * @return The {@code EntityType} is returned.
	 */
	public EntityType getEntityType() {
		return entityType;
	}

	/**
	 * Returns the default MIME type for a newly created {@link Entity}
	 * associated with this entity config.
	 *
	 * @return The default MIME type is returned.
	 */
	public String getMimeType() {
		return mimeType;
	}

	/**
	 * Returns all related child configs.
	 *
	 * @return The returned {@code List} is unmodifiable.
	 */
	public List<EntityConfig<? extends Deletable>> getChildConfigs() {
		return Collections.unmodifiableList(childConfigs);
	}

	/**
	 * Returns all related inherited configs.
	 *
	 * @return The returned {@code List} is unmodifiable.
	 */
	public List<EntityConfig<?>> getInheritedConfigs() {
		return Collections.unmodifiableList(inheritedConfigs);
	}

	/**
	 * Returns all related mandatory configs.
	 *
	 * @return The returned {@code List} is unmodifiable.
	 */
	public List<EntityConfig<?>> getMandatoryConfigs() {
		return Collections.unmodifiableList(mandatoryConfigs);
	}

	/**
	 * Returns all related optional configs.
	 *
	 * @return The returned {@code List} is unmodifiable.
	 */
	public List<EntityConfig<?>> getOptionalConfigs() {
		return Collections.unmodifiableList(optionalConfigs);
	}

	/**
	 * Checks whether {@link Entity}s associated with this entity config may
	 * have children of the same type.
	 *
	 * @return Returns {@code true} if this entity config is reflexive.
	 */
	public boolean isReflexive() {
		return reflexive;
	}

	/**
	 * Sets a new {@link Comparator} for this entity config.
	 *
	 * @param comparator The new {@code Comparator}.
	 */
	public void setComparator(Comparator<? super T> comparator) {
		this.comparator = comparator;
	}

	/**
	 * Sets the {@link EntityType} for this entity config.
	 *
	 * @param entityType The {@code EntityType}.
	 * @throws IllegalStateException Thrown if {@link EntityType} is already
	 * 		defined.
	 */
	public void setEntityType(EntityType entityType) {
		if(this.entityType != null) {
			throw new IllegalStateException("It is not allowed to override the entity type.");
		}

		this.entityType = entityType;
	}

	/**
	 * Sets the default MIME type for newly created {@link Entity}s associated
	 * with this entity config.
	 *
	 * @param mimeType The default MIME type.
	 * @throws IllegalStateException Thrown if default MIME type is already defined.
	 */
	public void setMimeType(String mimeType) {
		if(this.mimeType != null && !this.mimeType.isEmpty()) {
			throw new IllegalStateException("It is not allowed to override the default MIME type.");
		}

		this.mimeType = mimeType;
	}

	/**
	 * Adds a related inherited {@link EntityConfig}.
	 *
	 * @param entityConfig The {@code EntityConfig}.
	 */
	public void addInherited(EntityConfig<?> entityConfig) {
		inheritedConfigs.add(entityConfig);
	}

	/**
	 * Adds a related mandatory {@link EntityConfig}.
	 *
	 * @param entityConfig The {@code EntityConfig}.
	 */
	public void addMandatory(EntityConfig<?> entityConfig) {
		mandatoryConfigs.add(entityConfig);
	}

	/**
	 * Adds a related optional {@link EntityConfig}.
	 *
	 * @param entityConfig The {@code EntityConfig}.
	 */
	public void addOptional(EntityConfig<?> entityConfig) {
		optionalConfigs.add(entityConfig);
	}

	/**
	 * Adds a related child {@link EntityConfig}.
	 *
	 * @param childConfig The {@code EntityConfig}.
	 */
	public void addChild(EntityConfig<? extends Deletable> childConfig) {
		if(this == childConfig) {
			reflexive = true;
		} else {
			childConfigs.add(childConfig);
		}
	}

	// ======================================================================
	// Inner classes
	// ======================================================================

	/**
	 * Used as an identifier for {@link EntityConfig}s.
	 *
	 * @param <T> The entity type.
	 */
	public static final class Key<T extends Entity> {

		// ======================================================================
		// Instance variables
		// ======================================================================

		final Class<? extends StatusAttachable> statusAttachableClass;
		final Class<T> entityClass;
		final ContextType contextType;

		// ======================================================================
		// Constructors
		// ======================================================================

		/**
		 * Constructor.
		 *
		 * @param entityClass The {@link Entity} class.
		 */
		public Key(Class<T> entityClass) {
			this.entityClass = entityClass;
			statusAttachableClass = null;
			contextType = null;
		}

		/**
		 * Constructor.
		 *
		 * @param entityClass The {@link Entity} class.
		 * @param statusAttachableClass The {@link StatusAttachable} class.
		 */
		public Key(Class<T> entityClass, Class<? extends StatusAttachable> statusAttachableClass) {
			this.statusAttachableClass = statusAttachableClass;
			this.entityClass = entityClass;
			contextType = null;
		}

		/**
		 * Constructor.
		 *
		 * @param entityClass The {@link Entity} class.
		 * @param contextType The {@link ContextType}.
		 */
		public Key(Class<T> entityClass, ContextType contextType) {
			this.entityClass = entityClass;
			this.contextType = contextType;
			statusAttachableClass = null;
		}

		// ======================================================================
		// Public methods
		// ======================================================================

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int hashCode() {
			return Objects.hash(entityClass, statusAttachableClass, contextType);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean equals(Object object) {
			// reference check (this == object) omitted
			if(object instanceof Key) {
				Key<?> other = (Key<?>) object;
				return Objects.equals(entityClass, other.entityClass) &&
						Objects.equals(statusAttachableClass, other.statusAttachableClass) &&
						Objects.equals(contextType, other.contextType);
			}

			return false;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder(entityClass.getSimpleName());

			if(statusAttachableClass != null) {
				sb.append('_').append(statusAttachableClass.getSimpleName());
			}

			if(contextType != null) {
				sb.append('_').append(contextType);
			}

			return sb.toString();
		}

	}

}
