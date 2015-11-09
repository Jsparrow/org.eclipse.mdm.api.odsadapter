package org.asam.ods;

/**
 * Generated from IDL alias "S_BYTESTR".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class S_BYTESTRHolder
	implements org.omg.CORBA.portable.Streamable
{
	public byte[][] value;

	public S_BYTESTRHolder ()
	{
	}
	public S_BYTESTRHolder (final byte[][] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return S_BYTESTRHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = S_BYTESTRHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		S_BYTESTRHelper.write (out,value);
	}
}
