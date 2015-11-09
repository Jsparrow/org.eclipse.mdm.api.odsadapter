package org.asam.ods;

import org.omg.PortableServer.POA;

/**
 * Generated from IDL interface "NameValueUnitIdIterator".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public class NameValueUnitIdIteratorPOATie
	extends NameValueUnitIdIteratorPOA
{
	private NameValueUnitIdIteratorOperations _delegate;

	private POA _poa;
	public NameValueUnitIdIteratorPOATie(NameValueUnitIdIteratorOperations delegate)
	{
		_delegate = delegate;
	}
	public NameValueUnitIdIteratorPOATie(NameValueUnitIdIteratorOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.asam.ods.NameValueUnitIdIterator _this()
	{
		return org.asam.ods.NameValueUnitIdIteratorHelper.narrow(_this_object());
	}
	public org.asam.ods.NameValueUnitIdIterator _this(org.omg.CORBA.ORB orb)
	{
		return org.asam.ods.NameValueUnitIdIteratorHelper.narrow(_this_object(orb));
	}
	public NameValueUnitIdIteratorOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(NameValueUnitIdIteratorOperations delegate)
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
	public org.asam.ods.NameValueUnitId nextOne() throws org.asam.ods.AoException
	{
		return _delegate.nextOne();
	}

	public org.asam.ods.NameValueSeqUnitId nextN(int how_many) throws org.asam.ods.AoException
	{
		return _delegate.nextN(how_many);
	}

	public void reset() throws org.asam.ods.AoException
	{
_delegate.reset();
	}

	public int getCount() throws org.asam.ods.AoException
	{
		return _delegate.getCount();
	}

	public void destroy() throws org.asam.ods.AoException
	{
_delegate.destroy();
	}

}
