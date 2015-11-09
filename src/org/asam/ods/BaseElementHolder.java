package org.asam.ods;

/**
 * Generated from IDL interface "BaseElement".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class BaseElementHolder	implements org.omg.CORBA.portable.Streamable{
	 public BaseElement value;
	public BaseElementHolder()
	{
	}
	public BaseElementHolder (final BaseElement initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return BaseElementHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = BaseElementHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		BaseElementHelper.write (_out,value);
	}
}
