package org.asam.ods;
/**
 * Generated from IDL enum "SelOpcode".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class SelOpcodeHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_enum_tc(org.asam.ods.SelOpcodeHelper.id(),"SelOpcode",new String[]{"EQ","NEQ","LT","GT","LTE","GTE","INSET","NOTINSET","LIKE","CI_EQ","CI_NEQ","CI_LT","CI_GT","CI_LTE","CI_GTE","CI_INSET","CI_NOTINSET","CI_LIKE","IS_NULL","IS_NOT_NULL","NOTLIKE","CI_NOTLIKE","BETWEEN"});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.asam.ods.SelOpcode s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.asam.ods.SelOpcode extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/asam/ods/SelOpcode:1.0";
	}
	public static SelOpcode read (final org.omg.CORBA.portable.InputStream in)
	{
		return SelOpcode.from_int(in.read_long());
	}

	public static void write (final org.omg.CORBA.portable.OutputStream out, final SelOpcode s)
	{
		out.write_long(s.value());
	}
}
