package org.asam.ods;

/**
 * Generated from IDL interface "NameValueUnitIdIterator".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class NameValueUnitIdIteratorHolder	implements org.omg.CORBA.portable.Streamable{
	 public NameValueUnitIdIterator value;
	public NameValueUnitIdIteratorHolder()
	{
	}
	public NameValueUnitIdIteratorHolder (final NameValueUnitIdIterator initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return NameValueUnitIdIteratorHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = NameValueUnitIdIteratorHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		NameValueUnitIdIteratorHelper.write (_out,value);
	}
}
