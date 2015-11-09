package org.asam.ods;

/**
 * Generated from IDL struct "ApplicationStructureValue".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class ApplicationStructureValueHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.asam.ods.ApplicationStructureValue value;

	public ApplicationStructureValueHolder ()
	{
	}
	public ApplicationStructureValueHolder(final org.asam.ods.ApplicationStructureValue initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.asam.ods.ApplicationStructureValueHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.asam.ods.ApplicationStructureValueHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.asam.ods.ApplicationStructureValueHelper.write(_out, value);
	}
}
