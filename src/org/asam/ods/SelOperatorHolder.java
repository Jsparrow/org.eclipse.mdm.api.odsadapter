package org.asam.ods;
/**
 * Generated from IDL enum "SelOperator".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class SelOperatorHolder
	implements org.omg.CORBA.portable.Streamable
{
	public SelOperator value;

	public SelOperatorHolder ()
	{
	}
	public SelOperatorHolder (final SelOperator initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return SelOperatorHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = SelOperatorHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		SelOperatorHelper.write (out,value);
	}
}
