package org.asam.ods;

/**
 * Generated from IDL alias "SS_FLOAT".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class SS_FLOATHolder
	implements org.omg.CORBA.portable.Streamable
{
	public float[][] value;

	public SS_FLOATHolder ()
	{
	}
	public SS_FLOATHolder (final float[][] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return SS_FLOATHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = SS_FLOATHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		SS_FLOATHelper.write (out,value);
	}
}
