package org.asam.ods;

/**
 * Generated from IDL alias "ApplicationAttributeSequence".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class ApplicationAttributeSequenceHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.asam.ods.ApplicationAttribute[] value;

	public ApplicationAttributeSequenceHolder ()
	{
	}
	public ApplicationAttributeSequenceHolder (final org.asam.ods.ApplicationAttribute[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return ApplicationAttributeSequenceHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = ApplicationAttributeSequenceHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		ApplicationAttributeSequenceHelper.write (out,value);
	}
}
