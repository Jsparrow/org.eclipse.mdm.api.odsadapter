package org.asam.ods;


/**
 * Generated from IDL struct "AIDNameUnitId".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class AIDNameUnitIdHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.asam.ods.AIDNameUnitIdHelper.id(),"AIDNameUnitId",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("attr", org.asam.ods.AIDNameHelper.type(), null),new org.omg.CORBA.StructMember("unitId", org.asam.ods.T_LONGLONGHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.asam.ods.AIDNameUnitId s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.asam.ods.AIDNameUnitId extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/asam/ods/AIDNameUnitId:1.0";
	}
	public static org.asam.ods.AIDNameUnitId read (final org.omg.CORBA.portable.InputStream in)
	{
		org.asam.ods.AIDNameUnitId result = new org.asam.ods.AIDNameUnitId();
		result.attr=org.asam.ods.AIDNameHelper.read(in);
		result.unitId=org.asam.ods.T_LONGLONGHelper.read(in);
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.asam.ods.AIDNameUnitId s)
	{
		org.asam.ods.AIDNameHelper.write(out,s.attr);
		org.asam.ods.T_LONGLONGHelper.write(out,s.unitId);
	}
}
