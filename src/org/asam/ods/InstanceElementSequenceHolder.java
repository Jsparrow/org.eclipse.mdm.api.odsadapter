package org.asam.ods;

/**
 * Generated from IDL alias "InstanceElementSequence".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class InstanceElementSequenceHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.asam.ods.InstanceElement[] value;

	public InstanceElementSequenceHolder ()
	{
	}
	public InstanceElementSequenceHolder (final org.asam.ods.InstanceElement[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return InstanceElementSequenceHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = InstanceElementSequenceHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		InstanceElementSequenceHelper.write (out,value);
	}
}
