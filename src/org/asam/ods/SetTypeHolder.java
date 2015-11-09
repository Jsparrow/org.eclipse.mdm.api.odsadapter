package org.asam.ods;
/**
 * Generated from IDL enum "SetType".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class SetTypeHolder
	implements org.omg.CORBA.portable.Streamable
{
	public SetType value;

	public SetTypeHolder ()
	{
	}
	public SetTypeHolder (final SetType initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return SetTypeHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = SetTypeHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		SetTypeHelper.write (out,value);
	}
}
