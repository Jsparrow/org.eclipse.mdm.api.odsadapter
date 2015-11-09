package org.asam.ods;

/**
 * Generated from IDL struct "T_DCOMPLEX".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class T_DCOMPLEXHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.asam.ods.T_DCOMPLEX value;

	public T_DCOMPLEXHolder ()
	{
	}
	public T_DCOMPLEXHolder(final org.asam.ods.T_DCOMPLEX initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.asam.ods.T_DCOMPLEXHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.asam.ods.T_DCOMPLEXHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.asam.ods.T_DCOMPLEXHelper.write(_out, value);
	}
}
