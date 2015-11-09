package org.asam.ods;

/**
 * Generated from IDL interface "AoSession".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class AoSessionHolder	implements org.omg.CORBA.portable.Streamable{
	 public AoSession value;
	public AoSessionHolder()
	{
	}
	public AoSessionHolder (final AoSession initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return AoSessionHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = AoSessionHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		AoSessionHelper.write (_out,value);
	}
}
