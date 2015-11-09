package org.asam.ods;

/**
 * Generated from IDL alias "T_BOOLEAN".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class T_BOOLEANHelper
{
	private static org.omg.CORBA.TypeCode _type = null;

	public static void insert (org.omg.CORBA.Any any, boolean s)
	{
		any.insert_boolean(s);
	}

	public static boolean extract (final org.omg.CORBA.Any any)
	{
		return any.extract_boolean();
	}

	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_alias_tc(org.asam.ods.T_BOOLEANHelper.id(), "T_BOOLEAN",org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.from_int(8)));
		}
		return _type;
	}

	public static String id()
	{
		return "IDL:org/asam/ods/T_BOOLEAN:1.0";
	}
	public static boolean read (final org.omg.CORBA.portable.InputStream _in)
	{
		boolean _result;
		_result=_in.read_boolean();
		return _result;
	}

	public static void write (final org.omg.CORBA.portable.OutputStream _out, boolean _s)
	{
		_out.write_boolean(_s);
	}
}
