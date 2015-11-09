package org.asam.ods;
/**
 * Generated from IDL enum "DataType".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class DataTypeHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_enum_tc(org.asam.ods.DataTypeHelper.id(),"DataType",new String[]{"DT_UNKNOWN","DT_STRING","DT_SHORT","DT_FLOAT","DT_BOOLEAN","DT_BYTE","DT_LONG","DT_DOUBLE","DT_LONGLONG","DT_ID","DT_DATE","DT_BYTESTR","DT_BLOB","DT_COMPLEX","DT_DCOMPLEX","DS_STRING","DS_SHORT","DS_FLOAT","DS_BOOLEAN","DS_BYTE","DS_LONG","DS_DOUBLE","DS_LONGLONG","DS_COMPLEX","DS_DCOMPLEX","DS_ID","DS_DATE","DS_BYTESTR","DT_EXTERNALREFERENCE","DS_EXTERNALREFERENCE","DT_ENUM","DS_ENUM"});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.asam.ods.DataType s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.asam.ods.DataType extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/asam/ods/DataType:1.0";
	}
	public static DataType read (final org.omg.CORBA.portable.InputStream in)
	{
		return DataType.from_int(in.read_long());
	}

	public static void write (final org.omg.CORBA.portable.OutputStream out, final DataType s)
	{
		out.write_long(s.value());
	}
}
