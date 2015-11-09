package org.asam.ods;
/**
 * Generated from IDL enum "SelOperator".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class SelOperatorHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_enum_tc(org.asam.ods.SelOperatorHelper.id(),"SelOperator",new String[]{"AND","OR","NOT","OPEN","CLOSE"});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.asam.ods.SelOperator s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.asam.ods.SelOperator extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/asam/ods/SelOperator:1.0";
	}
	public static SelOperator read (final org.omg.CORBA.portable.InputStream in)
	{
		return SelOperator.from_int(in.read_long());
	}

	public static void write (final org.omg.CORBA.portable.OutputStream out, final SelOperator s)
	{
		out.write_long(s.value());
	}
}
