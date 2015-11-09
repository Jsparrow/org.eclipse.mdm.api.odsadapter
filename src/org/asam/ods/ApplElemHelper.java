package org.asam.ods;


/**
 * Generated from IDL struct "ApplElem".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class ApplElemHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.asam.ods.ApplElemHelper.id(),"ApplElem",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("aid", org.asam.ods.T_LONGLONGHelper.type(), null),new org.omg.CORBA.StructMember("beName", org.asam.ods.NameHelper.type(), null),new org.omg.CORBA.StructMember("aeName", org.asam.ods.NameHelper.type(), null),new org.omg.CORBA.StructMember("attributes", org.asam.ods.ApplAttrSequenceHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.asam.ods.ApplElem s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.asam.ods.ApplElem extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/asam/ods/ApplElem:1.0";
	}
	public static org.asam.ods.ApplElem read (final org.omg.CORBA.portable.InputStream in)
	{
		org.asam.ods.ApplElem result = new org.asam.ods.ApplElem();
		result.aid=org.asam.ods.T_LONGLONGHelper.read(in);
		result.beName=in.read_string();
		result.aeName=in.read_string();
		result.attributes = org.asam.ods.ApplAttrSequenceHelper.read(in);
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.asam.ods.ApplElem s)
	{
		org.asam.ods.T_LONGLONGHelper.write(out,s.aid);
		out.write_string(s.beName);
		out.write_string(s.aeName);
		org.asam.ods.ApplAttrSequenceHelper.write(out,s.attributes);
	}
}
