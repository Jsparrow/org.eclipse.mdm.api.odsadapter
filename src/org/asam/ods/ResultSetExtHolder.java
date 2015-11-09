package org.asam.ods;

/**
 * Generated from IDL struct "ResultSetExt".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class ResultSetExtHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.asam.ods.ResultSetExt value;

	public ResultSetExtHolder ()
	{
	}
	public ResultSetExtHolder(final org.asam.ods.ResultSetExt initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.asam.ods.ResultSetExtHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.asam.ods.ResultSetExtHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.asam.ods.ResultSetExtHelper.write(_out, value);
	}
}
