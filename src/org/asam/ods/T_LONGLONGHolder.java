package org.asam.ods;

/**
 * Generated from IDL struct "T_LONGLONG".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class T_LONGLONGHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.asam.ods.T_LONGLONG value;

	public T_LONGLONGHolder ()
	{
	}
	public T_LONGLONGHolder(final org.asam.ods.T_LONGLONG initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.asam.ods.T_LONGLONGHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.asam.ods.T_LONGLONGHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.asam.ods.T_LONGLONGHelper.write(_out, value);
	}
}
