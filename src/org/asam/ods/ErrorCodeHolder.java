package org.asam.ods;
/**
 * Generated from IDL enum "ErrorCode".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class ErrorCodeHolder
	implements org.omg.CORBA.portable.Streamable
{
	public ErrorCode value;

	public ErrorCodeHolder ()
	{
	}
	public ErrorCodeHolder (final ErrorCode initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return ErrorCodeHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = ErrorCodeHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		ErrorCodeHelper.write (out,value);
	}
}
