package org.asam.ods;


/**
 * Generated from IDL struct "ApplicationRelationInstanceElementSeq".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class ApplicationRelationInstanceElementSeqHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.asam.ods.ApplicationRelationInstanceElementSeqHelper.id(),"ApplicationRelationInstanceElementSeq",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("applRel", org.omg.CORBA.ORB.init().create_interface_tc("IDL:org/asam/ods/ApplicationRelation:1.0", "ApplicationRelation"), null),new org.omg.CORBA.StructMember("instances", org.asam.ods.InstanceElementSequenceHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.asam.ods.ApplicationRelationInstanceElementSeq s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.asam.ods.ApplicationRelationInstanceElementSeq extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/asam/ods/ApplicationRelationInstanceElementSeq:1.0";
	}
	public static org.asam.ods.ApplicationRelationInstanceElementSeq read (final org.omg.CORBA.portable.InputStream in)
	{
		org.asam.ods.ApplicationRelationInstanceElementSeq result = new org.asam.ods.ApplicationRelationInstanceElementSeq();
		result.applRel=org.asam.ods.ApplicationRelationHelper.read(in);
		result.instances = org.asam.ods.InstanceElementSequenceHelper.read(in);
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.asam.ods.ApplicationRelationInstanceElementSeq s)
	{
		org.asam.ods.ApplicationRelationHelper.write(out,s.applRel);
		org.asam.ods.InstanceElementSequenceHelper.write(out,s.instances);
	}
}
