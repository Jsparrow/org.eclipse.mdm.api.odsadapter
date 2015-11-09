package org.asam.ods;

/**
 * Generated from IDL alias "SS_LONG".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class SS_LONGHolder
	implements org.omg.CORBA.portable.Streamable
{
	public int[][] value;

	public SS_LONGHolder ()
	{
	}
	public SS_LONGHolder (final int[][] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return SS_LONGHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = SS_LONGHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		SS_LONGHelper.write (out,value);
	}
}
