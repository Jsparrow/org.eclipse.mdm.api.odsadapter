package org.asam.ods;
/**
 * Generated from IDL enum "QueryStatus".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class QueryStatusHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_enum_tc(org.asam.ods.QueryStatusHelper.id(),"QueryStatus",new String[]{"COMPLETE","INCOMPLETE"});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.asam.ods.QueryStatus s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.asam.ods.QueryStatus extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/asam/ods/QueryStatus:1.0";
	}
	public static QueryStatus read (final org.omg.CORBA.portable.InputStream in)
	{
		return QueryStatus.from_int(in.read_long());
	}

	public static void write (final org.omg.CORBA.portable.OutputStream out, final QueryStatus s)
	{
		out.write_long(s.value());
	}
}
