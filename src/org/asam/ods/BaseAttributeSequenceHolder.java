package org.asam.ods;

/**
 * Generated from IDL alias "BaseAttributeSequence".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class BaseAttributeSequenceHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.asam.ods.BaseAttribute[] value;

	public BaseAttributeSequenceHolder ()
	{
	}
	public BaseAttributeSequenceHolder (final org.asam.ods.BaseAttribute[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return BaseAttributeSequenceHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = BaseAttributeSequenceHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		BaseAttributeSequenceHelper.write (out,value);
	}
}
