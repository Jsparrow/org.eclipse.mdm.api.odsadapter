package org.asam.ods;

/**
 * Generated from IDL alias "SelOrderSequence".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class SelOrderSequenceHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.asam.ods.SelOrder[] value;

	public SelOrderSequenceHolder ()
	{
	}
	public SelOrderSequenceHolder (final org.asam.ods.SelOrder[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return SelOrderSequenceHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = SelOrderSequenceHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		SelOrderSequenceHelper.write (out,value);
	}
}
