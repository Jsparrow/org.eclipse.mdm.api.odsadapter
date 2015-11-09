package org.asam.ods;

/**
 * Generated from IDL alias "SelAIDNameUnitIdSequence".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class SelAIDNameUnitIdSequenceHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.asam.ods.SelAIDNameUnitId[] value;

	public SelAIDNameUnitIdSequenceHolder ()
	{
	}
	public SelAIDNameUnitIdSequenceHolder (final org.asam.ods.SelAIDNameUnitId[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return SelAIDNameUnitIdSequenceHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = SelAIDNameUnitIdSequenceHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		SelAIDNameUnitIdSequenceHelper.write (out,value);
	}
}
