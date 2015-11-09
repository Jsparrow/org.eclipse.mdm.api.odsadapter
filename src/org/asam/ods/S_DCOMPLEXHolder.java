package org.asam.ods;

/**
 * Generated from IDL alias "S_DCOMPLEX".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class S_DCOMPLEXHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.asam.ods.T_DCOMPLEX[] value;

	public S_DCOMPLEXHolder ()
	{
	}
	public S_DCOMPLEXHolder (final org.asam.ods.T_DCOMPLEX[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return S_DCOMPLEXHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = S_DCOMPLEXHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		S_DCOMPLEXHelper.write (out,value);
	}
}
