package org.asam.ods;


/**
 * Generated from IDL struct "RelationRange".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class RelationRangeHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.asam.ods.RelationRangeHelper.id(),"RelationRange",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("min", org.asam.ods.T_SHORTHelper.type(), null),new org.omg.CORBA.StructMember("max", org.asam.ods.T_SHORTHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.asam.ods.RelationRange s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.asam.ods.RelationRange extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/asam/ods/RelationRange:1.0";
	}
	public static org.asam.ods.RelationRange read (final org.omg.CORBA.portable.InputStream in)
	{
		org.asam.ods.RelationRange result = new org.asam.ods.RelationRange();
		result.min=in.read_short();
		result.max=in.read_short();
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.asam.ods.RelationRange s)
	{
		out.write_short(s.min);
		out.write_short(s.max);
	}
}
