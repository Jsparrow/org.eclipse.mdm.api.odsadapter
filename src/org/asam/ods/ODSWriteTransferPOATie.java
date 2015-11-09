package org.asam.ods;

import org.omg.PortableServer.POA;

/**
 * Generated from IDL interface "ODSWriteTransfer".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public class ODSWriteTransferPOATie
	extends ODSWriteTransferPOA
{
	private ODSWriteTransferOperations _delegate;

	private POA _poa;
	public ODSWriteTransferPOATie(ODSWriteTransferOperations delegate)
	{
		_delegate = delegate;
	}
	public ODSWriteTransferPOATie(ODSWriteTransferOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.asam.ods.ODSWriteTransfer _this()
	{
		return org.asam.ods.ODSWriteTransferHelper.narrow(_this_object());
	}
	public org.asam.ods.ODSWriteTransfer _this(org.omg.CORBA.ORB orb)
	{
		return org.asam.ods.ODSWriteTransferHelper.narrow(_this_object(orb));
	}
	public ODSWriteTransferOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(ODSWriteTransferOperations delegate)
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
	public org.asam.ods.T_LONGLONG getPosition() throws org.asam.ods.AoException
	{
		return _delegate.getPosition();
	}

	public void putOctectSeq(byte[] buffer) throws org.asam.ods.AoException
	{
_delegate.putOctectSeq(buffer);
	}

	public void close() throws org.asam.ods.AoException
	{
_delegate.close();
	}

}
