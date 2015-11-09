package org.asam.ods;

/**
 * Generated from IDL alias "ElemIdSequence".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class ElemIdSequenceHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.asam.ods.ElemId[] value;

	public ElemIdSequenceHolder ()
	{
	}
	public ElemIdSequenceHolder (final org.asam.ods.ElemId[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return ElemIdSequenceHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = ElemIdSequenceHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		ElemIdSequenceHelper.write (out,value);
	}
}
