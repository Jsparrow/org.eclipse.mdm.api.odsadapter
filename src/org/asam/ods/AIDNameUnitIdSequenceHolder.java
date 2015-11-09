package org.asam.ods;

/**
 * Generated from IDL alias "AIDNameUnitIdSequence".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class AIDNameUnitIdSequenceHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.asam.ods.AIDNameUnitId[] value;

	public AIDNameUnitIdSequenceHolder ()
	{
	}
	public AIDNameUnitIdSequenceHolder (final org.asam.ods.AIDNameUnitId[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return AIDNameUnitIdSequenceHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = AIDNameUnitIdSequenceHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		AIDNameUnitIdSequenceHelper.write (out,value);
	}
}
