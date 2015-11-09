package org.asam.ods;

/**
 * Generated from IDL interface "ApplElemAccess".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class ApplElemAccessHolder	implements org.omg.CORBA.portable.Streamable{
	 public ApplElemAccess value;
	public ApplElemAccessHolder()
	{
	}
	public ApplElemAccessHolder (final ApplElemAccess initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return ApplElemAccessHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = ApplElemAccessHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		ApplElemAccessHelper.write (_out,value);
	}
}
