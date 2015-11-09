package org.asam.ods;

/**
 * Generated from IDL interface "ODSFile".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class ODSFileHolder	implements org.omg.CORBA.portable.Streamable{
	 public ODSFile value;
	public ODSFileHolder()
	{
	}
	public ODSFileHolder (final ODSFile initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return ODSFileHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = ODSFileHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		ODSFileHelper.write (_out,value);
	}
}
