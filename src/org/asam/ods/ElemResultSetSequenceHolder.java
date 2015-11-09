package org.asam.ods;

/**
 * Generated from IDL alias "ElemResultSetSequence".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class ElemResultSetSequenceHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.asam.ods.ElemResultSet[] value;

	public ElemResultSetSequenceHolder ()
	{
	}
	public ElemResultSetSequenceHolder (final org.asam.ods.ElemResultSet[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return ElemResultSetSequenceHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = ElemResultSetSequenceHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		ElemResultSetSequenceHelper.write (out,value);
	}
}
