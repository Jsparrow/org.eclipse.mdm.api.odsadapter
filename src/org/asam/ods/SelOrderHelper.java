package org.asam.ods;


/**
 * Generated from IDL struct "SelOrder".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class SelOrderHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.asam.ods.SelOrderHelper.id(),"SelOrder",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("attr", org.asam.ods.AIDNameHelper.type(), null),new org.omg.CORBA.StructMember("ascending", org.asam.ods.T_BOOLEANHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.asam.ods.SelOrder s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.asam.ods.SelOrder extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/asam/ods/SelOrder:1.0";
	}
	public static org.asam.ods.SelOrder read (final org.omg.CORBA.portable.InputStream in)
	{
		org.asam.ods.SelOrder result = new org.asam.ods.SelOrder();
		result.attr=org.asam.ods.AIDNameHelper.read(in);
		result.ascending=in.read_boolean();
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.asam.ods.SelOrder s)
	{
		org.asam.ods.AIDNameHelper.write(out,s.attr);
		out.write_boolean(s.ascending);
	}
}
