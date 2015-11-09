package org.asam.ods;

/**
 * Generated from IDL interface "QueryConstants".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class QueryConstantsHolder	implements org.omg.CORBA.portable.Streamable{
	 public QueryConstants value;
	public QueryConstantsHolder()
	{
	}
	public QueryConstantsHolder (final QueryConstants initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return QueryConstantsHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = QueryConstantsHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		QueryConstantsHelper.write (_out,value);
	}
}
