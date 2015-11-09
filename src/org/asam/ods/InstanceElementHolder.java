package org.asam.ods;

/**
 * Generated from IDL interface "InstanceElement".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class InstanceElementHolder	implements org.omg.CORBA.portable.Streamable{
	 public InstanceElement value;
	public InstanceElementHolder()
	{
	}
	public InstanceElementHolder (final InstanceElement initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return InstanceElementHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = InstanceElementHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		InstanceElementHelper.write (_out,value);
	}
}
