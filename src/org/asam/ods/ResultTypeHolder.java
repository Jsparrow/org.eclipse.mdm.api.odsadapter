package org.asam.ods;

/**
 * Generated from IDL interface "ResultType".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class ResultTypeHolder	implements org.omg.CORBA.portable.Streamable{
	 public ResultType value;
	public ResultTypeHolder()
	{
	}
	public ResultTypeHolder (final ResultType initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return ResultTypeHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = ResultTypeHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		ResultTypeHelper.write (_out,value);
	}
}
