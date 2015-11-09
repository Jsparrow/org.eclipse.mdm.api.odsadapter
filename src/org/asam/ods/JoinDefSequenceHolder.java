package org.asam.ods;

/**
 * Generated from IDL alias "JoinDefSequence".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class JoinDefSequenceHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.asam.ods.JoinDef[] value;

	public JoinDefSequenceHolder ()
	{
	}
	public JoinDefSequenceHolder (final org.asam.ods.JoinDef[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return JoinDefSequenceHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = JoinDefSequenceHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		JoinDefSequenceHelper.write (out,value);
	}
}
