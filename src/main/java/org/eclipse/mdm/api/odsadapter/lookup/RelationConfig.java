package org.eclipse.mdm.api.odsadapter.lookup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.eclipse.mdm.api.base.query.EntityType;
import org.eclipse.mdm.api.base.query.Record;
import org.eclipse.mdm.api.base.query.Relation;
import org.eclipse.mdm.api.odsadapter.lookup.config.EntityConfig;

public final class RelationConfig {

	/*
	 * TODO: hide private fields -> getter / setter
	 */

	final Map<Long, List<EntityRecord<?>>> dependants = new HashMap<>();
	final EntityConfig<?> entityConfig;
	final Relation relation;
	final boolean mandatory;

	public RelationConfig(EntityType source, EntityConfig<?> entityConfig, boolean mandatory) {
		this.entityConfig = entityConfig;
		relation = source.getRelation(entityConfig.getEntityType());
		this.mandatory = mandatory;
	}

	public void add(EntityRecord<?> entityRecord, Record record) {
		Optional<Long> relatedEntityID = record.getID(relation);
		if(relatedEntityID.isPresent()) {
			dependants.computeIfAbsent(relatedEntityID.get(), k -> new ArrayList<>()).add(entityRecord);
		} else if(mandatory) {
			throw new IllegalStateException("Mandatory relation unsatisfied.");
		}
	}

}
