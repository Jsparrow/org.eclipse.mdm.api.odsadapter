package org.asam.ods;

/**
 * Generated from IDL alias "ApplAttrSequence".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class ApplAttrSequenceHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.asam.ods.ApplAttr[] value;

	public ApplAttrSequenceHolder ()
	{
	}
	public ApplAttrSequenceHolder (final org.asam.ods.ApplAttr[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return ApplAttrSequenceHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = ApplAttrSequenceHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		ApplAttrSequenceHelper.write (out,value);
	}
}
