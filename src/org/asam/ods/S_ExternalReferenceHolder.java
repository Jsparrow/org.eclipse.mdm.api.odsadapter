package org.asam.ods;

/**
 * Generated from IDL alias "S_ExternalReference".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class S_ExternalReferenceHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.asam.ods.T_ExternalReference[] value;

	public S_ExternalReferenceHolder ()
	{
	}
	public S_ExternalReferenceHolder (final org.asam.ods.T_ExternalReference[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return S_ExternalReferenceHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = S_ExternalReferenceHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		S_ExternalReferenceHelper.write (out,value);
	}
}
