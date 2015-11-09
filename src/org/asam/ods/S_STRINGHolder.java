package org.asam.ods;

/**
 * Generated from IDL alias "S_STRING".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class S_STRINGHolder
	implements org.omg.CORBA.portable.Streamable
{
	public java.lang.String[] value;

	public S_STRINGHolder ()
	{
	}
	public S_STRINGHolder (final java.lang.String[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return S_STRINGHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = S_STRINGHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		S_STRINGHelper.write (out,value);
	}
}
