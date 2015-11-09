package org.asam.ods;

/**
 * Generated from IDL alias "AttrResultSetSequence".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class AttrResultSetSequenceHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.asam.ods.AttrResultSet[] value;

	public AttrResultSetSequenceHolder ()
	{
	}
	public AttrResultSetSequenceHolder (final org.asam.ods.AttrResultSet[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return AttrResultSetSequenceHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = AttrResultSetSequenceHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		AttrResultSetSequenceHelper.write (out,value);
	}
}
