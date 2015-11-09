package org.asam.ods;

/**
 * Generated from IDL alias "T_BYTE".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class T_BYTEHelper
{
	private static org.omg.CORBA.TypeCode _type = null;

	public static void insert (org.omg.CORBA.Any any, byte s)
	{
		any.insert_octet(s);
	}

	public static byte extract (final org.omg.CORBA.Any any)
	{
		return any.extract_octet();
	}

	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_alias_tc(org.asam.ods.T_BYTEHelper.id(), "T_BYTE",org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.from_int(10)));
		}
		return _type;
	}

	public static String id()
	{
		return "IDL:org/asam/ods/T_BYTE:1.0";
	}
	public static byte read (final org.omg.CORBA.portable.InputStream _in)
	{
		byte _result;
		_result=_in.read_octet();
		return _result;
	}

	public static void write (final org.omg.CORBA.portable.OutputStream _out, byte _s)
	{
		_out.write_octet(_s);
	}
}
