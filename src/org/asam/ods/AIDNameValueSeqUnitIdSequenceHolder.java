package org.asam.ods;

/**
 * Generated from IDL alias "AIDNameValueSeqUnitIdSequence".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class AIDNameValueSeqUnitIdSequenceHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.asam.ods.AIDNameValueSeqUnitId[] value;

	public AIDNameValueSeqUnitIdSequenceHolder ()
	{
	}
	public AIDNameValueSeqUnitIdSequenceHolder (final org.asam.ods.AIDNameValueSeqUnitId[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return AIDNameValueSeqUnitIdSequenceHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = AIDNameValueSeqUnitIdSequenceHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		AIDNameValueSeqUnitIdSequenceHelper.write (out,value);
	}
}
