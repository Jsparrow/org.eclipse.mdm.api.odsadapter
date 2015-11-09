package org.asam.ods;


/**
 * Generated from IDL struct "ElemId".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class ElemIdHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.asam.ods.ElemIdHelper.id(),"ElemId",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("aid", org.asam.ods.T_LONGLONGHelper.type(), null),new org.omg.CORBA.StructMember("iid", org.asam.ods.T_LONGLONGHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.asam.ods.ElemId s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.asam.ods.ElemId extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/asam/ods/ElemId:1.0";
	}
	public static org.asam.ods.ElemId read (final org.omg.CORBA.portable.InputStream in)
	{
		org.asam.ods.ElemId result = new org.asam.ods.ElemId();
		result.aid=org.asam.ods.T_LONGLONGHelper.read(in);
		result.iid=org.asam.ods.T_LONGLONGHelper.read(in);
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.asam.ods.ElemId s)
	{
		org.asam.ods.T_LONGLONGHelper.write(out,s.aid);
		org.asam.ods.T_LONGLONGHelper.write(out,s.iid);
	}
}
