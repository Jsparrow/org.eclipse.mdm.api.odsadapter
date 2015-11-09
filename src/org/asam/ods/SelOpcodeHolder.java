package org.asam.ods;
/**
 * Generated from IDL enum "SelOpcode".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class SelOpcodeHolder
	implements org.omg.CORBA.portable.Streamable
{
	public SelOpcode value;

	public SelOpcodeHolder ()
	{
	}
	public SelOpcodeHolder (final SelOpcode initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return SelOpcodeHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = SelOpcodeHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		SelOpcodeHelper.write (out,value);
	}
}
