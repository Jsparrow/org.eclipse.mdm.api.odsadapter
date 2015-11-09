package org.asam.ods;


/**
 * Generated from IDL struct "JoinDef".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class JoinDefHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.asam.ods.JoinDefHelper.id(),"JoinDef",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("fromAID", org.asam.ods.T_LONGLONGHelper.type(), null),new org.omg.CORBA.StructMember("toAID", org.asam.ods.T_LONGLONGHelper.type(), null),new org.omg.CORBA.StructMember("refName", org.asam.ods.NameHelper.type(), null),new org.omg.CORBA.StructMember("joiningType", org.asam.ods.JoinTypeHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.asam.ods.JoinDef s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.asam.ods.JoinDef extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/asam/ods/JoinDef:1.0";
	}
	public static org.asam.ods.JoinDef read (final org.omg.CORBA.portable.InputStream in)
	{
		org.asam.ods.JoinDef result = new org.asam.ods.JoinDef();
		result.fromAID=org.asam.ods.T_LONGLONGHelper.read(in);
		result.toAID=org.asam.ods.T_LONGLONGHelper.read(in);
		result.refName=in.read_string();
		result.joiningType=org.asam.ods.JoinTypeHelper.read(in);
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.asam.ods.JoinDef s)
	{
		org.asam.ods.T_LONGLONGHelper.write(out,s.fromAID);
		org.asam.ods.T_LONGLONGHelper.write(out,s.toAID);
		out.write_string(s.refName);
		org.asam.ods.JoinTypeHelper.write(out,s.joiningType);
	}
}
