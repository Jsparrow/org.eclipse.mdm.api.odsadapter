package org.asam.ods;
/**
 * Generated from IDL union "TS_Union".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class TS_UnionHolder
	implements org.omg.CORBA.portable.Streamable
{
	public TS_Union value;

	public TS_UnionHolder ()
	{
	}
	public TS_UnionHolder (final TS_Union initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return TS_UnionHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = TS_UnionHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		TS_UnionHelper.write (out, value);
	}
}
