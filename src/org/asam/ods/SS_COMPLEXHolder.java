package org.asam.ods;

/**
 * Generated from IDL alias "SS_COMPLEX".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class SS_COMPLEXHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.asam.ods.T_COMPLEX[][] value;

	public SS_COMPLEXHolder ()
	{
	}
	public SS_COMPLEXHolder (final org.asam.ods.T_COMPLEX[][] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return SS_COMPLEXHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = SS_COMPLEXHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		SS_COMPLEXHelper.write (out,value);
	}
}
