package org.asam.ods;


/**
 * Generated from IDL struct "SelValueExt".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class SelValueExtHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.asam.ods.SelValueExtHelper.id(),"SelValueExt",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("attr", org.asam.ods.AIDNameUnitIdHelper.type(), null),new org.omg.CORBA.StructMember("oper", org.asam.ods.SelOpcodeHelper.type(), null),new org.omg.CORBA.StructMember("value", org.asam.ods.TS_ValueHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.asam.ods.SelValueExt s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.asam.ods.SelValueExt extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/asam/ods/SelValueExt:1.0";
	}
	public static org.asam.ods.SelValueExt read (final org.omg.CORBA.portable.InputStream in)
	{
		org.asam.ods.SelValueExt result = new org.asam.ods.SelValueExt();
		result.attr=org.asam.ods.AIDNameUnitIdHelper.read(in);
		result.oper=org.asam.ods.SelOpcodeHelper.read(in);
		result.value=org.asam.ods.TS_ValueHelper.read(in);
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.asam.ods.SelValueExt s)
	{
		org.asam.ods.AIDNameUnitIdHelper.write(out,s.attr);
		org.asam.ods.SelOpcodeHelper.write(out,s.oper);
		org.asam.ods.TS_ValueHelper.write(out,s.value);
	}
}
