package org.asam.ods;


/**
 * Generated from IDL struct "SelAIDNameUnitId".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class SelAIDNameUnitIdHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.asam.ods.SelAIDNameUnitIdHelper.id(),"SelAIDNameUnitId",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("attr", org.asam.ods.AIDNameHelper.type(), null),new org.omg.CORBA.StructMember("unitId", org.asam.ods.T_LONGLONGHelper.type(), null),new org.omg.CORBA.StructMember("aggregate", org.asam.ods.AggrFuncHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.asam.ods.SelAIDNameUnitId s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.asam.ods.SelAIDNameUnitId extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/asam/ods/SelAIDNameUnitId:1.0";
	}
	public static org.asam.ods.SelAIDNameUnitId read (final org.omg.CORBA.portable.InputStream in)
	{
		org.asam.ods.SelAIDNameUnitId result = new org.asam.ods.SelAIDNameUnitId();
		result.attr=org.asam.ods.AIDNameHelper.read(in);
		result.unitId=org.asam.ods.T_LONGLONGHelper.read(in);
		result.aggregate=org.asam.ods.AggrFuncHelper.read(in);
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.asam.ods.SelAIDNameUnitId s)
	{
		org.asam.ods.AIDNameHelper.write(out,s.attr);
		org.asam.ods.T_LONGLONGHelper.write(out,s.unitId);
		org.asam.ods.AggrFuncHelper.write(out,s.aggregate);
	}
}
