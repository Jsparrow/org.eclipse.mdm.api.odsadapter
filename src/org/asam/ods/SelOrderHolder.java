package org.asam.ods;

/**
 * Generated from IDL struct "SelOrder".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class SelOrderHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.asam.ods.SelOrder value;

	public SelOrderHolder ()
	{
	}
	public SelOrderHolder(final org.asam.ods.SelOrder initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.asam.ods.SelOrderHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.asam.ods.SelOrderHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.asam.ods.SelOrderHelper.write(_out, value);
	}
}
