package org.asam.ods;

/**
 * Generated from IDL alias "SS_LONGLONG".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class SS_LONGLONGHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.asam.ods.T_LONGLONG[][] value;

	public SS_LONGLONGHolder ()
	{
	}
	public SS_LONGLONGHolder (final org.asam.ods.T_LONGLONG[][] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return SS_LONGLONGHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = SS_LONGLONGHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		SS_LONGLONGHelper.write (out,value);
	}
}
