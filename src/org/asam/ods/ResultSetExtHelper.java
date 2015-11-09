package org.asam.ods;


/**
 * Generated from IDL struct "ResultSetExt".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class ResultSetExtHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.asam.ods.ResultSetExtHelper.id(),"ResultSetExt",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("firstElems", org.asam.ods.ElemResultSetExtSequenceHelper.type(), null),new org.omg.CORBA.StructMember("restElems", org.omg.CORBA.ORB.init().create_interface_tc("IDL:org/asam/ods/ElemResultSetExtSeqIterator:1.0", "ElemResultSetExtSeqIterator"), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.asam.ods.ResultSetExt s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.asam.ods.ResultSetExt extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/asam/ods/ResultSetExt:1.0";
	}
	public static org.asam.ods.ResultSetExt read (final org.omg.CORBA.portable.InputStream in)
	{
		org.asam.ods.ResultSetExt result = new org.asam.ods.ResultSetExt();
		result.firstElems = org.asam.ods.ElemResultSetExtSequenceHelper.read(in);
		result.restElems=org.asam.ods.ElemResultSetExtSeqIteratorHelper.read(in);
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.asam.ods.ResultSetExt s)
	{
		org.asam.ods.ElemResultSetExtSequenceHelper.write(out,s.firstElems);
		org.asam.ods.ElemResultSetExtSeqIteratorHelper.write(out,s.restElems);
	}
}
