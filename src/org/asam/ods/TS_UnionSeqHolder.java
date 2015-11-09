package org.asam.ods;
/**
 * Generated from IDL union "TS_UnionSeq".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class TS_UnionSeqHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TS_UnionSeq value;

	public TS_UnionSeqHolder ()
	{
	}
	public TS_UnionSeqHolder (final TS_UnionSeq initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TS_UnionSeqHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TS_UnionSeqHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TS_UnionSeqHelper.write (out, value);
	}
}
