package org.asam.ods;


/**
 * Generated from IDL interface "ApplicationRelation".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class ApplicationRelationHelper
{
	public static void insert (final org.omg.CORBA.Any any, final org.asam.ods.ApplicationRelation s)
	{
			any.insert_Object(s);
	}
	public static org.asam.ods.ApplicationRelation extract(final org.omg.CORBA.Any any)
	{
		return narrow(any.extract_Object()) ;
	}
	public static org.omg.CORBA.TypeCode type()
	{
		return org.omg.CORBA.ORB.init().create_interface_tc("IDL:org/asam/ods/ApplicationRelation:1.0", "ApplicationRelation");
	}
	public static String id()
	{
		return "IDL:org/asam/ods/ApplicationRelation:1.0";
	}
	public static ApplicationRelation read(final org.omg.CORBA.portable.InputStream in)
	{
		return narrow(in.read_Object(org.asam.ods._ApplicationRelationStub.class));
	}
	public static void write(final org.omg.CORBA.portable.OutputStream _out, final org.asam.ods.ApplicationRelation s)
	{
		_out.write_Object(s);
	}
	public static org.asam.ods.ApplicationRelation narrow(final org.omg.CORBA.Object obj)
	{
		if (obj == null)
		{
			return null;
		}
		else if (obj instanceof org.asam.ods.ApplicationRelation)
		{
			return (org.asam.ods.ApplicationRelation)obj;
		}
		else if (obj._is_a("IDL:org/asam/ods/ApplicationRelation:1.0"))
		{
			org.asam.ods._ApplicationRelationStub stub;
			stub = new org.asam.ods._ApplicationRelationStub();
			stub._set_delegate(((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate());
			return stub;
		}
		else
		{
			throw new org.omg.CORBA.BAD_PARAM("Narrow failed");
		}
	}
	public static org.asam.ods.ApplicationRelation unchecked_narrow(final org.omg.CORBA.Object obj)
	{
		if (obj == null)
		{
			return null;
		}
		else if (obj instanceof org.asam.ods.ApplicationRelation)
		{
			return (org.asam.ods.ApplicationRelation)obj;
		}
		else
		{
			org.asam.ods._ApplicationRelationStub stub;
			stub = new org.asam.ods._ApplicationRelationStub();
			stub._set_delegate(((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate());
			return stub;
		}
	}
}
