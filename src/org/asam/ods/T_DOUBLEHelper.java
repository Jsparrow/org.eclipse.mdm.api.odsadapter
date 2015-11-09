package org.asam.ods;

/**
 * Generated from IDL alias "T_DOUBLE".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class T_DOUBLEHelper
{
	private static org.omg.CORBA.TypeCode _type = null;

	public static void insert (org.omg.CORBA.Any any, double s)
	{
		any.insert_double(s);
	}

	public static double extract (final org.omg.CORBA.Any any)
	{
		return any.extract_double();
	}

	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_alias_tc(org.asam.ods.T_DOUBLEHelper.id(), "T_DOUBLE",org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.from_int(7)));
		}
		return _type;
	}

	public static String id()
	{
		return "IDL:org/asam/ods/T_DOUBLE:1.0";
	}
	public static double read (final org.omg.CORBA.portable.InputStream _in)
	{
		double _result;
		_result=_in.read_double();
		return _result;
	}

	public static void write (final org.omg.CORBA.portable.OutputStream _out, double _s)
	{
		_out.write_double(_s);
	}
}
