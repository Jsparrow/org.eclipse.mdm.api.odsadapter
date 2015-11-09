package org.asam.ods;

/**
 * Generated from IDL alias "NameValueSequence".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class NameValueSequenceHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.asam.ods.NameValue[] value;

	public NameValueSequenceHolder ()
	{
	}
	public NameValueSequenceHolder (final org.asam.ods.NameValue[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return NameValueSequenceHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = NameValueSequenceHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		NameValueSequenceHelper.write (out,value);
	}
}
