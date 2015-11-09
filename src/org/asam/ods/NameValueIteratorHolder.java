package org.asam.ods;

/**
 * Generated from IDL interface "NameValueIterator".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class NameValueIteratorHolder	implements org.omg.CORBA.portable.Streamable{
	 public NameValueIterator value;
	public NameValueIteratorHolder()
	{
	}
	public NameValueIteratorHolder (final NameValueIterator initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return NameValueIteratorHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = NameValueIteratorHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		NameValueIteratorHelper.write (_out,value);
	}
}
