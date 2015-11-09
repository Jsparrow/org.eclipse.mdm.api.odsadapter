package org.asam.ods;

/**
 * Generated from IDL alias "NameValueUnitSequence".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class NameValueUnitSequenceHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.asam.ods.NameValueUnit[] value;

	public NameValueUnitSequenceHolder ()
	{
	}
	public NameValueUnitSequenceHolder (final org.asam.ods.NameValueUnit[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return NameValueUnitSequenceHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = NameValueUnitSequenceHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		NameValueUnitSequenceHelper.write (out,value);
	}
}
