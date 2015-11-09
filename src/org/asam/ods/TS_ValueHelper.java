package org.asam.ods;


/**
 * Generated from IDL struct "TS_Value".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class TS_ValueHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.asam.ods.TS_ValueHelper.id(),"TS_Value",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("u", org.asam.ods.TS_UnionHelper.type(), null),new org.omg.CORBA.StructMember("flag", org.asam.ods.T_SHORTHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.asam.ods.TS_Value s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.asam.ods.TS_Value extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/asam/ods/TS_Value:1.0";
	}
	public static org.asam.ods.TS_Value read (final org.omg.CORBA.portable.InputStream in)
	{
		org.asam.ods.TS_Value result = new org.asam.ods.TS_Value();
		result.u=org.asam.ods.TS_UnionHelper.read(in);
		result.flag=in.read_short();
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.asam.ods.TS_Value s)
	{
		org.asam.ods.TS_UnionHelper.write(out,s.u);
		out.write_short(s.flag);
	}
}
