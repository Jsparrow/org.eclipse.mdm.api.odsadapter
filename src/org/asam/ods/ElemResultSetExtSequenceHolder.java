package org.asam.ods;

/**
 * Generated from IDL alias "ElemResultSetExtSequence".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class ElemResultSetExtSequenceHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.asam.ods.ElemResultSetExt[] value;

	public ElemResultSetExtSequenceHolder ()
	{
	}
	public ElemResultSetExtSequenceHolder (final org.asam.ods.ElemResultSetExt[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return ElemResultSetExtSequenceHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = ElemResultSetExtSequenceHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		ElemResultSetExtSequenceHelper.write (out,value);
	}
}
