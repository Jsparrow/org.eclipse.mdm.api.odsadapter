package org.asam.ods;
/**
 * Generated from IDL union "SelItem".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class SelItemHolder
	implements org.omg.CORBA.portable.Streamable
{
	public SelItem value;

	public SelItemHolder ()
	{
	}
	public SelItemHolder (final SelItem initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return SelItemHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = SelItemHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		SelItemHelper.write (out, value);
	}
}
