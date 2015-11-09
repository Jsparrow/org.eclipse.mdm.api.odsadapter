package org.asam.ods;

import org.omg.PortableServer.POA;

/**
 * Generated from IDL interface "BaseElement".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public class BaseElementPOATie
	extends BaseElementPOA
{
	private BaseElementOperations _delegate;

	private POA _poa;
	public BaseElementPOATie(BaseElementOperations delegate)
	{
		_delegate = delegate;
	}
	public BaseElementPOATie(BaseElementOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.asam.ods.BaseElement _this()
	{
		return org.asam.ods.BaseElementHelper.narrow(_this_object());
	}
	public org.asam.ods.BaseElement _this(org.omg.CORBA.ORB orb)
	{
		return org.asam.ods.BaseElementHelper.narrow(_this_object(orb));
	}
	public BaseElementOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(BaseElementOperations delegate)
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
	public org.asam.ods.BaseAttribute[] getAttributes(java.lang.String baPattern) throws org.asam.ods.AoException
	{
		return _delegate.getAttributes(baPattern);
	}

	public boolean isTopLevel() throws org.asam.ods.AoException
	{
		return _delegate.isTopLevel();
	}

	public org.asam.ods.BaseElement[] getRelatedElementsByRelationship(org.asam.ods.Relationship brRelationship) throws org.asam.ods.AoException
	{
		return _delegate.getRelatedElementsByRelationship(brRelationship);
	}

	public java.lang.String[] listRelatedElementsByRelationship(org.asam.ods.Relationship brRelationship) throws org.asam.ods.AoException
	{
		return _delegate.listRelatedElementsByRelationship(brRelationship);
	}

	public org.asam.ods.BaseRelation[] getRelationsByType(org.asam.ods.RelationType brRelationType) throws org.asam.ods.AoException
	{
		return _delegate.getRelationsByType(brRelationType);
	}

	public java.lang.String getType() throws org.asam.ods.AoException
	{
		return _delegate.getType();
	}

	public java.lang.String[] listAttributes(java.lang.String baPattern) throws org.asam.ods.AoException
	{
		return _delegate.listAttributes(baPattern);
	}

	public org.asam.ods.BaseRelation[] getAllRelations() throws org.asam.ods.AoException
	{
		return _delegate.getAllRelations();
	}

}
