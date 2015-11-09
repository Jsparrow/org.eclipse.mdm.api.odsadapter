package org.asam.ods;
/**
 * Generated from IDL enum "AttrType".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class AttrTypeHolder
	implements org.omg.CORBA.portable.Streamable
{
	public AttrType value;

	public AttrTypeHolder ()
	{
	}
	public AttrTypeHolder (final AttrType initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return AttrTypeHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = AttrTypeHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		AttrTypeHelper.write (out,value);
	}
}
