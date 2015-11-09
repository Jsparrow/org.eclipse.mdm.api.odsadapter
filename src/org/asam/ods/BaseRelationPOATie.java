package org.asam.ods;

import org.omg.PortableServer.POA;

/**
 * Generated from IDL interface "BaseRelation".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public class BaseRelationPOATie
	extends BaseRelationPOA
{
	private BaseRelationOperations _delegate;

	private POA _poa;
	public BaseRelationPOATie(BaseRelationOperations delegate)
	{
		_delegate = delegate;
	}
	public BaseRelationPOATie(BaseRelationOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.asam.ods.BaseRelation _this()
	{
		return org.asam.ods.BaseRelationHelper.narrow(_this_object());
	}
	public org.asam.ods.BaseRelation _this(org.omg.CORBA.ORB orb)
	{
		return org.asam.ods.BaseRelationHelper.narrow(_this_object(orb));
	}
	public BaseRelationOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(BaseRelationOperations delegate)
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
	public java.lang.String getRelationName() throws org.asam.ods.AoException
	{
		return _delegate.getRelationName();
	}

	public org.asam.ods.Relationship getRelationship() throws org.asam.ods.AoException
	{
		return _delegate.getRelationship();
	}

	public org.asam.ods.RelationRange getRelationRange() throws org.asam.ods.AoException
	{
		return _delegate.getRelationRange();
	}

	public org.asam.ods.RelationRange getInverseRelationRange() throws org.asam.ods.AoException
	{
		return _delegate.getInverseRelationRange();
	}

	public org.asam.ods.BaseElement getElem1() throws org.asam.ods.AoException
	{
		return _delegate.getElem1();
	}

	public org.asam.ods.BaseElement getElem2() throws org.asam.ods.AoException
	{
		return _delegate.getElem2();
	}

	public java.lang.String getInverseRelationName() throws org.asam.ods.AoException
	{
		return _delegate.getInverseRelationName();
	}

	public org.asam.ods.Relationship getInverseRelationship() throws org.asam.ods.AoException
	{
		return _delegate.getInverseRelationship();
	}

	public org.asam.ods.RelationType getRelationType() throws org.asam.ods.AoException
	{
		return _delegate.getRelationType();
	}

}
