package org.asam.ods;

/**
 * Generated from IDL alias "NameValueSeqUnitSequence".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class NameValueSeqUnitSequenceHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.asam.ods.NameValueSeqUnit[] value;

	public NameValueSeqUnitSequenceHolder ()
	{
	}
	public NameValueSeqUnitSequenceHolder (final org.asam.ods.NameValueSeqUnit[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return NameValueSeqUnitSequenceHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = NameValueSeqUnitSequenceHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		NameValueSeqUnitSequenceHelper.write (out,value);
	}
}
