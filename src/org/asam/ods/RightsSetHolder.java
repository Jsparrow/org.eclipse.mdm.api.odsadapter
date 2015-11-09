package org.asam.ods;
/**
 * Generated from IDL enum "RightsSet".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class RightsSetHolder
	implements org.omg.CORBA.portable.Streamable
{
	public RightsSet value;

	public RightsSetHolder ()
	{
	}
	public RightsSetHolder (final RightsSet initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return RightsSetHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = RightsSetHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		RightsSetHelper.write (out,value);
	}
}
