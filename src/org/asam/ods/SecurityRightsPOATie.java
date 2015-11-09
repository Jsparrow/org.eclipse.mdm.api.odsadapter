package org.asam.ods;

import org.omg.PortableServer.POA;

/**
 * Generated from IDL interface "SecurityRights".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public class SecurityRightsPOATie
	extends SecurityRightsPOA
{
	private SecurityRightsOperations _delegate;

	private POA _poa;
	public SecurityRightsPOATie(SecurityRightsOperations delegate)
	{
		_delegate = delegate;
	}
	public SecurityRightsPOATie(SecurityRightsOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.asam.ods.SecurityRights _this()
	{
		return org.asam.ods.SecurityRightsHelper.narrow(_this_object());
	}
	public org.asam.ods.SecurityRights _this(org.omg.CORBA.ORB orb)
	{
		return org.asam.ods.SecurityRightsHelper.narrow(_this_object(orb));
	}
	public SecurityRightsOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(SecurityRightsOperations delegate)
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
