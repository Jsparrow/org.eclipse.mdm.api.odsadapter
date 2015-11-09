package org.asam.ods;
/**
 * Generated from IDL enum "RelationType".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class RelationTypeHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_enum_tc(org.asam.ods.RelationTypeHelper.id(),"RelationType",new String[]{"FATHER_CHILD","INFO","INHERITANCE"});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.asam.ods.RelationType s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.asam.ods.RelationType extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/asam/ods/RelationType:1.0";
	}
	public static RelationType read (final org.omg.CORBA.portable.InputStream in)
	{
		return RelationType.from_int(in.read_long());
	}

	public static void write (final org.omg.CORBA.portable.OutputStream out, final RelationType s)
	{
		out.write_long(s.value());
	}
}
