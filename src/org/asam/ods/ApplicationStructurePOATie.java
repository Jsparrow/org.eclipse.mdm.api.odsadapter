package org.asam.ods;

import org.omg.PortableServer.POA;

/**
 * Generated from IDL interface "ApplicationStructure".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public class ApplicationStructurePOATie
	extends ApplicationStructurePOA
{
	private ApplicationStructureOperations _delegate;

	private POA _poa;
	public ApplicationStructurePOATie(ApplicationStructureOperations delegate)
	{
		_delegate = delegate;
	}
	public ApplicationStructurePOATie(ApplicationStructureOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.asam.ods.ApplicationStructure _this()
	{
		return org.asam.ods.ApplicationStructureHelper.narrow(_this_object());
	}
	public org.asam.ods.ApplicationStructure _this(org.omg.CORBA.ORB orb)
	{
		return org.asam.ods.ApplicationStructureHelper.narrow(_this_object(orb));
	}
	public ApplicationStructureOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(ApplicationStructureOperations delegate)
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
	public void removeEnumerationDefinition(java.lang.String enumName) throws org.asam.ods.AoException
	{
_delegate.removeEnumerationDefinition(enumName);
	}

	public java.lang.String[] listEnumerations() throws org.asam.ods.AoException
	{
		return _delegate.listEnumerations();
	}

	public void removeRelation(org.asam.ods.ApplicationRelation applRel) throws org.asam.ods.AoException
	{
_delegate.removeRelation(applRel);
	}

	public java.lang.String[] listTopLevelElements(java.lang.String aeType) throws org.asam.ods.AoException
	{
		return _delegate.listTopLevelElements(aeType);
	}

	public org.asam.ods.AoSession getSession() throws org.asam.ods.AoException
	{
		return _delegate.getSession();
	}

	public org.asam.ods.ApplicationElement createElement(org.asam.ods.BaseElement baseElem) throws org.asam.ods.AoException
	{
		return _delegate.createElement(baseElem);
	}

	public org.asam.ods.InstanceElement getInstanceByAsamPath(java.lang.String asamPath) throws org.asam.ods.AoException
	{
		return _delegate.getInstanceByAsamPath(asamPath);
	}

	public org.asam.ods.ApplicationRelation[] getRelations(org.asam.ods.ApplicationElement applElem1, org.asam.ods.ApplicationElement applElem2) throws org.asam.ods.AoException
	{
		return _delegate.getRelations(applElem1,applElem2);
	}

	public org.asam.ods.ApplicationElement[] getTopLevelElements(java.lang.String aeType) throws org.asam.ods.AoException
	{
		return _delegate.getTopLevelElements(aeType);
	}

	public java.lang.String[] listElements(java.lang.String aePattern) throws org.asam.ods.AoException
	{
		return _delegate.listElements(aePattern);
	}

	public org.asam.ods.ApplicationElement[] getElements(java.lang.String aePattern) throws org.asam.ods.AoException
	{
		return _delegate.getElements(aePattern);
	}

	public void createInstanceRelations(org.asam.ods.ApplicationRelation applRel, org.asam.ods.InstanceElement[] elemList1, org.asam.ods.InstanceElement[] elemList2) throws org.asam.ods.AoException
	{
_delegate.createInstanceRelations(applRel,elemList1,elemList2);
	}

	public java.lang.String[] listElementsByBaseType(java.lang.String aeType) throws org.asam.ods.AoException
	{
		return _delegate.listElementsByBaseType(aeType);
	}

	public org.asam.ods.ApplicationElement getElementById(org.asam.ods.T_LONGLONG aeId) throws org.asam.ods.AoException
	{
		return _delegate.getElementById(aeId);
	}

	public org.asam.ods.ApplicationRelation createRelation() throws org.asam.ods.AoException
	{
		return _delegate.createRelation();
	}

	public org.asam.ods.EnumerationDefinition getEnumerationDefinition(java.lang.String enumName) throws org.asam.ods.AoException
	{
		return _delegate.getEnumerationDefinition(enumName);
	}

	public org.asam.ods.EnumerationDefinition createEnumerationDefinition(java.lang.String enumName) throws org.asam.ods.AoException
	{
		return _delegate.createEnumerationDefinition(enumName);
	}

	public void check() throws org.asam.ods.AoException
	{
_delegate.check();
	}

	public void removeElement(org.asam.ods.ApplicationElement applElem) throws org.asam.ods.AoException
	{
_delegate.removeElement(applElem);
	}

	public org.asam.ods.ApplicationElement getElementByName(java.lang.String aeName) throws org.asam.ods.AoException
	{
		return _delegate.getElementByName(aeName);
	}

	public org.asam.ods.InstanceElement[] getInstancesById(org.asam.ods.ElemId[] ieIds) throws org.asam.ods.AoException
	{
		return _delegate.getInstancesById(ieIds);
	}

	public org.asam.ods.ApplicationElement[] getElementsByBaseType(java.lang.String aeType) throws org.asam.ods.AoException
	{
		return _delegate.getElementsByBaseType(aeType);
	}

}
