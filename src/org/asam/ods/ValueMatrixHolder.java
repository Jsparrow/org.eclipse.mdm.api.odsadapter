package org.asam.ods;

/**
 * Generated from IDL interface "ValueMatrix".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class ValueMatrixHolder	implements org.omg.CORBA.portable.Streamable{
	 public ValueMatrix value;
	public ValueMatrixHolder()
	{
	}
	public ValueMatrixHolder (final ValueMatrix initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return ValueMatrixHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = ValueMatrixHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		ValueMatrixHelper.write (_out,value);
	}
}
