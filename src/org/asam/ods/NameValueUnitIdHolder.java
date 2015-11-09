package org.asam.ods;

/**
 * Generated from IDL struct "NameValueUnitId".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class NameValueUnitIdHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.asam.ods.NameValueUnitId value;

	public NameValueUnitIdHolder ()
	{
	}
	public NameValueUnitIdHolder(final org.asam.ods.NameValueUnitId initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.asam.ods.NameValueUnitIdHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.asam.ods.NameValueUnitIdHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.asam.ods.NameValueUnitIdHelper.write(_out, value);
	}
}
