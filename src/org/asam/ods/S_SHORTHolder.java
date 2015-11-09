package org.asam.ods;

/**
 * Generated from IDL alias "S_SHORT".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class S_SHORTHolder
	implements org.omg.CORBA.portable.Streamable
{
	public short[] value;

	public S_SHORTHolder ()
	{
	}
	public S_SHORTHolder (final short[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return S_SHORTHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = S_SHORTHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		S_SHORTHelper.write (out,value);
	}
}
