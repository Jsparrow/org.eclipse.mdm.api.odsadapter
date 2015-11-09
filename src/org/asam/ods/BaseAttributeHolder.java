package org.asam.ods;

/**
 * Generated from IDL interface "BaseAttribute".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class BaseAttributeHolder	implements org.omg.CORBA.portable.Streamable{
	 public BaseAttribute value;
	public BaseAttributeHolder()
	{
	}
	public BaseAttributeHolder (final BaseAttribute initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return BaseAttributeHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = BaseAttributeHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		BaseAttributeHelper.write (_out,value);
	}
}
