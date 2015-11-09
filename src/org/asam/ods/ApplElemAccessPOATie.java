package org.asam.ods;

import org.omg.PortableServer.POA;

/**
 * Generated from IDL interface "ApplElemAccess".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public class ApplElemAccessPOATie
	extends ApplElemAccessPOA
{
	private ApplElemAccessOperations _delegate;

	private POA _poa;
	public ApplElemAccessPOATie(ApplElemAccessOperations delegate)
	{
		_delegate = delegate;
	}
	public ApplElemAccessPOATie(ApplElemAccessOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.asam.ods.ApplElemAccess _this()
	{
		return org.asam.ods.ApplElemAccessHelper.narrow(_this_object());
	}
	public org.asam.ods.ApplElemAccess _this(org.omg.CORBA.ORB orb)
	{
		return org.asam.ods.ApplElemAccessHelper.narrow(_this_object(orb));
	}
	public ApplElemAccessOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(ApplElemAccessOperations delegate)
	{
		_delegate = delegate;
	}
	public POA _default_POA()
	{
		if (_poa != null)
		{
			return _poa;
		}
		return super._default_POA();
	}
	public void updateInstances(org.asam.ods.AIDNameValueSeqUnitId[] val) throws org.asam.ods.AoException
	{
_delegate.updateInstances(val);
	}

	public org.asam.ods.ElemId[] insertInstances(org.asam.ods.AIDNameValueSeqUnitId[] val) throws org.asam.ods.AoException
	{
		return _delegate.insertInstances(val);
	}

	public void setElementRights(org.asam.ods.T_LONGLONG aid, org.asam.ods.T_LONGLONG usergroupId, int rights, org.asam.ods.RightsSet set) throws org.asam.ods.AoException
	{
_delegate.setElementRights(aid,usergroupId,rights,set);
	}

	public org.asam.ods.ACL[] getAttributeRights(org.asam.ods.T_LONGLONG aid, java.lang.String attrName) throws org.asam.ods.AoException
	{
		return _delegate.getAttributeRights(aid,attrName);
	}

	public java.lang.String[] getInitialRightReference(org.asam.ods.T_LONGLONG aid) throws org.asam.ods.AoException
	{
		return _delegate.getInitialRightReference(aid);
	}

	public void setInitialRightReference(org.asam.ods.T_LONGLONG aid, java.lang.String refName, org.asam.ods.RightsSet set) throws org.asam.ods.AoException
	{
_delegate.setInitialRightReference(aid,refName,set);
	}

	public void setInstanceInitialRights(org.asam.ods.T_LONGLONG aid, org.asam.ods.T_LONGLONG[] instIds, org.asam.ods.T_LONGLONG usergroupId, int rights, org.asam.ods.T_LONGLONG refAid, org.asam.ods.RightsSet set) throws org.asam.ods.AoException
	{
_delegate.setInstanceInitialRights(aid,instIds,usergroupId,rights,refAid,set);
	}

	public org.asam.ods.InitialRight[] getElementInitialRights(org.asam.ods.T_LONGLONG aid) throws org.asam.ods.AoException
	{
		return _delegate.getElementInitialRights(aid);
	}

	public org.asam.ods.ElemResultSet[] getInstances(org.asam.ods.QueryStructure aoq, int how_many) throws org.asam.ods.AoException
	{
		return _delegate.getInstances(aoq,how_many);
	}

	public void setElementInitialRights(org.asam.ods.T_LONGLONG aid, org.asam.ods.T_LONGLONG usergroupId, int rights, org.asam.ods.T_LONGLONG refAid, org.asam.ods.RightsSet set) throws org.asam.ods.AoException
	{
_delegate.setElementInitialRights(aid,usergroupId,rights,refAid,set);
	}

	public org.asam.ods.ACL[] getElementRights(org.asam.ods.T_LONGLONG aid) throws org.asam.ods.AoException
	{
		return _delegate.getElementRights(aid);
	}

	public void setInstanceRights(org.asam.ods.T_LONGLONG aid, org.asam.ods.T_LONGLONG[] instIds, org.asam.ods.T_LONGLONG usergroupId, int rights, org.asam.ods.RightsSet set) throws org.asam.ods.AoException
	{
_delegate.setInstanceRights(aid,instIds,usergroupId,rights,set);
	}

	public org.asam.ods.InitialRight[] getInstanceInitialRights(org.asam.ods.T_LONGLONG aid, org.asam.ods.T_LONGLONG iid) throws org.asam.ods.AoException
	{
		return _delegate.getInstanceInitialRights(aid,iid);
	}

	public org.asam.ods.ValueMatrix getValueMatrix(org.asam.ods.ElemId elem) throws org.asam.ods.AoException
	{
		return _delegate.getValueMatrix(elem);
	}

	public void setAttributeRights(org.asam.ods.T_LONGLONG aid, java.lang.String attrName, org.asam.ods.T_LONGLONG usergroupId, int rights, org.asam.ods.RightsSet set) throws org.asam.ods.AoException
	{
_delegate.setAttributeRights(aid,attrName,usergroupId,rights,set);
	}

	public org.asam.ods.ACL[] getInstanceRights(org.asam.ods.T_LONGLONG aid, org.asam.ods.T_LONGLONG iid) throws org.asam.ods.AoException
	{
		return _delegate.getInstanceRights(aid,iid);
	}

	public org.asam.ods.ResultSetExt[] getInstancesExt(org.asam.ods.QueryStructureExt aoq, int how_many) throws org.asam.ods.AoException
	{
		return _delegate.getInstancesExt(aoq,how_many);
	}

	public void deleteInstances(org.asam.ods.T_LONGLONG aid, org.asam.ods.T_LONGLONG[] instIds) throws org.asam.ods.AoException
	{
_delegate.deleteInstances(aid,instIds);
	}

	public org.asam.ods.ValueMatrix getValueMatrixInMode(org.asam.ods.ElemId elem, org.asam.ods.ValueMatrixMode vmMode) throws org.asam.ods.AoException
	{
		return _delegate.getValueMatrixInMode(elem,vmMode);
	}

	public org.asam.ods.ODSFile getODSFile(org.asam.ods.ElemId elem) throws org.asam.ods.AoException
	{
		return _delegate.getODSFile(elem);
	}

	public void setRelInst(org.asam.ods.ElemId elem, java.lang.String relName, org.asam.ods.T_LONGLONG[] instIds, org.asam.ods.SetType type) throws org.asam.ods.AoException
	{
_delegate.setRelInst(elem,relName,instIds,type);
	}

	public org.asam.ods.T_LONGLONG[] getRelInst(org.asam.ods.ElemId elem, java.lang.String relName) throws org.asam.ods.AoException
	{
		return _delegate.getRelInst(elem,relName);
	}

}
