package org.asam.ods;
/**
 * Generated from IDL enum "BuildUpFunction".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class BuildUpFunctionHolder
	implements org.omg.CORBA.portable.Streamable
{
	public BuildUpFunction value;

	public BuildUpFunctionHolder ()
	{
	}
	public BuildUpFunctionHolder (final BuildUpFunction initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return BuildUpFunctionHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = BuildUpFunctionHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		BuildUpFunctionHelper.write (out,value);
	}
}
