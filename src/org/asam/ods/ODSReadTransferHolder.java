package org.asam.ods;

/**
 * Generated from IDL interface "ODSReadTransfer".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class ODSReadTransferHolder	implements org.omg.CORBA.portable.Streamable{
	 public ODSReadTransfer value;
	public ODSReadTransferHolder()
	{
	}
	public ODSReadTransferHolder (final ODSReadTransfer initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return ODSReadTransferHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = ODSReadTransferHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		ODSReadTransferHelper.write (_out,value);
	}
}
