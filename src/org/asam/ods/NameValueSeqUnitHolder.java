package org.asam.ods;

/**
 * Generated from IDL struct "NameValueSeqUnit".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class NameValueSeqUnitHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.asam.ods.NameValueSeqUnit value;

	public NameValueSeqUnitHolder ()
	{
	}
	public NameValueSeqUnitHolder(final org.asam.ods.NameValueSeqUnit initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.asam.ods.NameValueSeqUnitHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.asam.ods.NameValueSeqUnitHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.asam.ods.NameValueSeqUnitHelper.write(_out, value);
	}
}
