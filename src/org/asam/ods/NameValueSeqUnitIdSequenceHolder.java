package org.asam.ods;

/**
 * Generated from IDL alias "NameValueSeqUnitIdSequence".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class NameValueSeqUnitIdSequenceHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.asam.ods.NameValueSeqUnitId[] value;

	public NameValueSeqUnitIdSequenceHolder ()
	{
	}
	public NameValueSeqUnitIdSequenceHolder (final org.asam.ods.NameValueSeqUnitId[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return NameValueSeqUnitIdSequenceHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = NameValueSeqUnitIdSequenceHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		NameValueSeqUnitIdSequenceHelper.write (out,value);
	}
}
