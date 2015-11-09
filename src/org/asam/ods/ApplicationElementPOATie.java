package org.asam.ods;

import org.omg.PortableServer.POA;

/**
 * Generated from IDL interface "ApplicationElement".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public class ApplicationElementPOATie
	extends ApplicationElementPOA
{
	private ApplicationElementOperations _delegate;

	private POA _poa;
	public ApplicationElementPOATie(ApplicationElementOperations delegate)
	{
		_delegate = delegate;
	}
	public ApplicationElementPOATie(ApplicationElementOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.asam.ods.ApplicationElement _this()
	{
		return org.asam.ods.ApplicationElementHelper.narrow(_this_object());
	}
	public org.asam.ods.ApplicationElement _this(org.omg.CORBA.ORB orb)
	{
		return org.asam.ods.ApplicationElementHelper.narrow(_this_object(orb));
	}
	public ApplicationElementOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(ApplicationElementOperations delegate)
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
	public java.lang.String[] listAttributes(java.lang.String aaPattern) throws org.asam.ods.AoException
	{
		return _delegate.listAttributes(aaPattern);
	}

	public void setRights(org.asam.ods.InstanceElement usergroup, int rights, org.asam.ods.RightsSet set) throws org.asam.ods.AoException
	{
_delegate.setRights(usergroup,rights,set);
	}

	public java.lang.String getName() throws org.asam.ods.AoException
	{
		return _delegate.getName();
	}

	public org.asam.ods.InitialRight[] getInitialRights() throws org.asam.ods.AoException
	{
		return _delegate.getInitialRights();
	}

	public void removeAttribute(org.asam.ods.ApplicationAttribute applAttr) throws org.asam.ods.AoException
	{
_delegate.removeAttribute(applAttr);
	}

	public int getSecurityLevel() throws org.asam.ods.AoException
	{
		return _delegate.getSecurityLevel();
	}

	public org.asam.ods.ApplicationAttribute createAttribute() throws org.asam.ods.AoException
	{
		return _delegate.createAttribute();
	}

	public org.asam.ods.T_LONGLONG getId() throws org.asam.ods.AoException
	{
		return _delegate.getId();
	}

	public org.asam.ods.ApplicationRelation[] getRelationsByBaseName(java.lang.String baseRelName) throws org.asam.ods.AoException
	{
		return _delegate.getRelationsByBaseName(baseRelName);
	}

	public org.asam.ods.InstanceElement[] createInstances(org.asam.ods.NameValueSeqUnit[] attributes, org.asam.ods.ApplicationRelationInstanceElementSeq[] relatedInstances) throws org.asam.ods.AoException
	{
		return _delegate.createInstances(attributes,relatedInstances);
	}

	public void setSecurityLevel(int secLevel, org.asam.ods.RightsSet set) throws org.asam.ods.AoException
	{
_delegate.setSecurityLevel(secLevel,set);
	}

	public org.asam.ods.ApplicationRelation[] getInitialRightRelations() throws org.asam.ods.AoException
	{
		return _delegate.getInitialRightRelations();
	}

	public java.lang.String[] listRelatedElementsByRelationship(org.asam.ods.Relationship aeRelationship) throws org.asam.ods.AoException
	{
		return _delegate.listRelatedElementsByRelationship(aeRelationship);
	}

	public void setBaseElement(org.asam.ods.BaseElement baseElem) throws org.asam.ods.AoException
	{
_delegate.setBaseElement(baseElem);
	}

	public org.asam.ods.ApplicationAttribute getAttributeByBaseName(java.lang.String baName) throws org.asam.ods.AoException
	{
		return _delegate.getAttributeByBaseName(baName);
	}

	public org.asam.ods.InstanceElement getInstanceById(org.asam.ods.T_LONGLONG ieId) throws org.asam.ods.AoException
	{
		return _delegate.getInstanceById(ieId);
	}

	public org.asam.ods.NameIterator listInstances(java.lang.String aaPattern) throws org.asam.ods.AoException
	{
		return _delegate.listInstances(aaPattern);
	}

	public org.asam.ods.ApplicationElement[] getAllRelatedElements() throws org.asam.ods.AoException
	{
		return _delegate.getAllRelatedElements();
	}

	public java.lang.String[] listAllRelatedElements() throws org.asam.ods.AoException
	{
		return _delegate.listAllRelatedElements();
	}

	public org.asam.ods.ApplicationElement[] getRelatedElementsByRelationship(org.asam.ods.Relationship aeRelationship) throws org.asam.ods.AoException
	{
		return _delegate.getRelatedElementsByRelationship(aeRelationship);
	}

	public org.asam.ods.BaseElement getBaseElement() throws org.asam.ods.AoException
	{
		return _delegate.getBaseElement();
	}

	public org.asam.ods.ACL[] getRights() throws org.asam.ods.AoException
	{
		return _delegate.getRights();
	}

	public void setName(java.lang.String aeName) throws org.asam.ods.AoException
	{
_delegate.setName(aeName);
	}

	public org.asam.ods.ApplicationStructure getApplicationStructure() throws org.asam.ods.AoException
	{
		return _delegate.getApplicationStructure();
	}

	public void setInitialRights(org.asam.ods.InstanceElement usergroup, int rights, org.asam.ods.T_LONGLONG refAid, org.asam.ods.RightsSet set) throws org.asam.ods.AoException
	{
_delegate.setInitialRights(usergroup,rights,refAid,set);
	}

	public org.asam.ods.ApplicationAttribute[] getAttributes(java.lang.String aaPattern) throws org.asam.ods.AoException
	{
		return _delegate.getAttributes(aaPattern);
	}

	public org.asam.ods.ApplicationRelation[] getAllRelations() throws org.asam.ods.AoException
	{
		return _delegate.getAllRelations();
	}

	public org.asam.ods.InstanceElementIterator getInstances(java.lang.String iePattern) throws org.asam.ods.AoException
	{
		return _delegate.getInstances(iePattern);
	}

	public org.asam.ods.InstanceElement getInstanceByName(java.lang.String ieName) throws org.asam.ods.AoException
	{
		return _delegate.getInstanceByName(ieName);
	}

	public org.asam.ods.InstanceElement createInstance(java.lang.String ieName) throws org.asam.ods.AoException
	{
		return _delegate.createInstance(ieName);
	}

	public void setInitialRightRelation(org.asam.ods.ApplicationRelation applRel, boolean set) throws org.asam.ods.AoException
	{
_delegate.setInitialRightRelation(applRel,set);
	}

	public org.asam.ods.ApplicationRelation[] getRelationsByType(org.asam.ods.RelationType aeRelationType) throws org.asam.ods.AoException
	{
		return _delegate.getRelationsByType(aeRelationType);
	}

	public org.asam.ods.ApplicationAttribute getAttributeByName(java.lang.String aaName) throws org.asam.ods.AoException
	{
		return _delegate.getAttributeByName(aaName);
	}

	public void removeInstance(org.asam.ods.T_LONGLONG ieId, boolean recursive) throws org.asam.ods.AoException
	{
_delegate.removeInstance(ieId,recursive);
	}

}
