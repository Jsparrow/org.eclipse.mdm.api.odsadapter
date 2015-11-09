package org.asam.ods;

/**
 * Generated from IDL alias "SS_SHORT".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class SS_SHORTHolder
	implements org.omg.CORBA.portable.Streamable
{
	public short[][] value;

	public SS_SHORTHolder ()
	{
	}
	public SS_SHORTHolder (final short[][] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return SS_SHORTHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = SS_SHORTHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		SS_SHORTHelper.write (out,value);
	}
}
