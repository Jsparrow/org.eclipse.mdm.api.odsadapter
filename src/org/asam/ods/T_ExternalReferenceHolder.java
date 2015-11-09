package org.asam.ods;

/**
 * Generated from IDL struct "T_ExternalReference".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class T_ExternalReferenceHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.asam.ods.T_ExternalReference value;

	public T_ExternalReferenceHolder ()
	{
	}
	public T_ExternalReferenceHolder(final org.asam.ods.T_ExternalReference initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.asam.ods.T_ExternalReferenceHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.asam.ods.T_ExternalReferenceHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.asam.ods.T_ExternalReferenceHelper.write(_out, value);
	}
}
