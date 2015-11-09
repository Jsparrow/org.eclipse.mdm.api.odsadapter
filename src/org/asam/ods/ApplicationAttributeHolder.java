package org.asam.ods;

/**
 * Generated from IDL interface "ApplicationAttribute".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class ApplicationAttributeHolder	implements org.omg.CORBA.portable.Streamable{
	 public ApplicationAttribute value;
	public ApplicationAttributeHolder()
	{
	}
	public ApplicationAttributeHolder (final ApplicationAttribute initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return ApplicationAttributeHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = ApplicationAttributeHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		ApplicationAttributeHelper.write (_out,value);
	}
}
