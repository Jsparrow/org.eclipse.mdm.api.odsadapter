package org.asam.ods;

import org.omg.PortableServer.POA;

/**
 * Generated from IDL interface "ODSFile".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public class ODSFilePOATie
	extends ODSFilePOA
{
	private ODSFileOperations _delegate;

	private POA _poa;
	public ODSFilePOATie(ODSFileOperations delegate)
	{
		_delegate = delegate;
	}
	public ODSFilePOATie(ODSFileOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.asam.ods.ODSFile _this()
	{
		return org.asam.ods.ODSFileHelper.narrow(_this_object());
	}
	public org.asam.ods.ODSFile _this(org.omg.CORBA.ORB orb)
	{
		return org.asam.ods.ODSFileHelper.narrow(_this_object(orb));
	}
	public ODSFileOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(ODSFileOperations delegate)
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
	public void setValue(org.asam.ods.NameValueUnit value) throws org.asam.ods.AoException
	{
_delegate.setValue(value);
	}

	public org.asam.ods.ODSWriteTransfer create() throws org.asam.ods.AoException
	{
		return _delegate.create();
	}

	public java.lang.String getDate() throws org.asam.ods.AoException
	{
		return _delegate.getDate();
	}

	public org.asam.ods.T_LONGLONG getSize() throws org.asam.ods.AoException
	{
		return _delegate.getSize();
	}

	public org.asam.ods.ODSWriteTransfer append() throws org.asam.ods.AoException
	{
		return _delegate.append();
	}

	public org.asam.ods.ApplicationElement getApplicationElement() throws org.asam.ods.AoException
	{
		return _delegate.getApplicationElement();
	}

	public void removeRelation(org.asam.ods.ApplicationRelation applRel, org.asam.ods.InstanceElement instElem_nm) throws org.asam.ods.AoException
	{
_delegate.removeRelation(applRel,instElem_nm);
	}

	public void setRights(org.asam.ods.InstanceElement usergroup, int rights, org.asam.ods.RightsSet set) throws org.asam.ods.AoException
	{
_delegate.setRights(usergroup,rights,set);
	}

	public org.asam.ods.SubMatrix upcastSubMatrix() throws org.asam.ods.AoException
	{
		return _delegate.upcastSubMatrix();
	}

	public org.asam.ods.NameValueUnit getValueByBaseName(java.lang.String baseAttrName) throws org.asam.ods.AoException
	{
		return _delegate.getValueByBaseName(baseAttrName);
	}

	public org.asam.ods.Measurement upcastMeasurement() throws org.asam.ods.AoException
	{
		return _delegate.upcastMeasurement();
	}

	public boolean canWrite() throws org.asam.ods.AoException
	{
		return _delegate.canWrite();
	}

	public void setValueSeq(org.asam.ods.NameValueUnit[] values) throws org.asam.ods.AoException
	{
_delegate.setValueSeq(values);
	}

	public void renameInstanceAttribute(java.lang.String oldName, java.lang.String newName) throws org.asam.ods.AoException
	{
_delegate.renameInstanceAttribute(oldName,newName);
	}

	public org.asam.ods.InstanceElementIterator getRelatedInstances(org.asam.ods.ApplicationRelation applRel, java.lang.String iePattern) throws org.asam.ods.AoException
	{
		return _delegate.getRelatedInstances(applRel,iePattern);
	}

	public org.asam.ods.InstanceElement[] createRelatedInstances(org.asam.ods.ApplicationRelation applRel, org.asam.ods.NameValueSeqUnit[] attributes, org.asam.ods.ApplicationRelationInstanceElementSeq[] relatedInstances) throws org.asam.ods.AoException
	{
		return _delegate.createRelatedInstances(applRel,attributes,relatedInstances);
	}

	public void remove() throws org.asam.ods.AoException
	{
_delegate.remove();
	}

	public java.lang.String getName() throws org.asam.ods.AoException
	{
		return _delegate.getName();
	}

	public org.asam.ods.InitialRight[] getInitialRights() throws org.asam.ods.AoException
	{
		return _delegate.getInitialRights();
	}

	public org.asam.ods.InstanceElement deepCopy(java.lang.String newName, java.lang.String newVersion) throws org.asam.ods.AoException
	{
		return _delegate.deepCopy(newName,newVersion);
	}

	public void addInstanceAttribute(org.asam.ods.NameValueUnit instAttr) throws org.asam.ods.AoException
	{
_delegate.addInstanceAttribute(instAttr);
	}

	public boolean canRead() throws org.asam.ods.AoException
	{
		return _delegate.canRead();
	}

	public java.lang.String getAsamPath() throws org.asam.ods.AoException
	{
		return _delegate.getAsamPath();
	}

	public org.asam.ods.InstanceElementIterator getRelatedInstancesByRelationship(org.asam.ods.Relationship ieRelationship, java.lang.String iePattern) throws org.asam.ods.AoException
	{
		return _delegate.getRelatedInstancesByRelationship(ieRelationship,iePattern);
	}

	public void removeFromControl(java.lang.String targetUrl) throws org.asam.ods.AoException
	{
_delegate.removeFromControl(targetUrl);
	}

	public void destroy() throws org.asam.ods.AoException
	{
_delegate.destroy();
	}

	public org.asam.ods.T_LONGLONG getId() throws org.asam.ods.AoException
	{
		return _delegate.getId();
	}

	public void removeInstanceAttribute(java.lang.String attrName) throws org.asam.ods.AoException
	{
_delegate.removeInstanceAttribute(attrName);
	}

	public org.asam.ods.ACL[] getRights() throws org.asam.ods.AoException
	{
		return _delegate.getRights();
	}

	public org.asam.ods.InstanceElement shallowCopy(java.lang.String newName, java.lang.String newVersion) throws org.asam.ods.AoException
	{
		return _delegate.shallowCopy(newName,newVersion);
	}

	public java.lang.String[] listAttributes(java.lang.String iaPattern, org.asam.ods.AttrType aType) throws org.asam.ods.AoException
	{
		return _delegate.listAttributes(iaPattern,aType);
	}

	public org.asam.ods.NameValueUnit[] getValueSeq(java.lang.String[] attrNames) throws org.asam.ods.AoException
	{
		return _delegate.getValueSeq(attrNames);
	}

	public org.asam.ods.T_LONGLONG compare(org.asam.ods.InstanceElement compIeObj) throws org.asam.ods.AoException
	{
		return _delegate.compare(compIeObj);
	}

	public boolean exists() throws org.asam.ods.AoException
	{
		return _delegate.exists();
	}

	public org.asam.ods.NameValueUnit getValueInUnit(org.asam.ods.NameUnit attr) throws org.asam.ods.AoException
	{
		return _delegate.getValueInUnit(attr);
	}

	public void createRelation(org.asam.ods.ApplicationRelation relation, org.asam.ods.InstanceElement instElem) throws org.asam.ods.AoException
	{
_delegate.createRelation(relation,instElem);
	}

	public void takeUnderControl(java.lang.String sourceUrl) throws org.asam.ods.AoException
	{
_delegate.takeUnderControl(sourceUrl);
	}

	public org.asam.ods.NameIterator listRelatedInstancesByRelationship(org.asam.ods.Relationship ieRelationship, java.lang.String iePattern) throws org.asam.ods.AoException
	{
		return _delegate.listRelatedInstancesByRelationship(ieRelationship,iePattern);
	}

	public org.asam.ods.NameIterator listRelatedInstances(org.asam.ods.ApplicationRelation ieRelation, java.lang.String iePattern) throws org.asam.ods.AoException
	{
		return _delegate.listRelatedInstances(ieRelation,iePattern);
	}

	public org.asam.ods.NameValueUnit getValue(java.lang.String attrName) throws org.asam.ods.AoException
	{
		return _delegate.getValue(attrName);
	}

	public void setInitialRights(org.asam.ods.InstanceElement usergroup, int rights, org.asam.ods.T_LONGLONG refAid, org.asam.ods.RightsSet set) throws org.asam.ods.AoException
	{
_delegate.setInitialRights(usergroup,rights,refAid,set);
	}

	public org.asam.ods.ODSReadTransfer read() throws org.asam.ods.AoException
	{
		return _delegate.read();
	}

	public org.asam.ods.ODSFile upcastODSFile() throws org.asam.ods.AoException
	{
		return _delegate.upcastODSFile();
	}

	public void setName(java.lang.String iaName) throws org.asam.ods.AoException
	{
_delegate.setName(iaName);
	}

}
