package org.asam.ods;

/**
 * Generated from IDL interface "NameValueUnitIterator".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class NameValueUnitIteratorHolder	implements org.omg.CORBA.portable.Streamable{
	 public NameValueUnitIterator value;
	public NameValueUnitIteratorHolder()
	{
	}
	public NameValueUnitIteratorHolder (final NameValueUnitIterator initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return NameValueUnitIteratorHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = NameValueUnitIteratorHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		NameValueUnitIteratorHelper.write (_out,value);
	}
}
