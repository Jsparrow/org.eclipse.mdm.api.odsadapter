package org.asam.ods;

/**
 * Generated from IDL alias "EnumerationStructureSequence".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class EnumerationStructureSequenceHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.asam.ods.EnumerationStructure[] value;

	public EnumerationStructureSequenceHolder ()
	{
	}
	public EnumerationStructureSequenceHolder (final org.asam.ods.EnumerationStructure[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return EnumerationStructureSequenceHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = EnumerationStructureSequenceHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		EnumerationStructureSequenceHelper.write (out,value);
	}
}
