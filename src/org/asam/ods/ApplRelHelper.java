package org.asam.ods;


/**
 * Generated from IDL struct "ApplRel".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class ApplRelHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.asam.ods.ApplRelHelper.id(),"ApplRel",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("elem1", org.asam.ods.T_LONGLONGHelper.type(), null),new org.omg.CORBA.StructMember("elem2", org.asam.ods.T_LONGLONGHelper.type(), null),new org.omg.CORBA.StructMember("arName", org.asam.ods.NameHelper.type(), null),new org.omg.CORBA.StructMember("invName", org.asam.ods.NameHelper.type(), null),new org.omg.CORBA.StructMember("brName", org.asam.ods.NameHelper.type(), null),new org.omg.CORBA.StructMember("invBrName", org.asam.ods.NameHelper.type(), null),new org.omg.CORBA.StructMember("arRelationType", org.asam.ods.RelationTypeHelper.type(), null),new org.omg.CORBA.StructMember("arRelationRange", org.asam.ods.RelationRangeHelper.type(), null),new org.omg.CORBA.StructMember("invRelationRange", org.asam.ods.RelationRangeHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.asam.ods.ApplRel s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.asam.ods.ApplRel extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/asam/ods/ApplRel:1.0";
	}
	public static org.asam.ods.ApplRel read (final org.omg.CORBA.portable.InputStream in)
	{
		org.asam.ods.ApplRel result = new org.asam.ods.ApplRel();
		result.elem1=org.asam.ods.T_LONGLONGHelper.read(in);
		result.elem2=org.asam.ods.T_LONGLONGHelper.read(in);
		result.arName=in.read_string();
		result.invName=in.read_string();
		result.brName=in.read_string();
		result.invBrName=in.read_string();
		result.arRelationType=org.asam.ods.RelationTypeHelper.read(in);
		result.arRelationRange=org.asam.ods.RelationRangeHelper.read(in);
		result.invRelationRange=org.asam.ods.RelationRangeHelper.read(in);
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.asam.ods.ApplRel s)
	{
		org.asam.ods.T_LONGLONGHelper.write(out,s.elem1);
		org.asam.ods.T_LONGLONGHelper.write(out,s.elem2);
		out.write_string(s.arName);
		out.write_string(s.invName);
		out.write_string(s.brName);
		out.write_string(s.invBrName);
		org.asam.ods.RelationTypeHelper.write(out,s.arRelationType);
		org.asam.ods.RelationRangeHelper.write(out,s.arRelationRange);
		org.asam.ods.RelationRangeHelper.write(out,s.invRelationRange);
	}
}
