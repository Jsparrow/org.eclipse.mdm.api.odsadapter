package org.asam.ods;


/**
 * Generated from IDL struct "TS_ValueSeq".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class TS_ValueSeqHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.asam.ods.TS_ValueSeqHelper.id(),"TS_ValueSeq",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("u", org.asam.ods.TS_UnionSeqHelper.type(), null),new org.omg.CORBA.StructMember("flag", org.asam.ods.S_SHORTHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.asam.ods.TS_ValueSeq s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.asam.ods.TS_ValueSeq extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/asam/ods/TS_ValueSeq:1.0";
	}
	public static org.asam.ods.TS_ValueSeq read (final org.omg.CORBA.portable.InputStream in)
	{
		org.asam.ods.TS_ValueSeq result = new org.asam.ods.TS_ValueSeq();
		result.u=org.asam.ods.TS_UnionSeqHelper.read(in);
		result.flag = org.asam.ods.S_SHORTHelper.read(in);
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.asam.ods.TS_ValueSeq s)
	{
		org.asam.ods.TS_UnionSeqHelper.write(out,s.u);
		org.asam.ods.S_SHORTHelper.write(out,s.flag);
	}
}
