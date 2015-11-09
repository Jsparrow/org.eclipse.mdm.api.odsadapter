package org.asam.ods;

/**
 * Generated from IDL interface "ODSWriteTransfer".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class ODSWriteTransferHolder	implements org.omg.CORBA.portable.Streamable{
	 public ODSWriteTransfer value;
	public ODSWriteTransferHolder()
	{
	}
	public ODSWriteTransferHolder (final ODSWriteTransfer initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return ODSWriteTransferHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = ODSWriteTransferHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		ODSWriteTransferHelper.write (_out,value);
	}
}
