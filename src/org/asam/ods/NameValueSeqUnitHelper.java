package org.asam.ods;


/**
 * Generated from IDL struct "NameValueSeqUnit".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class NameValueSeqUnitHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.asam.ods.NameValueSeqUnitHelper.id(),"NameValueSeqUnit",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("valName", org.asam.ods.NameHelper.type(), null),new org.omg.CORBA.StructMember("value", org.asam.ods.TS_ValueSeqHelper.type(), null),new org.omg.CORBA.StructMember("unit", org.asam.ods.T_STRINGHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.asam.ods.NameValueSeqUnit s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.asam.ods.NameValueSeqUnit extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/asam/ods/NameValueSeqUnit:1.0";
	}
	public static org.asam.ods.NameValueSeqUnit read (final org.omg.CORBA.portable.InputStream in)
	{
		org.asam.ods.NameValueSeqUnit result = new org.asam.ods.NameValueSeqUnit();
		result.valName=in.read_string();
		result.value=org.asam.ods.TS_ValueSeqHelper.read(in);
		result.unit=in.read_string();
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.asam.ods.NameValueSeqUnit s)
	{
		out.write_string(s.valName);
		org.asam.ods.TS_ValueSeqHelper.write(out,s.value);
		out.write_string(s.unit);
	}
}
