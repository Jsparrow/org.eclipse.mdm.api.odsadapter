package org.asam.ods;
/**
 * Generated from IDL enum "SeverityFlag".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class SeverityFlagHolder
	implements org.omg.CORBA.portable.Streamable
{
	public SeverityFlag value;

	public SeverityFlagHolder ()
	{
	}
	public SeverityFlagHolder (final SeverityFlag initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return SeverityFlagHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = SeverityFlagHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		SeverityFlagHelper.write (out,value);
	}
}
