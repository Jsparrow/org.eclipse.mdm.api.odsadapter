package org.asam.ods;


/**
 * Generated from IDL struct "EnumerationStructure".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class EnumerationStructureHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.asam.ods.EnumerationStructureHelper.id(),"EnumerationStructure",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("enumName", org.asam.ods.T_STRINGHelper.type(), null),new org.omg.CORBA.StructMember("items", org.asam.ods.EnumerationItemStructureSequenceHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.asam.ods.EnumerationStructure s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.asam.ods.EnumerationStructure extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/asam/ods/EnumerationStructure:1.0";
	}
	public static org.asam.ods.EnumerationStructure read (final org.omg.CORBA.portable.InputStream in)
	{
		org.asam.ods.EnumerationStructure result = new org.asam.ods.EnumerationStructure();
		result.enumName=in.read_string();
		result.items = org.asam.ods.EnumerationItemStructureSequenceHelper.read(in);
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.asam.ods.EnumerationStructure s)
	{
		out.write_string(s.enumName);
		org.asam.ods.EnumerationItemStructureSequenceHelper.write(out,s.items);
	}
}
