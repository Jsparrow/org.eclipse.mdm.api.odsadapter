package org.asam.ods;

/**
 * Generated from IDL interface "SMatLink".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class SMatLinkHolder	implements org.omg.CORBA.portable.Streamable{
	 public SMatLink value;
	public SMatLinkHolder()
	{
	}
	public SMatLinkHolder (final SMatLink initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return SMatLinkHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = SMatLinkHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		SMatLinkHelper.write (_out,value);
	}
}
