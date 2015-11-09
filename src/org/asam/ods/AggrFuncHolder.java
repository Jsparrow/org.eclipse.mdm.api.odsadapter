package org.asam.ods;
/**
 * Generated from IDL enum "AggrFunc".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class AggrFuncHolder
	implements org.omg.CORBA.portable.Streamable
{
	public AggrFunc value;

	public AggrFuncHolder ()
	{
	}
	public AggrFuncHolder (final AggrFunc initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return AggrFuncHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = AggrFuncHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		AggrFuncHelper.write (out,value);
	}
}
