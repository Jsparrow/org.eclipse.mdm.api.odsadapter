package org.asam.ods;

/**
 * Generated from IDL interface "ApplicationElement".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class ApplicationElementHolder	implements org.omg.CORBA.portable.Streamable{
	 public ApplicationElement value;
	public ApplicationElementHolder()
	{
	}
	public ApplicationElementHolder (final ApplicationElement initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return ApplicationElementHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = ApplicationElementHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		ApplicationElementHelper.write (_out,value);
	}
}
