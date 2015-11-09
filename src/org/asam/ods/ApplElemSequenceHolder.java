package org.asam.ods;

/**
 * Generated from IDL alias "ApplElemSequence".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class ApplElemSequenceHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.asam.ods.ApplElem[] value;

	public ApplElemSequenceHolder ()
	{
	}
	public ApplElemSequenceHolder (final org.asam.ods.ApplElem[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return ApplElemSequenceHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = ApplElemSequenceHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		ApplElemSequenceHelper.write (out,value);
	}
}
