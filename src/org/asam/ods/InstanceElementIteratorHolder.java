package org.asam.ods;

/**
 * Generated from IDL interface "InstanceElementIterator".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class InstanceElementIteratorHolder	implements org.omg.CORBA.portable.Streamable{
	 public InstanceElementIterator value;
	public InstanceElementIteratorHolder()
	{
	}
	public InstanceElementIteratorHolder (final InstanceElementIterator initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return InstanceElementIteratorHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = InstanceElementIteratorHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		InstanceElementIteratorHelper.write (_out,value);
	}
}
