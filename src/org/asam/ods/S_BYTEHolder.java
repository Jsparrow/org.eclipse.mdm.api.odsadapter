package org.asam.ods;

/**
 * Generated from IDL alias "S_BYTE".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class S_BYTEHolder
	implements org.omg.CORBA.portable.Streamable
{
	public byte[] value;

	public S_BYTEHolder ()
	{
	}
	public S_BYTEHolder (final byte[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return S_BYTEHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = S_BYTEHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		S_BYTEHelper.write (out,value);
	}
}
