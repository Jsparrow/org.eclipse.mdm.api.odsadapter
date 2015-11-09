package org.asam.ods;
/**
 * Generated from IDL enum "DataType".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class DataTypeHolder
	implements org.omg.CORBA.portable.Streamable
{
	public DataType value;

	public DataTypeHolder ()
	{
	}
	public DataTypeHolder (final DataType initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return DataTypeHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = DataTypeHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		DataTypeHelper.write (out,value);
	}
}
