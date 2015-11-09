package org.asam.ods;

/**
 * Generated from IDL alias "S_DOUBLE".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class S_DOUBLEHolder
	implements org.omg.CORBA.portable.Streamable
{
	public double[] value;

	public S_DOUBLEHolder ()
	{
	}
	public S_DOUBLEHolder (final double[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return S_DOUBLEHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = S_DOUBLEHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		S_DOUBLEHelper.write (out,value);
	}
}
