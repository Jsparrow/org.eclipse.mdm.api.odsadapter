package org.asam.ods;

/**
 * Generated from IDL alias "SMatLinkSequence".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class SMatLinkSequenceHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.asam.ods.SMatLink[] value;

	public SMatLinkSequenceHolder ()
	{
	}
	public SMatLinkSequenceHolder (final org.asam.ods.SMatLink[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return SMatLinkSequenceHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = SMatLinkSequenceHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		SMatLinkSequenceHelper.write (out,value);
	}
}
