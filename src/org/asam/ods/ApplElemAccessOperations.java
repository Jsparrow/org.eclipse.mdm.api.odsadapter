package org.asam.ods;


/**
 * Generated from IDL interface "ApplElemAccess".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public interface ApplElemAccessOperations
{
	/* constants */
	/* operations  */
	org.asam.ods.ElemResultSet[] getInstances(org.asam.ods.QueryStructure aoq, int how_many) throws org.asam.ods.AoException;
	org.asam.ods.T_LONGLONG[] getRelInst(org.asam.ods.ElemId elem, java.lang.String relName) throws org.asam.ods.AoException;
	void setRelInst(org.asam.ods.ElemId elem, java.lang.String relName, org.asam.ods.T_LONGLONG[] instIds, org.asam.ods.SetType type) throws org.asam.ods.AoException;
	org.asam.ods.ElemId[] insertInstances(org.asam.ods.AIDNameValueSeqUnitId[] val) throws org.asam.ods.AoException;
	void updateInstances(org.asam.ods.AIDNameValueSeqUnitId[] val) throws org.asam.ods.AoException;
	void deleteInstances(org.asam.ods.T_LONGLONG aid, org.asam.ods.T_LONGLONG[] instIds) throws org.asam.ods.AoException;
	org.asam.ods.ValueMatrix getValueMatrix(org.asam.ods.ElemId elem) throws org.asam.ods.AoException;
	void setAttributeRights(org.asam.ods.T_LONGLONG aid, java.lang.String attrName, org.asam.ods.T_LONGLONG usergroupId, int rights, org.asam.ods.RightsSet set) throws org.asam.ods.AoException;
	void setElementRights(org.asam.ods.T_LONGLONG aid, org.asam.ods.T_LONGLONG usergroupId, int rights, org.asam.ods.RightsSet set) throws org.asam.ods.AoException;
	void setInstanceRights(org.asam.ods.T_LONGLONG aid, org.asam.ods.T_LONGLONG[] instIds, org.asam.ods.T_LONGLONG usergroupId, int rights, org.asam.ods.RightsSet set) throws org.asam.ods.AoException;
	org.asam.ods.ACL[] getAttributeRights(org.asam.ods.T_LONGLONG aid, java.lang.String attrName) throws org.asam.ods.AoException;
	org.asam.ods.ACL[] getElementRights(org.asam.ods.T_LONGLONG aid) throws org.asam.ods.AoException;
	org.asam.ods.ACL[] getInstanceRights(org.asam.ods.T_LONGLONG aid, org.asam.ods.T_LONGLONG iid) throws org.asam.ods.AoException;
	void setElementInitialRights(org.asam.ods.T_LONGLONG aid, org.asam.ods.T_LONGLONG usergroupId, int rights, org.asam.ods.T_LONGLONG refAid, org.asam.ods.RightsSet set) throws org.asam.ods.AoException;
	void setInstanceInitialRights(org.asam.ods.T_LONGLONG aid, org.asam.ods.T_LONGLONG[] instIds, org.asam.ods.T_LONGLONG usergroupId, int rights, org.asam.ods.T_LONGLONG refAid, org.asam.ods.RightsSet set) throws org.asam.ods.AoException;
	void setInitialRightReference(org.asam.ods.T_LONGLONG aid, java.lang.String refName, org.asam.ods.RightsSet set) throws org.asam.ods.AoException;
	java.lang.String[] getInitialRightReference(org.asam.ods.T_LONGLONG aid) throws org.asam.ods.AoException;
	org.asam.ods.InitialRight[] getElementInitialRights(org.asam.ods.T_LONGLONG aid) throws org.asam.ods.AoException;
	org.asam.ods.InitialRight[] getInstanceInitialRights(org.asam.ods.T_LONGLONG aid, org.asam.ods.T_LONGLONG iid) throws org.asam.ods.AoException;
	org.asam.ods.ResultSetExt[] getInstancesExt(org.asam.ods.QueryStructureExt aoq, int how_many) throws org.asam.ods.AoException;
	org.asam.ods.ValueMatrix getValueMatrixInMode(org.asam.ods.ElemId elem, org.asam.ods.ValueMatrixMode vmMode) throws org.asam.ods.AoException;
	org.asam.ods.ODSFile getODSFile(org.asam.ods.ElemId elem) throws org.asam.ods.AoException;
}
