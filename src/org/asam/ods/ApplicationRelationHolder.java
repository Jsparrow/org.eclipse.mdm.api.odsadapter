package org.asam.ods;

/**
 * Generated from IDL interface "ApplicationRelation".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class ApplicationRelationHolder	implements org.omg.CORBA.portable.Streamable{
	 public ApplicationRelation value;
	public ApplicationRelationHolder()
	{
	}
	public ApplicationRelationHolder (final ApplicationRelation initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return ApplicationRelationHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = ApplicationRelationHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		ApplicationRelationHelper.write (_out,value);
	}
}
