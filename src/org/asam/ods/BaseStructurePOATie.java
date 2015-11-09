package org.asam.ods;

import org.omg.PortableServer.POA;

/**
 * Generated from IDL interface "BaseStructure".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public class BaseStructurePOATie
	extends BaseStructurePOA
{
	private BaseStructureOperations _delegate;

	private POA _poa;
	public BaseStructurePOATie(BaseStructureOperations delegate)
	{
		_delegate = delegate;
	}
	public BaseStructurePOATie(BaseStructureOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.asam.ods.BaseStructure _this()
	{
		return org.asam.ods.BaseStructureHelper.narrow(_this_object());
	}
	public org.asam.ods.BaseStructure _this(org.omg.CORBA.ORB orb)
	{
		return org.asam.ods.BaseStructureHelper.narrow(_this_object(orb));
	}
	public BaseStructureOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(BaseStructureOperations delegate)
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
	public java.lang.String[] listTopLevelElements(java.lang.String bePattern) throws org.asam.ods.AoException
	{
		return _delegate.listTopLevelElements(bePattern);
	}

	public org.asam.ods.BaseElement[] getTopLevelElements(java.lang.String bePattern) throws org.asam.ods.AoException
	{
		return _delegate.getTopLevelElements(bePattern);
	}

	public org.asam.ods.BaseElement[] getElements(java.lang.String bePattern) throws org.asam.ods.AoException
	{
		return _delegate.getElements(bePattern);
	}

	public org.asam.ods.BaseElement getElementByType(java.lang.String beType) throws org.asam.ods.AoException
	{
		return _delegate.getElementByType(beType);
	}

	public org.asam.ods.BaseRelation[] getRelations(org.asam.ods.BaseElement elem1, org.asam.ods.BaseElement elem2) throws org.asam.ods.AoException
	{
		return _delegate.getRelations(elem1,elem2);
	}

	public java.lang.String[] listElements(java.lang.String bePattern) throws org.asam.ods.AoException
	{
		return _delegate.listElements(bePattern);
	}

	public java.lang.String getVersion() throws org.asam.ods.AoException
	{
		return _delegate.getVersion();
	}

	public org.asam.ods.BaseRelation getRelation(org.asam.ods.BaseElement elem1, org.asam.ods.BaseElement elem2) throws org.asam.ods.AoException
	{
		return _delegate.getRelation(elem1,elem2);
	}

}
