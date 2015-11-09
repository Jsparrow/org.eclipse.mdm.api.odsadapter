package org.asam.ods;

/**
 * Generated from IDL alias "S_BLOB".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class S_BLOBHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.asam.ods.Blob[] value;

	public S_BLOBHolder ()
	{
	}
	public S_BLOBHolder (final org.asam.ods.Blob[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return S_BLOBHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = S_BLOBHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		S_BLOBHelper.write (out,value);
	}
}
