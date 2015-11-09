package org.asam.ods;

/**
 * Generated from IDL struct "ApplElem".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class ApplElemHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.asam.ods.ApplElem value;

	public ApplElemHolder ()
	{
	}
	public ApplElemHolder(final org.asam.ods.ApplElem initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.asam.ods.ApplElemHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.asam.ods.ApplElemHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.asam.ods.ApplElemHelper.write(_out, value);
	}
}
