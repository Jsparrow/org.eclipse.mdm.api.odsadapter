package org.asam.ods;

/**
 * Generated from IDL struct "AIDNameValueSeqUnitId".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class AIDNameValueSeqUnitIdHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.asam.ods.AIDNameValueSeqUnitId value;

	public AIDNameValueSeqUnitIdHolder ()
	{
	}
	public AIDNameValueSeqUnitIdHolder(final org.asam.ods.AIDNameValueSeqUnitId initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.asam.ods.AIDNameValueSeqUnitIdHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.asam.ods.AIDNameValueSeqUnitIdHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.asam.ods.AIDNameValueSeqUnitIdHelper.write(_out, value);
	}
}
