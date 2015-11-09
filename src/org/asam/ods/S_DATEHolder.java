package org.asam.ods;

/**
 * Generated from IDL alias "S_DATE".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class S_DATEHolder
	implements org.omg.CORBA.portable.Streamable
{
	public java.lang.String[] value;

	public S_DATEHolder ()
	{
	}
	public S_DATEHolder (final java.lang.String[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return S_DATEHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = S_DATEHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		S_DATEHelper.write (out,value);
	}
}
