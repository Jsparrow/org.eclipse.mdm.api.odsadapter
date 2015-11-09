package org.asam.ods;

/**
 * Generated from IDL alias "SS_DOUBLE".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class SS_DOUBLEHolder
	implements org.omg.CORBA.portable.Streamable
{
	public double[][] value;

	public SS_DOUBLEHolder ()
	{
	}
	public SS_DOUBLEHolder (final double[][] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return SS_DOUBLEHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = SS_DOUBLEHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		SS_DOUBLEHelper.write (out,value);
	}
}
