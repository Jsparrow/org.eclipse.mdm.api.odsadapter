package org.asam.ods;

/**
 * Generated from IDL alias "T_FLOAT".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class T_FLOATHelper
{
	private static org.omg.CORBA.TypeCode _type = null;

	public static void insert (org.omg.CORBA.Any any, float s)
	{
		any.insert_float(s);
	}

	public static float extract (final org.omg.CORBA.Any any)
	{
		return any.extract_float();
	}

	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_alias_tc(org.asam.ods.T_FLOATHelper.id(), "T_FLOAT",org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.from_int(6)));
		}
		return _type;
	}

	public static String id()
	{
		return "IDL:org/asam/ods/T_FLOAT:1.0";
	}
	public static float read (final org.omg.CORBA.portable.InputStream _in)
	{
		float _result;
		_result=_in.read_float();
		return _result;
	}

	public static void write (final org.omg.CORBA.portable.OutputStream _out, float _s)
	{
		_out.write_float(_s);
	}
}
