package org.asam.ods;

import org.omg.PortableServer.POA;

/**
 * Generated from IDL interface "AoFactory".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public class AoFactoryPOATie
	extends AoFactoryPOA
{
	private AoFactoryOperations _delegate;

	private POA _poa;
	public AoFactoryPOATie(AoFactoryOperations delegate)
	{
		_delegate = delegate;
	}
	public AoFactoryPOATie(AoFactoryOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.asam.ods.AoFactory _this()
	{
		return org.asam.ods.AoFactoryHelper.narrow(_this_object());
	}
	public org.asam.ods.AoFactory _this(org.omg.CORBA.ORB orb)
	{
		return org.asam.ods.AoFactoryHelper.narrow(_this_object(orb));
	}
	public AoFactoryOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(AoFactoryOperations delegate)
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
	public org.asam.ods.AoSession newSessionNameValue(org.asam.ods.NameValue[] auth) throws org.asam.ods.AoException
	{
		return _delegate.newSessionNameValue(auth);
	}

	public org.asam.ods.AoSession newSession(java.lang.String auth) throws org.asam.ods.AoException
	{
		return _delegate.newSession(auth);
	}

	public java.lang.String getInterfaceVersion() throws org.asam.ods.AoException
	{
		return _delegate.getInterfaceVersion();
	}

	public java.lang.String getType() throws org.asam.ods.AoException
	{
		return _delegate.getType();
	}

	public java.lang.String getName() throws org.asam.ods.AoException
	{
		return _delegate.getName();
	}

	public java.lang.String getDescription() throws org.asam.ods.AoException
	{
		return _delegate.getDescription();
	}

}
