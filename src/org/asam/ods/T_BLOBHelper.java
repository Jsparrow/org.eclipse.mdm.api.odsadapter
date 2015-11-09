package org.asam.ods;

/**
 * Generated from IDL alias "T_BLOB".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class T_BLOBHelper
{
	private static org.omg.CORBA.TypeCode _type = null;

	public static void insert (org.omg.CORBA.Any any, org.asam.ods.Blob s)
	{
		any.type (type ());
		write (any.create_output_stream (), s);
	}

	public static org.asam.ods.Blob extract (final org.omg.CORBA.Any any)
	{
		return read (any.create_input_stream ());
	}

	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_alias_tc(org.asam.ods.T_BLOBHelper.id(), "T_BLOB",org.omg.CORBA.ORB.init().create_interface_tc("IDL:org/asam/ods/Blob:1.0", "Blob"));
		}
		return _type;
	}

	public static String id()
	{
		return "IDL:org/asam/ods/T_BLOB:1.0";
	}
	public static org.asam.ods.Blob read (final org.omg.CORBA.portable.InputStream _in)
	{
		org.asam.ods.Blob _result;
		_result=org.asam.ods.BlobHelper.read(_in);
		return _result;
	}

	public static void write (final org.omg.CORBA.portable.OutputStream _out, org.asam.ods.Blob _s)
	{
		org.asam.ods.BlobHelper.write(_out,_s);
	}
}
