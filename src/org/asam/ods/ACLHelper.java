package org.asam.ods;


/**
 * Generated from IDL struct "ACL".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class ACLHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.asam.ods.ACLHelper.id(),"ACL",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("usergroupId", org.asam.ods.T_LONGLONGHelper.type(), null),new org.omg.CORBA.StructMember("rights", org.asam.ods.T_LONGHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.asam.ods.ACL s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.asam.ods.ACL extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/asam/ods/ACL:1.0";
	}
	public static org.asam.ods.ACL read (final org.omg.CORBA.portable.InputStream in)
	{
		org.asam.ods.ACL result = new org.asam.ods.ACL();
		result.usergroupId=org.asam.ods.T_LONGLONGHelper.read(in);
		result.rights=in.read_long();
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.asam.ods.ACL s)
	{
		org.asam.ods.T_LONGLONGHelper.write(out,s.usergroupId);
		out.write_long(s.rights);
	}
}
