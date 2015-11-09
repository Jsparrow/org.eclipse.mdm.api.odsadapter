package org.asam.ods;

/**
 * Generated from IDL interface "SecurityLevel".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class SecurityLevelHolder	implements org.omg.CORBA.portable.Streamable{
	 public SecurityLevel value;
	public SecurityLevelHolder()
	{
	}
	public SecurityLevelHolder (final SecurityLevel initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return SecurityLevelHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = SecurityLevelHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		SecurityLevelHelper.write (_out,value);
	}
}
