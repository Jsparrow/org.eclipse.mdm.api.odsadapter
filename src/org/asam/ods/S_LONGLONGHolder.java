package org.asam.ods;

/**
 * Generated from IDL alias "S_LONGLONG".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class S_LONGLONGHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.asam.ods.T_LONGLONG[] value;

	public S_LONGLONGHolder ()
	{
	}
	public S_LONGLONGHolder (final org.asam.ods.T_LONGLONG[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return S_LONGLONGHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = S_LONGLONGHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		S_LONGLONGHelper.write (out,value);
	}
}
