package org.asam.ods;

/**
 * Generated from IDL alias "EnumerationItemStructureSequence".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class EnumerationItemStructureSequenceHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.asam.ods.EnumerationItemStructure[] value;

	public EnumerationItemStructureSequenceHolder ()
	{
	}
	public EnumerationItemStructureSequenceHolder (final org.asam.ods.EnumerationItemStructure[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return EnumerationItemStructureSequenceHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = EnumerationItemStructureSequenceHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		EnumerationItemStructureSequenceHelper.write (out,value);
	}
}
