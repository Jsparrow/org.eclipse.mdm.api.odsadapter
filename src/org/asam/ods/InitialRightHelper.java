package org.asam.ods;


/**
 * Generated from IDL struct "InitialRight".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class InitialRightHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.asam.ods.InitialRightHelper.id(),"InitialRight",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("rights", org.asam.ods.T_LONGHelper.type(), null),new org.omg.CORBA.StructMember("usergroupId", org.asam.ods.T_LONGLONGHelper.type(), null),new org.omg.CORBA.StructMember("refAid", org.asam.ods.T_LONGLONGHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.asam.ods.InitialRight s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.asam.ods.InitialRight extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/asam/ods/InitialRight:1.0";
	}
	public static org.asam.ods.InitialRight read (final org.omg.CORBA.portable.InputStream in)
	{
		org.asam.ods.InitialRight result = new org.asam.ods.InitialRight();
		result.rights=in.read_long();
		result.usergroupId=org.asam.ods.T_LONGLONGHelper.read(in);
		result.refAid=org.asam.ods.T_LONGLONGHelper.read(in);
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.asam.ods.InitialRight s)
	{
		out.write_long(s.rights);
		org.asam.ods.T_LONGLONGHelper.write(out,s.usergroupId);
		org.asam.ods.T_LONGLONGHelper.write(out,s.refAid);
	}
}
