package org.asam.ods;

/**
 * Generated from IDL interface "AoFactory".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class AoFactoryHolder	implements org.omg.CORBA.portable.Streamable{
	 public AoFactory value;
	public AoFactoryHolder()
	{
	}
	public AoFactoryHolder (final AoFactory initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return AoFactoryHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = AoFactoryHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		AoFactoryHelper.write (_out,value);
	}
}
