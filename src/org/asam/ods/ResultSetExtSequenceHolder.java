package org.asam.ods;

/**
 * Generated from IDL alias "ResultSetExtSequence".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class ResultSetExtSequenceHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.asam.ods.ResultSetExt[] value;

	public ResultSetExtSequenceHolder ()
	{
	}
	public ResultSetExtSequenceHolder (final org.asam.ods.ResultSetExt[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return ResultSetExtSequenceHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = ResultSetExtSequenceHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		ResultSetExtSequenceHelper.write (out,value);
	}
}
