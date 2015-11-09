package org.asam.ods;

/**
 * Generated from IDL alias "InitialRightSequence".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class InitialRightSequenceHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.asam.ods.InitialRight[] value;

	public InitialRightSequenceHolder ()
	{
	}
	public InitialRightSequenceHolder (final org.asam.ods.InitialRight[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return InitialRightSequenceHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = InitialRightSequenceHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		InitialRightSequenceHelper.write (out,value);
	}
}
