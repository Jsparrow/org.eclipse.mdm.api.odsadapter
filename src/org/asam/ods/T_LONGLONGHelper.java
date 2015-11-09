package org.asam.ods;


/**
 * Generated from IDL struct "T_LONGLONG".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class T_LONGLONGHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.asam.ods.T_LONGLONGHelper.id(),"T_LONGLONG",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("high", org.asam.ods.T_LONGHelper.type(), null),new org.omg.CORBA.StructMember("low", org.asam.ods.T_LONGHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.asam.ods.T_LONGLONG s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.asam.ods.T_LONGLONG extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/asam/ods/T_LONGLONG:1.0";
	}
	public static org.asam.ods.T_LONGLONG read (final org.omg.CORBA.portable.InputStream in)
	{
		org.asam.ods.T_LONGLONG result = new org.asam.ods.T_LONGLONG();
		result.high=in.read_long();
		result.low=in.read_long();
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.asam.ods.T_LONGLONG s)
	{
		out.write_long(s.high);
		out.write_long(s.low);
	}
}
