package org.asam.ods;

/**
 * Generated from IDL alias "SubMatrixSequence".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class SubMatrixSequenceHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.asam.ods.SubMatrix[] value;

	public SubMatrixSequenceHolder ()
	{
	}
	public SubMatrixSequenceHolder (final org.asam.ods.SubMatrix[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return SubMatrixSequenceHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = SubMatrixSequenceHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		SubMatrixSequenceHelper.write (out,value);
	}
}
