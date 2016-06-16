package org.eclipse.mdm.api.odsadapter.lookup;

import org.eclipse.mdm.api.base.model.Entity;
import org.eclipse.mdm.api.base.model.Core;

public final class EntityRecord<T extends Entity> {

	final Core core;
	final T entity;

	EntityRecord(T entity, Core core) {
		this.entity = entity;
		this.core = core;
	}

}
