package org.asam.ods;

import org.omg.PortableServer.POA;

/**
 * Generated from IDL interface "LockMode".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public class LockModePOATie
	extends LockModePOA
{
	private LockModeOperations _delegate;

	private POA _poa;
	public LockModePOATie(LockModeOperations delegate)
	{
		_delegate = delegate;
	}
	public LockModePOATie(LockModeOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.asam.ods.LockMode _this()
	{
		return org.asam.ods.LockModeHelper.narrow(_this_object());
	}
	public org.asam.ods.LockMode _this(org.omg.CORBA.ORB orb)
	{
		return org.asam.ods.LockModeHelper.narrow(_this_object(orb));
	}
	public LockModeOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(LockModeOperations delegate)
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
}
