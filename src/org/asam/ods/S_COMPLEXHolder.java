package org.asam.ods;

/**
 * Generated from IDL alias "S_COMPLEX".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class S_COMPLEXHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.asam.ods.T_COMPLEX[] value;

	public S_COMPLEXHolder ()
	{
	}
	public S_COMPLEXHolder (final org.asam.ods.T_COMPLEX[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return S_COMPLEXHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = S_COMPLEXHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		S_COMPLEXHelper.write (out,value);
	}
}
