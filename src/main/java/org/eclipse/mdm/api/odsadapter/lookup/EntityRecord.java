package org.eclipse.mdm.api.odsadapter.lookup;

import org.eclipse.mdm.api.base.model.Core;
import org.eclipse.mdm.api.base.model.Entity;

/**
 * Utility class to group {@link Entity} and its {@link Core}.
 *
 * @param <T>
 *            The entity type.
 * @since 1.0.0
 * @author Viktor Stoehr, Gigatronik Ingolstadt GmbH
 */
public final class EntityRecord<T extends Entity> {

	// ======================================================================
	// Instance variables
	// ======================================================================

	final Core core;
	final T entity;

	// ======================================================================
	// Constructors
	// ======================================================================

	/**
	 * Constructor.
	 *
	 * @param entity
	 *            The {@link Entity}.
	 * @param core
	 *            The {@link Core} of the {@code Entity}.
	 */
	EntityRecord(T entity, Core core) {
		this.entity = entity;
		this.core = core;
	}

}
