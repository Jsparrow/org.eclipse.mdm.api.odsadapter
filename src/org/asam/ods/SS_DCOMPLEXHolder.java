package org.asam.ods;

/**
 * Generated from IDL alias "SS_DCOMPLEX".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class SS_DCOMPLEXHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.asam.ods.T_DCOMPLEX[][] value;

	public SS_DCOMPLEXHolder ()
	{
	}
	public SS_DCOMPLEXHolder (final org.asam.ods.T_DCOMPLEX[][] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return SS_DCOMPLEXHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = SS_DCOMPLEXHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		SS_DCOMPLEXHelper.write (out,value);
	}
}
