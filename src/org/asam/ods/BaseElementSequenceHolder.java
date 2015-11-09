package org.asam.ods;

/**
 * Generated from IDL alias "BaseElementSequence".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class BaseElementSequenceHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.asam.ods.BaseElement[] value;

	public BaseElementSequenceHolder ()
	{
	}
	public BaseElementSequenceHolder (final org.asam.ods.BaseElement[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return BaseElementSequenceHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = BaseElementSequenceHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		BaseElementSequenceHelper.write (out,value);
	}
}
