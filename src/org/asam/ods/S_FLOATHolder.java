package org.asam.ods;

/**
 * Generated from IDL alias "S_FLOAT".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class S_FLOATHolder
	implements org.omg.CORBA.portable.Streamable
{
	public float[] value;

	public S_FLOATHolder ()
	{
	}
	public S_FLOATHolder (final float[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return S_FLOATHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = S_FLOATHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		S_FLOATHelper.write (out,value);
	}
}
