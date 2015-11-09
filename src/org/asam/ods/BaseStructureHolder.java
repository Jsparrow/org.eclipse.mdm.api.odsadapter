package org.asam.ods;

/**
 * Generated from IDL interface "BaseStructure".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class BaseStructureHolder	implements org.omg.CORBA.portable.Streamable{
	 public BaseStructure value;
	public BaseStructureHolder()
	{
	}
	public BaseStructureHolder (final BaseStructure initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return BaseStructureHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = BaseStructureHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		BaseStructureHelper.write (_out,value);
	}
}
