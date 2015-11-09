package org.asam.ods;
/**
 * Generated from IDL enum "Relationship".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class RelationshipHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_enum_tc(org.asam.ods.RelationshipHelper.id(),"Relationship",new String[]{"FATHER","CHILD","INFO_TO","INFO_FROM","INFO_REL","SUPERTYPE","SUBTYPE","ALL_REL"});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.asam.ods.Relationship s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.asam.ods.Relationship extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/asam/ods/Relationship:1.0";
	}
	public static Relationship read (final org.omg.CORBA.portable.InputStream in)
	{
		return Relationship.from_int(in.read_long());
	}

	public static void write (final org.omg.CORBA.portable.OutputStream out, final Relationship s)
	{
		out.write_long(s.value());
	}
}
