package org.asam.ods;


/**
 * Generated from IDL struct "T_ExternalReference".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class T_ExternalReferenceHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.asam.ods.T_ExternalReferenceHelper.id(),"T_ExternalReference",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("description", org.asam.ods.T_STRINGHelper.type(), null),new org.omg.CORBA.StructMember("mimeType", org.asam.ods.T_STRINGHelper.type(), null),new org.omg.CORBA.StructMember("location", org.asam.ods.T_STRINGHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.asam.ods.T_ExternalReference s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.asam.ods.T_ExternalReference extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/asam/ods/T_ExternalReference:1.0";
	}
	public static org.asam.ods.T_ExternalReference read (final org.omg.CORBA.portable.InputStream in)
	{
		org.asam.ods.T_ExternalReference result = new org.asam.ods.T_ExternalReference();
		result.description=in.read_string();
		result.mimeType=in.read_string();
		result.location=in.read_string();
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.asam.ods.T_ExternalReference s)
	{
		out.write_string(s.description);
		out.write_string(s.mimeType);
		out.write_string(s.location);
	}
}
