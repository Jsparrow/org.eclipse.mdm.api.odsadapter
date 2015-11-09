package org.asam.ods;


/**
 * Generated from IDL struct "T_COMPLEX".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class T_COMPLEXHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.asam.ods.T_COMPLEXHelper.id(),"T_COMPLEX",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("r", org.asam.ods.T_FLOATHelper.type(), null),new org.omg.CORBA.StructMember("i", org.asam.ods.T_FLOATHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.asam.ods.T_COMPLEX s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.asam.ods.T_COMPLEX extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/asam/ods/T_COMPLEX:1.0";
	}
	public static org.asam.ods.T_COMPLEX read (final org.omg.CORBA.portable.InputStream in)
	{
		org.asam.ods.T_COMPLEX result = new org.asam.ods.T_COMPLEX();
		result.r=in.read_float();
		result.i=in.read_float();
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.asam.ods.T_COMPLEX s)
	{
		out.write_float(s.r);
		out.write_float(s.i);
	}
}
