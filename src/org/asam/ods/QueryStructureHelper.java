package org.asam.ods;


/**
 * Generated from IDL struct "QueryStructure".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class QueryStructureHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.asam.ods.QueryStructureHelper.id(),"QueryStructure",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("anuSeq", org.asam.ods.AIDNameUnitIdSequenceHelper.type(), null),new org.omg.CORBA.StructMember("condSeq", org.asam.ods.SelValueSequenceHelper.type(), null),new org.omg.CORBA.StructMember("operSeq", org.asam.ods.SelOperatorSequenceHelper.type(), null),new org.omg.CORBA.StructMember("relInst", org.asam.ods.ElemIdHelper.type(), null),new org.omg.CORBA.StructMember("relName", org.asam.ods.NameHelper.type(), null),new org.omg.CORBA.StructMember("orderBy", org.asam.ods.SelOrderSequenceHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.asam.ods.QueryStructure s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.asam.ods.QueryStructure extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/asam/ods/QueryStructure:1.0";
	}
	public static org.asam.ods.QueryStructure read (final org.omg.CORBA.portable.InputStream in)
	{
		org.asam.ods.QueryStructure result = new org.asam.ods.QueryStructure();
		result.anuSeq = org.asam.ods.AIDNameUnitIdSequenceHelper.read(in);
		result.condSeq = org.asam.ods.SelValueSequenceHelper.read(in);
		result.operSeq = org.asam.ods.SelOperatorSequenceHelper.read(in);
		result.relInst=org.asam.ods.ElemIdHelper.read(in);
		result.relName=in.read_string();
		result.orderBy = org.asam.ods.SelOrderSequenceHelper.read(in);
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.asam.ods.QueryStructure s)
	{
		org.asam.ods.AIDNameUnitIdSequenceHelper.write(out,s.anuSeq);
		org.asam.ods.SelValueSequenceHelper.write(out,s.condSeq);
		org.asam.ods.SelOperatorSequenceHelper.write(out,s.operSeq);
		org.asam.ods.ElemIdHelper.write(out,s.relInst);
		out.write_string(s.relName);
		org.asam.ods.SelOrderSequenceHelper.write(out,s.orderBy);
	}
}
