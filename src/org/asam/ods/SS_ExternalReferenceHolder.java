package org.asam.ods;

/**
 * Generated from IDL alias "SS_ExternalReference".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class SS_ExternalReferenceHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.asam.ods.T_ExternalReference[][] value;

	public SS_ExternalReferenceHolder ()
	{
	}
	public SS_ExternalReferenceHolder (final org.asam.ods.T_ExternalReference[][] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return SS_ExternalReferenceHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = SS_ExternalReferenceHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		SS_ExternalReferenceHelper.write (out,value);
	}
}
