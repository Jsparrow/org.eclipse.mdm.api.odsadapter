package org.asam.ods;

/**
 * Generated from IDL alias "SelItemSequence".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class SelItemSequenceHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.asam.ods.SelItem[] value;

	public SelItemSequenceHolder ()
	{
	}
	public SelItemSequenceHolder (final org.asam.ods.SelItem[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return SelItemSequenceHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = SelItemSequenceHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		SelItemSequenceHelper.write (out,value);
	}
}
