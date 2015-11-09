package org.asam.ods;


/**
 * Generated from IDL interface "SecurityRights".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public abstract class SecurityRightsPOA
	extends org.omg.PortableServer.Servant
	implements org.omg.CORBA.portable.InvokeHandler, org.asam.ods.SecurityRightsOperations
{
	private String[] ids = {"IDL:org/asam/ods/SecurityRights:1.0"};
	public org.asam.ods.SecurityRights _this()
	{
		return org.asam.ods.SecurityRightsHelper.narrow(_this_object());
	}
	public org.asam.ods.SecurityRights _this(org.omg.CORBA.ORB orb)
	{
		return org.asam.ods.SecurityRightsHelper.narrow(_this_object(orb));
	}
	public org.omg.CORBA.portable.OutputStream _invoke(String method, org.omg.CORBA.portable.InputStream _input, org.omg.CORBA.portable.ResponseHandler handler)
		throws org.omg.CORBA.SystemException
	{
		throw new org.omg.CORBA.BAD_OPERATION(method + " not found");
	}

	public String[] _all_interfaces(org.omg.PortableServer.POA poa, byte[] obj_id)
	{
		return ids;
	}
}
