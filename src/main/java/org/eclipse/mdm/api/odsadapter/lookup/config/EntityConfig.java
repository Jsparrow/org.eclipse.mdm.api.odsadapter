package org.eclipse.mdm.api.odsadapter.lookup.config;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.eclipse.mdm.api.base.model.ContextType;
import org.eclipse.mdm.api.base.model.Deletable;
import org.eclipse.mdm.api.base.model.Entity;
import org.eclipse.mdm.api.base.model.StatusAttachable;
import org.eclipse.mdm.api.base.query.EntityType;

public class EntityConfig<T extends Entity> {

	private final List<EntityConfig<? extends Deletable>> childConfigs = new ArrayList<>();

	private final List<EntityConfig<?>> inheritedConfigs = new ArrayList<>();
	private final List<EntityConfig<?>> mandatoryConfigs = new ArrayList<>();
	private final List<EntityConfig<?>> optionalConfigs = new ArrayList<>();

	private Comparator<? super T> comparator = Entity.COMPARATOR;

	private final Key<T> key;

	private EntityType entityType;

	private boolean reflexive;
	private String mimeType;

	public EntityConfig(Key<T> key) {
		this.key = key;
	}

	public Key<T> getKey() {
		return new Key<>(this);
	}

	public Class<T> getEntityClass() {
		return key.entityClass;
	}

	public Optional<Class<? extends StatusAttachable>> getStatusAttachableClass() {
		return Optional.ofNullable(key.statusAttachableClass);
	}

	public Optional<ContextType> getContextType() {
		return Optional.ofNullable(key.contextType);
	}

	public Comparator<? super T> getComparator() {
		return comparator;
	}

	public EntityType getEntityType() {
		return entityType;
	}

	public String getMimeType() {
		return mimeType;
	}

	public List<EntityConfig<? extends Deletable>> getChildConfigs() {
		return childConfigs;
	}

	public List<EntityConfig<?>> getInheritedConfigs() {
		// TODO Name is bescheiden...
		return inheritedConfigs;
	}

	public List<EntityConfig<?>> getMandatoryConfigs() {
		return mandatoryConfigs;
	}

	public List<EntityConfig<?>> getOptionalConfigs() {
		return optionalConfigs;
	}

	public boolean isReflexive() {
		return reflexive;
	}

	// ##### HIDE SETTER

	public void setComparator(Comparator<? super T> comparator) {
		this.comparator = comparator;
	}

	public void setEntityType(EntityType entityType) {
		this.entityType = entityType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public void addInherited(EntityConfig<?> entityConfig) {
		inheritedConfigs.add(entityConfig);
	}

	public void addMandatory(EntityConfig<?> entityConfig) {
		mandatoryConfigs.add(entityConfig);
	}

	public void addOptional(EntityConfig<?> entityConfig) {
		optionalConfigs.add(entityConfig);
	}

	public void addChild(EntityConfig<? extends Deletable> childConfig) {
		if(this == childConfig) {
			reflexive = true;
		} else {
			childConfigs.add(childConfig);
		}
	}

	public static final class Key<T extends Entity> {

		final Class<? extends StatusAttachable> statusAttachableClass;
		final Class<T> entityClass;
		final ContextType contextType;

		Key(EntityConfig<T> entityConfig) {
			statusAttachableClass = entityConfig.getStatusAttachableClass().orElse(null);
			contextType = entityConfig.getContextType().orElse(null);
			entityClass = entityConfig.getEntityClass();
		}

		public Key(Class<T> entityClass) {
			this.entityClass = entityClass;
			statusAttachableClass = null;
			contextType = null;
		}

		public Key(Class<T> entityClass, Class<? extends StatusAttachable> statusAttachableClass) {
			this.statusAttachableClass = statusAttachableClass;
			this.entityClass = entityClass;
			contextType = null;
		}

		public Key(Class<T> entityClass, ContextType contextType) {
			this.entityClass = entityClass;
			this.contextType = contextType;
			statusAttachableClass = null;
		}

		@Override
		public int hashCode() {
			return Objects.hash(entityClass, statusAttachableClass, contextType);
		}

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

	}

}
