package org.asam.ods;


/**
 * Generated from IDL struct "ApplicationStructureValue".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class ApplicationStructureValueHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(org.asam.ods.ApplicationStructureValueHelper.id(),"ApplicationStructureValue",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("applElems", org.asam.ods.ApplElemSequenceHelper.type(), null),new org.omg.CORBA.StructMember("applRels", org.asam.ods.ApplRelSequenceHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.asam.ods.ApplicationStructureValue s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.asam.ods.ApplicationStructureValue extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/asam/ods/ApplicationStructureValue:1.0";
	}
	public static org.asam.ods.ApplicationStructureValue read (final org.omg.CORBA.portable.InputStream in)
	{
		org.asam.ods.ApplicationStructureValue result = new org.asam.ods.ApplicationStructureValue();
		result.applElems = org.asam.ods.ApplElemSequenceHelper.read(in);
		result.applRels = org.asam.ods.ApplRelSequenceHelper.read(in);
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.asam.ods.ApplicationStructureValue s)
	{
		org.asam.ods.ApplElemSequenceHelper.write(out,s.applElems);
		org.asam.ods.ApplRelSequenceHelper.write(out,s.applRels);
	}
}
