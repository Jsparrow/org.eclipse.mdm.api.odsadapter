package org.asam.ods;

/**
 * Generated from IDL union "SelItem".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class SelItemHelper
{
	private static org.omg.CORBA.TypeCode _type;
	public static void insert (final org.omg.CORBA.Any any, final org.asam.ods.SelItem s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.asam.ods.SelItem extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/asam/ods/SelItem:1.0";
	}
	public static SelItem read (org.omg.CORBA.portable.InputStream in)
	{
		SelItem result = new SelItem ();
		org.asam.ods.SelType disc = org.asam.ods.SelType.from_int(in.read_long());
		switch (disc.value ())
		{
			case org.asam.ods.SelType._SEL_VALUE_TYPE:
			{
				org.asam.ods.SelValueExt _var;
				_var=org.asam.ods.SelValueExtHelper.read(in);
				result.value (_var);
				break;
			}
			case org.asam.ods.SelType._SEL_OPERATOR_TYPE:
			{
				org.asam.ods.SelOperator _var;
				_var=org.asam.ods.SelOperatorHelper.read(in);
				result.operator (_var);
				break;
			}
		}
		return result;
	}
	public static void write (org.omg.CORBA.portable.OutputStream out, SelItem s)
	{
		out.write_long (s.discriminator().value ());
		switch (s.discriminator().value ())
		{
			case org.asam.ods.SelType._SEL_VALUE_TYPE:
			{
				org.asam.ods.SelValueExtHelper.write(out,s.value ());
				break;
			}
			case org.asam.ods.SelType._SEL_OPERATOR_TYPE:
			{
				org.asam.ods.SelOperatorHelper.write(out,s.operator ());
				break;
			}
		}
	}
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			org.omg.CORBA.UnionMember[] members = new org.omg.CORBA.UnionMember[2];
			org.omg.CORBA.Any label_any;
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.asam.ods.SelTypeHelper.insert(label_any, org.asam.ods.SelType.SEL_VALUE_TYPE);
			members[0] = new org.omg.CORBA.UnionMember ("value", label_any, org.asam.ods.SelValueExtHelper.type(),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.asam.ods.SelTypeHelper.insert(label_any, org.asam.ods.SelType.SEL_OPERATOR_TYPE);
			members[1] = new org.omg.CORBA.UnionMember ("operator", label_any, org.asam.ods.SelOperatorHelper.type(),null);
			 _type = org.omg.CORBA.ORB.init().create_union_tc(id(),"SelItem",org.asam.ods.SelTypeHelper.type(), members);
		}
		return _type;
	}
}
