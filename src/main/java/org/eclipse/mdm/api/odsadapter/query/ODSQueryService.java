package org.eclipse.mdm.api.odsadapter.query;

import org.eclipse.mdm.api.base.query.Query;
import org.eclipse.mdm.api.base.query.QueryService;

public class ODSQueryService implements QueryService {

	private ODSModelManager modelManager;
	
	public ODSQueryService(ODSModelManager modelManager) {
		this.modelManager = modelManager;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Query createQuery() {
		return new ODSQuery(modelManager.getApplElemAccess());
	}
}
