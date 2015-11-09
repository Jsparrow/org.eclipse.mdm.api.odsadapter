package org.asam.ods;

/**
 * Generated from IDL interface "ApplicationStructure".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class ApplicationStructureHolder	implements org.omg.CORBA.portable.Streamable{
	 public ApplicationStructure value;
	public ApplicationStructureHolder()
	{
	}
	public ApplicationStructureHolder (final ApplicationStructure initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return ApplicationStructureHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = ApplicationStructureHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		ApplicationStructureHelper.write (_out,value);
	}
}
