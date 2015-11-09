package org.asam.ods;

/**
 * Generated from IDL interface "SecurityRights".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class SecurityRightsHolder	implements org.omg.CORBA.portable.Streamable{
	 public SecurityRights value;
	public SecurityRightsHolder()
	{
	}
	public SecurityRightsHolder (final SecurityRights initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return SecurityRightsHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = SecurityRightsHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		SecurityRightsHelper.write (_out,value);
	}
}
