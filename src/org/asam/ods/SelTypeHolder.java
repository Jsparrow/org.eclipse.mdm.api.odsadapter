package org.asam.ods;
/**
 * Generated from IDL enum "SelType".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class SelTypeHolder
	implements org.omg.CORBA.portable.Streamable
{
	public SelType value;

	public SelTypeHolder ()
	{
	}
	public SelTypeHolder (final SelType initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return SelTypeHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = SelTypeHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		SelTypeHelper.write (out,value);
	}
}
