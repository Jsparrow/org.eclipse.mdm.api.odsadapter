package org.asam.ods;


/**
 * Generated from IDL struct "ElemResultSet".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class ElemResultSetHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.asam.ods.ElemResultSetHelper.id(),"ElemResultSet",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("aid", org.asam.ods.T_LONGLONGHelper.type(), null),new org.omg.CORBA.StructMember("attrValues", org.asam.ods.AttrResultSetSequenceHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.asam.ods.ElemResultSet s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.asam.ods.ElemResultSet extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/asam/ods/ElemResultSet:1.0";
	}
	public static org.asam.ods.ElemResultSet read (final org.omg.CORBA.portable.InputStream in)
	{
		org.asam.ods.ElemResultSet result = new org.asam.ods.ElemResultSet();
		result.aid=org.asam.ods.T_LONGLONGHelper.read(in);
		result.attrValues = org.asam.ods.AttrResultSetSequenceHelper.read(in);
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.asam.ods.ElemResultSet s)
	{
		org.asam.ods.T_LONGLONGHelper.write(out,s.aid);
		org.asam.ods.AttrResultSetSequenceHelper.write(out,s.attrValues);
	}
}
