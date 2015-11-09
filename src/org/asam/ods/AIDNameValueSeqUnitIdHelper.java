package org.asam.ods;


/**
 * Generated from IDL struct "AIDNameValueSeqUnitId".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class AIDNameValueSeqUnitIdHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.asam.ods.AIDNameValueSeqUnitIdHelper.id(),"AIDNameValueSeqUnitId",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("attr", org.asam.ods.AIDNameHelper.type(), null),new org.omg.CORBA.StructMember("unitId", org.asam.ods.T_LONGLONGHelper.type(), null),new org.omg.CORBA.StructMember("values", org.asam.ods.TS_ValueSeqHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.asam.ods.AIDNameValueSeqUnitId s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.asam.ods.AIDNameValueSeqUnitId extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/asam/ods/AIDNameValueSeqUnitId:1.0";
	}
	public static org.asam.ods.AIDNameValueSeqUnitId read (final org.omg.CORBA.portable.InputStream in)
	{
		org.asam.ods.AIDNameValueSeqUnitId result = new org.asam.ods.AIDNameValueSeqUnitId();
		result.attr=org.asam.ods.AIDNameHelper.read(in);
		result.unitId=org.asam.ods.T_LONGLONGHelper.read(in);
		result.values=org.asam.ods.TS_ValueSeqHelper.read(in);
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.asam.ods.AIDNameValueSeqUnitId s)
	{
		org.asam.ods.AIDNameHelper.write(out,s.attr);
		org.asam.ods.T_LONGLONGHelper.write(out,s.unitId);
		org.asam.ods.TS_ValueSeqHelper.write(out,s.values);
	}
}
