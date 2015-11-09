package org.asam.ods;

/**
 * Generated from IDL alias "SS_BYTE".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class SS_BYTEHolder
	implements org.omg.CORBA.portable.Streamable
{
	public byte[][] value;

	public SS_BYTEHolder ()
	{
	}
	public SS_BYTEHolder (final byte[][] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return SS_BYTEHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = SS_BYTEHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		SS_BYTEHelper.write (out,value);
	}
}
