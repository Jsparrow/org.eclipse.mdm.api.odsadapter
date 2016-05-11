package org.eclipse.mdm.api.odsadapter.search;

import java.util.List;

import org.eclipse.mdm.api.base.model.Entity;
import org.eclipse.mdm.api.base.query.DataAccessException;
import org.eclipse.mdm.api.base.query.Filter;

public interface EntityLoader {

	<T extends Entity> List<T> loadAll(Class<T> type, Filter filter) throws DataAccessException;

}
