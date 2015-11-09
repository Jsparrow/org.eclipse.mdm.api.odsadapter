package org.asam.ods;


/**
 * Generated from IDL struct "EnumerationItemStructure".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class EnumerationItemStructureHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.asam.ods.EnumerationItemStructureHelper.id(),"EnumerationItemStructure",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("index", org.asam.ods.T_LONGHelper.type(), null),new org.omg.CORBA.StructMember("itemName", org.asam.ods.T_STRINGHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.asam.ods.EnumerationItemStructure s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.asam.ods.EnumerationItemStructure extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/asam/ods/EnumerationItemStructure:1.0";
	}
	public static org.asam.ods.EnumerationItemStructure read (final org.omg.CORBA.portable.InputStream in)
	{
		org.asam.ods.EnumerationItemStructure result = new org.asam.ods.EnumerationItemStructure();
		result.index=in.read_long();
		result.itemName=in.read_string();
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.asam.ods.EnumerationItemStructure s)
	{
		out.write_long(s.index);
		out.write_string(s.itemName);
	}
}
