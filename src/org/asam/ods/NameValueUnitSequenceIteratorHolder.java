package org.asam.ods;

/**
 * Generated from IDL interface "NameValueUnitSequenceIterator".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class NameValueUnitSequenceIteratorHolder	implements org.omg.CORBA.portable.Streamable{
	 public NameValueUnitSequenceIterator value;
	public NameValueUnitSequenceIteratorHolder()
	{
	}
	public NameValueUnitSequenceIteratorHolder (final NameValueUnitSequenceIterator initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return NameValueUnitSequenceIteratorHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = NameValueUnitSequenceIteratorHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		NameValueUnitSequenceIteratorHelper.write (_out,value);
	}
}
