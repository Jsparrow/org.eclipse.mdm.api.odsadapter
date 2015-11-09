package org.asam.ods;


/**
 * Generated from IDL struct "AttrResultSet".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class AttrResultSetHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.asam.ods.AttrResultSetHelper.id(),"AttrResultSet",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("attrValues", org.asam.ods.NameValueSeqUnitIdHelper.type(), null),new org.omg.CORBA.StructMember("rest", org.omg.CORBA.ORB.init().create_interface_tc("IDL:org/asam/ods/NameValueUnitIdIterator:1.0", "NameValueUnitIdIterator"), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.asam.ods.AttrResultSet s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.asam.ods.AttrResultSet extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/asam/ods/AttrResultSet:1.0";
	}
	public static org.asam.ods.AttrResultSet read (final org.omg.CORBA.portable.InputStream in)
	{
		org.asam.ods.AttrResultSet result = new org.asam.ods.AttrResultSet();
		result.attrValues=org.asam.ods.NameValueSeqUnitIdHelper.read(in);
		result.rest=org.asam.ods.NameValueUnitIdIteratorHelper.read(in);
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.asam.ods.AttrResultSet s)
	{
		org.asam.ods.NameValueSeqUnitIdHelper.write(out,s.attrValues);
		org.asam.ods.NameValueUnitIdIteratorHelper.write(out,s.rest);
	}
}
