package org.asam.ods;


/**
 * Generated from IDL struct "QueryStructureExt".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class QueryStructureExtHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.asam.ods.QueryStructureExtHelper.id(),"QueryStructureExt",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("anuSeq", org.asam.ods.SelAIDNameUnitIdSequenceHelper.type(), null),new org.omg.CORBA.StructMember("condSeq", org.asam.ods.SelItemSequenceHelper.type(), null),new org.omg.CORBA.StructMember("joinSeq", org.asam.ods.JoinDefSequenceHelper.type(), null),new org.omg.CORBA.StructMember("orderBy", org.asam.ods.SelOrderSequenceHelper.type(), null),new org.omg.CORBA.StructMember("groupBy", org.asam.ods.AIDNameSequenceHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.asam.ods.QueryStructureExt s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.asam.ods.QueryStructureExt extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/asam/ods/QueryStructureExt:1.0";
	}
	public static org.asam.ods.QueryStructureExt read (final org.omg.CORBA.portable.InputStream in)
	{
		org.asam.ods.QueryStructureExt result = new org.asam.ods.QueryStructureExt();
		result.anuSeq = org.asam.ods.SelAIDNameUnitIdSequenceHelper.read(in);
		result.condSeq = org.asam.ods.SelItemSequenceHelper.read(in);
		result.joinSeq = org.asam.ods.JoinDefSequenceHelper.read(in);
		result.orderBy = org.asam.ods.SelOrderSequenceHelper.read(in);
		result.groupBy = org.asam.ods.AIDNameSequenceHelper.read(in);
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.asam.ods.QueryStructureExt s)
	{
		org.asam.ods.SelAIDNameUnitIdSequenceHelper.write(out,s.anuSeq);
		org.asam.ods.SelItemSequenceHelper.write(out,s.condSeq);
		org.asam.ods.JoinDefSequenceHelper.write(out,s.joinSeq);
		org.asam.ods.SelOrderSequenceHelper.write(out,s.orderBy);
		org.asam.ods.AIDNameSequenceHelper.write(out,s.groupBy);
	}
}
