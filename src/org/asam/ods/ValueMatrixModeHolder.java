package org.asam.ods;
/**
 * Generated from IDL enum "ValueMatrixMode".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class ValueMatrixModeHolder
	implements org.omg.CORBA.portable.Streamable
{
	public ValueMatrixMode value;

	public ValueMatrixModeHolder ()
	{
	}
	public ValueMatrixModeHolder (final ValueMatrixMode initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return ValueMatrixModeHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = ValueMatrixModeHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		ValueMatrixModeHelper.write (out,value);
	}
}
