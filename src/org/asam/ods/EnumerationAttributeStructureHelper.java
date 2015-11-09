package org.asam.ods;


/**
 * Generated from IDL struct "EnumerationAttributeStructure".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class EnumerationAttributeStructureHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.asam.ods.EnumerationAttributeStructureHelper.id(),"EnumerationAttributeStructure",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("aid", org.asam.ods.T_LONGLONGHelper.type(), null),new org.omg.CORBA.StructMember("aaName", org.asam.ods.T_STRINGHelper.type(), null),new org.omg.CORBA.StructMember("enumName", org.asam.ods.T_STRINGHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.asam.ods.EnumerationAttributeStructure s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.asam.ods.EnumerationAttributeStructure extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/asam/ods/EnumerationAttributeStructure:1.0";
	}
	public static org.asam.ods.EnumerationAttributeStructure read (final org.omg.CORBA.portable.InputStream in)
	{
		org.asam.ods.EnumerationAttributeStructure result = new org.asam.ods.EnumerationAttributeStructure();
		result.aid=org.asam.ods.T_LONGLONGHelper.read(in);
		result.aaName=in.read_string();
		result.enumName=in.read_string();
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.asam.ods.EnumerationAttributeStructure s)
	{
		org.asam.ods.T_LONGLONGHelper.write(out,s.aid);
		out.write_string(s.aaName);
		out.write_string(s.enumName);
	}
}
