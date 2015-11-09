package org.asam.ods;

/**
 * Generated from IDL alias "SelOperatorSequence".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class SelOperatorSequenceHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.asam.ods.SelOperator[] value;

	public SelOperatorSequenceHolder ()
	{
	}
	public SelOperatorSequenceHolder (final org.asam.ods.SelOperator[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return SelOperatorSequenceHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = SelOperatorSequenceHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		SelOperatorSequenceHelper.write (out,value);
	}
}
