package org.asam.ods;

/**
 * Generated from IDL struct "ElemResultSetExt".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class ElemResultSetExtHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.asam.ods.ElemResultSetExt value;

	public ElemResultSetExtHolder ()
	{
	}
	public ElemResultSetExtHolder(final org.asam.ods.ElemResultSetExt initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.asam.ods.ElemResultSetExtHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.asam.ods.ElemResultSetExtHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.asam.ods.ElemResultSetExtHelper.write(_out, value);
	}
}
