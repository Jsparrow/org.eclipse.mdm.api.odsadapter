package org.asam.ods;


/**
 * Generated from IDL interface "SubMatrix".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class SubMatrixHelper
{
	public static void insert (final org.omg.CORBA.Any any, final org.asam.ods.SubMatrix s)
	{
			any.insert_Object(s);
	}
	public static org.asam.ods.SubMatrix extract(final org.omg.CORBA.Any any)
	{
		return narrow(any.extract_Object()) ;
	}
	public static org.omg.CORBA.TypeCode type()
	{
		return org.omg.CORBA.ORB.init().create_interface_tc("IDL:org/asam/ods/SubMatrix:1.0", "SubMatrix");
	}
	public static String id()
	{
		return "IDL:org/asam/ods/SubMatrix:1.0";
	}
	public static SubMatrix read(final org.omg.CORBA.portable.InputStream in)
	{
		return narrow(in.read_Object(org.asam.ods._SubMatrixStub.class));
	}
	public static void write(final org.omg.CORBA.portable.OutputStream _out, final org.asam.ods.SubMatrix s)
	{
		_out.write_Object(s);
	}
	public static org.asam.ods.SubMatrix narrow(final org.omg.CORBA.Object obj)
	{
		if (obj == null)
		{
			return null;
		}
		else if (obj instanceof org.asam.ods.SubMatrix)
		{
			return (org.asam.ods.SubMatrix)obj;
		}
		else if (obj._is_a("IDL:org/asam/ods/SubMatrix:1.0"))
		{
			org.asam.ods._SubMatrixStub stub;
			stub = new org.asam.ods._SubMatrixStub();
			stub._set_delegate(((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate());
			return stub;
		}
		else
		{
			throw new org.omg.CORBA.BAD_PARAM("Narrow failed");
		}
	}
	public static org.asam.ods.SubMatrix unchecked_narrow(final org.omg.CORBA.Object obj)
	{
		if (obj == null)
		{
			return null;
		}
		else if (obj instanceof org.asam.ods.SubMatrix)
		{
			return (org.asam.ods.SubMatrix)obj;
		}
		else
		{
			org.asam.ods._SubMatrixStub stub;
			stub = new org.asam.ods._SubMatrixStub();
			stub._set_delegate(((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate());
			return stub;
		}
	}
}
