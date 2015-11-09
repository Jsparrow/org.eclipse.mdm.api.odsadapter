package org.asam.ods;

/**
 * Generated from IDL interface "LockMode".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class LockModeHolder	implements org.omg.CORBA.portable.Streamable{
	 public LockMode value;
	public LockModeHolder()
	{
	}
	public LockModeHolder (final LockMode initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return LockModeHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = LockModeHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		LockModeHelper.write (_out,value);
	}
}
