package org.asam.ods;

/**
 * Generated from IDL alias "ApplRelSequence".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class ApplRelSequenceHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.asam.ods.ApplRel[] value;

	public ApplRelSequenceHolder ()
	{
	}
	public ApplRelSequenceHolder (final org.asam.ods.ApplRel[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return ApplRelSequenceHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = ApplRelSequenceHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		ApplRelSequenceHelper.write (out,value);
	}
}
