package org.asam.ods;
/**
 * Generated from IDL enum "QueryStatus".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class QueryStatusHolder
	implements org.omg.CORBA.portable.Streamable
{
	public QueryStatus value;

	public QueryStatusHolder ()
	{
	}
	public QueryStatusHolder (final QueryStatus initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return QueryStatusHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = QueryStatusHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		QueryStatusHelper.write (out,value);
	}
}
