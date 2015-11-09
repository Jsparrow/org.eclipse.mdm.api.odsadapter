package org.asam.ods;

/**
 * Generated from IDL struct "TS_ValueSeq".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class TS_ValueSeqHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.asam.ods.TS_ValueSeq value;

	public TS_ValueSeqHolder ()
	{
	}
	public TS_ValueSeqHolder(final org.asam.ods.TS_ValueSeq initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.asam.ods.TS_ValueSeqHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.asam.ods.TS_ValueSeqHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.asam.ods.TS_ValueSeqHelper.write(_out, value);
	}
}
