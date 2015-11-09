package org.asam.ods;

/**
 * Generated from IDL interface "NameIterator".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class NameIteratorHolder	implements org.omg.CORBA.portable.Streamable{
	 public NameIterator value;
	public NameIteratorHolder()
	{
	}
	public NameIteratorHolder (final NameIterator initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return NameIteratorHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = NameIteratorHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		NameIteratorHelper.write (_out,value);
	}
}
