package org.asam.ods;

/**
 * Generated from IDL alias "ApplicationElementSequence".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class ApplicationElementSequenceHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.asam.ods.ApplicationElement[] value;

	public ApplicationElementSequenceHolder ()
	{
	}
	public ApplicationElementSequenceHolder (final org.asam.ods.ApplicationElement[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return ApplicationElementSequenceHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = ApplicationElementSequenceHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		ApplicationElementSequenceHelper.write (out,value);
	}
}
