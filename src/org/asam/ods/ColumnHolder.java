package org.asam.ods;

/**
 * Generated from IDL interface "Column".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class ColumnHolder	implements org.omg.CORBA.portable.Streamable{
	 public Column value;
	public ColumnHolder()
	{
	}
	public ColumnHolder (final Column initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return ColumnHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = ColumnHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		ColumnHelper.write (_out,value);
	}
}
