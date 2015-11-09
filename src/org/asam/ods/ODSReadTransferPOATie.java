package org.asam.ods;

import org.omg.PortableServer.POA;

/**
 * Generated from IDL interface "ODSReadTransfer".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public class ODSReadTransferPOATie
	extends ODSReadTransferPOA
{
	private ODSReadTransferOperations _delegate;

	private POA _poa;
	public ODSReadTransferPOATie(ODSReadTransferOperations delegate)
	{
		_delegate = delegate;
	}
	public ODSReadTransferPOATie(ODSReadTransferOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.asam.ods.ODSReadTransfer _this()
	{
		return org.asam.ods.ODSReadTransferHelper.narrow(_this_object());
	}
	public org.asam.ods.ODSReadTransfer _this(org.omg.CORBA.ORB orb)
	{
		return org.asam.ods.ODSReadTransferHelper.narrow(_this_object(orb));
	}
	public ODSReadTransferOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(ODSReadTransferOperations delegate)
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

	public byte[] getOctetSeq(int maxOctets) throws org.asam.ods.AoException
	{
		return _delegate.getOctetSeq(maxOctets);
	}

	public void close() throws org.asam.ods.AoException
	{
_delegate.close();
	}

	public org.asam.ods.T_LONGLONG skipOctets(org.asam.ods.T_LONGLONG numOctets) throws org.asam.ods.AoException
	{
		return _delegate.skipOctets(numOctets);
	}

}
