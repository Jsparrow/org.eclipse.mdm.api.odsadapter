package org.asam.ods;

/**
 * Generated from IDL alias "AIDNameSequence".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class AIDNameSequenceHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.asam.ods.AIDName[] value;

	public AIDNameSequenceHolder ()
	{
	}
	public AIDNameSequenceHolder (final org.asam.ods.AIDName[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return AIDNameSequenceHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = AIDNameSequenceHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		AIDNameSequenceHelper.write (out,value);
	}
}
