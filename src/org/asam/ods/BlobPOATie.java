package org.asam.ods;

import org.omg.PortableServer.POA;

/**
 * Generated from IDL interface "Blob".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public class BlobPOATie
	extends BlobPOA
{
	private BlobOperations _delegate;

	private POA _poa;
	public BlobPOATie(BlobOperations delegate)
	{
		_delegate = delegate;
	}
	public BlobPOATie(BlobOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.asam.ods.Blob _this()
	{
		return org.asam.ods.BlobHelper.narrow(_this_object());
	}
	public org.asam.ods.Blob _this(org.omg.CORBA.ORB orb)
	{
		return org.asam.ods.BlobHelper.narrow(_this_object(orb));
	}
	public BlobOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(BlobOperations delegate)
	{
		_delegate = delegate;
	}
	public POA _default_POA()
	{
		if (_poa != null)
		{
			return _poa;
		}
		return super._default_POA();
	}
	public void setHeader(java.lang.String header) throws org.asam.ods.AoException
	{
_delegate.setHeader(header);
	}

	public int getLength() throws org.asam.ods.AoException
	{
		return _delegate.getLength();
	}

	public byte[] get(int offset, int length) throws org.asam.ods.AoException
	{
		return _delegate.get(offset,length);
	}

	public java.lang.String getHeader() throws org.asam.ods.AoException
	{
		return _delegate.getHeader();
	}

	public void append(byte[] value) throws org.asam.ods.AoException
	{
_delegate.append(value);
	}

	public boolean compare(org.asam.ods.Blob aBlob) throws org.asam.ods.AoException
	{
		return _delegate.compare(aBlob);
	}

	public void destroy() throws org.asam.ods.AoException
	{
_delegate.destroy();
	}

	public void set(byte[] value) throws org.asam.ods.AoException
	{
_delegate.set(value);
	}

}
