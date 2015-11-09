package org.asam.ods;

/**
 * Generated from IDL interface "SubMatrix".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class SubMatrixHolder	implements org.omg.CORBA.portable.Streamable{
	 public SubMatrix value;
	public SubMatrixHolder()
	{
	}
	public SubMatrixHolder (final SubMatrix initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return SubMatrixHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = SubMatrixHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		SubMatrixHelper.write (_out,value);
	}
}
