package org.asam.ods;


/**
 * Generated from IDL struct "ApplAttr".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class ApplAttrHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.asam.ods.ApplAttrHelper.id(),"ApplAttr",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("aaName", org.asam.ods.NameHelper.type(), null),new org.omg.CORBA.StructMember("baName", org.asam.ods.NameHelper.type(), null),new org.omg.CORBA.StructMember("dType", org.asam.ods.DataTypeHelper.type(), null),new org.omg.CORBA.StructMember("length", org.asam.ods.T_LONGHelper.type(), null),new org.omg.CORBA.StructMember("isObligatory", org.asam.ods.T_BOOLEANHelper.type(), null),new org.omg.CORBA.StructMember("isUnique", org.asam.ods.T_BOOLEANHelper.type(), null),new org.omg.CORBA.StructMember("unitId", org.asam.ods.T_LONGLONGHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.asam.ods.ApplAttr s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.asam.ods.ApplAttr extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/asam/ods/ApplAttr:1.0";
	}
	public static org.asam.ods.ApplAttr read (final org.omg.CORBA.portable.InputStream in)
	{
		org.asam.ods.ApplAttr result = new org.asam.ods.ApplAttr();
		result.aaName=in.read_string();
		result.baName=in.read_string();
		result.dType=org.asam.ods.DataTypeHelper.read(in);
		result.length=in.read_long();
		result.isObligatory=in.read_boolean();
		result.isUnique=in.read_boolean();
		result.unitId=org.asam.ods.T_LONGLONGHelper.read(in);
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.asam.ods.ApplAttr s)
	{
		out.write_string(s.aaName);
		out.write_string(s.baName);
		org.asam.ods.DataTypeHelper.write(out,s.dType);
		out.write_long(s.length);
		out.write_boolean(s.isObligatory);
		out.write_boolean(s.isUnique);
		org.asam.ods.T_LONGLONGHelper.write(out,s.unitId);
	}
}
