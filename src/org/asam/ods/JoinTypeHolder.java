package org.asam.ods;
/**
 * Generated from IDL enum "JoinType".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class JoinTypeHolder
	implements org.omg.CORBA.portable.Streamable
{
	public JoinType value;

	public JoinTypeHolder ()
	{
	}
	public JoinTypeHolder (final JoinType initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return JoinTypeHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = JoinTypeHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		JoinTypeHelper.write (out,value);
	}
}
