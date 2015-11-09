package org.asam.ods;

/**
 * Generated from IDL struct "NameUnit".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class NameUnitHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.asam.ods.NameUnit value;

	public NameUnitHolder ()
	{
	}
	public NameUnitHolder(final org.asam.ods.NameUnit initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.asam.ods.NameUnitHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.asam.ods.NameUnitHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.asam.ods.NameUnitHelper.write(_out, value);
	}
}
