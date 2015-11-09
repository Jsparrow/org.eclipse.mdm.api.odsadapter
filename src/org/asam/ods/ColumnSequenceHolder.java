package org.asam.ods;

/**
 * Generated from IDL alias "ColumnSequence".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class ColumnSequenceHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.asam.ods.Column[] value;

	public ColumnSequenceHolder ()
	{
	}
	public ColumnSequenceHolder (final org.asam.ods.Column[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return ColumnSequenceHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = ColumnSequenceHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		ColumnSequenceHelper.write (out,value);
	}
}
