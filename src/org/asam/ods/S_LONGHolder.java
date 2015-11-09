package org.asam.ods;

/**
 * Generated from IDL alias "S_LONG".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class S_LONGHolder
	implements org.omg.CORBA.portable.Streamable
{
	public int[] value;

	public S_LONGHolder ()
	{
	}
	public S_LONGHolder (final int[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return S_LONGHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = S_LONGHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		S_LONGHelper.write (out,value);
	}
}
