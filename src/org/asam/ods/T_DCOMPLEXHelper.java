package org.asam.ods;


/**
 * Generated from IDL struct "T_DCOMPLEX".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class T_DCOMPLEXHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.asam.ods.T_DCOMPLEXHelper.id(),"T_DCOMPLEX",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("r", org.asam.ods.T_DOUBLEHelper.type(), null),new org.omg.CORBA.StructMember("i", org.asam.ods.T_DOUBLEHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.asam.ods.T_DCOMPLEX s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.asam.ods.T_DCOMPLEX extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/asam/ods/T_DCOMPLEX:1.0";
	}
	public static org.asam.ods.T_DCOMPLEX read (final org.omg.CORBA.portable.InputStream in)
	{
		org.asam.ods.T_DCOMPLEX result = new org.asam.ods.T_DCOMPLEX();
		result.r=in.read_double();
		result.i=in.read_double();
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.asam.ods.T_DCOMPLEX s)
	{
		out.write_double(s.r);
		out.write_double(s.i);
	}
}
