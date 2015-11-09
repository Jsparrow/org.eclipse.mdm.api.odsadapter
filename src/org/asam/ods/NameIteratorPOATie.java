package org.asam.ods;

import org.omg.PortableServer.POA;

/**
 * Generated from IDL interface "NameIterator".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public class NameIteratorPOATie
	extends NameIteratorPOA
{
	private NameIteratorOperations _delegate;

	private POA _poa;
	public NameIteratorPOATie(NameIteratorOperations delegate)
	{
		_delegate = delegate;
	}
	public NameIteratorPOATie(NameIteratorOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.asam.ods.NameIterator _this()
	{
		return org.asam.ods.NameIteratorHelper.narrow(_this_object());
	}
	public org.asam.ods.NameIterator _this(org.omg.CORBA.ORB orb)
	{
		return org.asam.ods.NameIteratorHelper.narrow(_this_object(orb));
	}
	public NameIteratorOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(NameIteratorOperations delegate)
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
	public java.lang.String nextOne() throws org.asam.ods.AoException
	{
		return _delegate.nextOne();
	}

	public java.lang.String[] nextN(int how_many) throws org.asam.ods.AoException
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
