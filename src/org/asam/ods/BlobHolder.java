package org.asam.ods;

/**
 * Generated from IDL interface "Blob".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class BlobHolder	implements org.omg.CORBA.portable.Streamable{
	 public Blob value;
	public BlobHolder()
	{
	}
	public BlobHolder (final Blob initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return BlobHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = BlobHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		BlobHelper.write (_out,value);
	}
}
