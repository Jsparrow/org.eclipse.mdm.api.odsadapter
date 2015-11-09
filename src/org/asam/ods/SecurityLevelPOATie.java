package org.asam.ods;

import org.omg.PortableServer.POA;

/**
 * Generated from IDL interface "SecurityLevel".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public class SecurityLevelPOATie
	extends SecurityLevelPOA
{
	private SecurityLevelOperations _delegate;

	private POA _poa;
	public SecurityLevelPOATie(SecurityLevelOperations delegate)
	{
		_delegate = delegate;
	}
	public SecurityLevelPOATie(SecurityLevelOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.asam.ods.SecurityLevel _this()
	{
		return org.asam.ods.SecurityLevelHelper.narrow(_this_object());
	}
	public org.asam.ods.SecurityLevel _this(org.omg.CORBA.ORB orb)
	{
		return org.asam.ods.SecurityLevelHelper.narrow(_this_object(orb));
	}
	public SecurityLevelOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(SecurityLevelOperations delegate)
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
