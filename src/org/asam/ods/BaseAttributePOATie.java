package org.asam.ods;

import org.omg.PortableServer.POA;

/**
 * Generated from IDL interface "BaseAttribute".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public class BaseAttributePOATie
	extends BaseAttributePOA
{
	private BaseAttributeOperations _delegate;

	private POA _poa;
	public BaseAttributePOATie(BaseAttributeOperations delegate)
	{
		_delegate = delegate;
	}
	public BaseAttributePOATie(BaseAttributeOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.asam.ods.BaseAttribute _this()
	{
		return org.asam.ods.BaseAttributeHelper.narrow(_this_object());
	}
	public org.asam.ods.BaseAttribute _this(org.omg.CORBA.ORB orb)
	{
		return org.asam.ods.BaseAttributeHelper.narrow(_this_object(orb));
	}
	public BaseAttributeOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(BaseAttributeOperations delegate)
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
	public boolean isUnique() throws org.asam.ods.AoException
	{
		return _delegate.isUnique();
	}

	public org.asam.ods.EnumerationDefinition getEnumerationDefinition() throws org.asam.ods.AoException
	{
		return _delegate.getEnumerationDefinition();
	}

	public boolean isObligatory() throws org.asam.ods.AoException
	{
		return _delegate.isObligatory();
	}

	public org.asam.ods.DataType getDataType() throws org.asam.ods.AoException
	{
		return _delegate.getDataType();
	}

	public org.asam.ods.BaseElement getBaseElement() throws org.asam.ods.AoException
	{
		return _delegate.getBaseElement();
	}

	public java.lang.String getName() throws org.asam.ods.AoException
	{
		return _delegate.getName();
	}

}
