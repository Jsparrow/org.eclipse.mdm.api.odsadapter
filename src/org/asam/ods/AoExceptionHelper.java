package org.asam.ods;


/**
 * Generated from IDL exception "AoException".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class AoExceptionHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_exception_tc(org.asam.ods.AoExceptionHelper.id(),"AoException",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("errCode", org.asam.ods.ErrorCodeHelper.type(), null),new org.omg.CORBA.StructMember("sevFlag", org.asam.ods.SeverityFlagHelper.type(), null),new org.omg.CORBA.StructMember("minorCode", org.asam.ods.T_LONGHelper.type(), null),new org.omg.CORBA.StructMember("reason", org.asam.ods.T_STRINGHelper.type(), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final org.asam.ods.AoException s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.asam.ods.AoException extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/asam/ods/AoException:1.0";
	}
	public static org.asam.ods.AoException read (final org.omg.CORBA.portable.InputStream in)
	{
		org.asam.ods.AoException result = new org.asam.ods.AoException();
		if (!in.read_string().equals(id())) throw new org.omg.CORBA.MARSHAL("wrong id");
		result.errCode=org.asam.ods.ErrorCodeHelper.read(in);
		result.sevFlag=org.asam.ods.SeverityFlagHelper.read(in);
		result.minorCode=in.read_long();
		result.reason=in.read_string();
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final org.asam.ods.AoException s)
	{
		out.write_string(id());
		org.asam.ods.ErrorCodeHelper.write(out,s.errCode);
		org.asam.ods.SeverityFlagHelper.write(out,s.sevFlag);
		out.write_long(s.minorCode);
		out.write_string(s.reason);
	}
}
