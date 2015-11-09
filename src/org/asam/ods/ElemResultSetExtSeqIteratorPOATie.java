package org.asam.ods;

import org.omg.PortableServer.POA;

/**
 * Generated from IDL interface "ElemResultSetExtSeqIterator".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public class ElemResultSetExtSeqIteratorPOATie
	extends ElemResultSetExtSeqIteratorPOA
{
	private ElemResultSetExtSeqIteratorOperations _delegate;

	private POA _poa;
	public ElemResultSetExtSeqIteratorPOATie(ElemResultSetExtSeqIteratorOperations delegate)
	{
		_delegate = delegate;
	}
	public ElemResultSetExtSeqIteratorPOATie(ElemResultSetExtSeqIteratorOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.asam.ods.ElemResultSetExtSeqIterator _this()
	{
		return org.asam.ods.ElemResultSetExtSeqIteratorHelper.narrow(_this_object());
	}
	public org.asam.ods.ElemResultSetExtSeqIterator _this(org.omg.CORBA.ORB orb)
	{
		return org.asam.ods.ElemResultSetExtSeqIteratorHelper.narrow(_this_object(orb));
	}
	public ElemResultSetExtSeqIteratorOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(ElemResultSetExtSeqIteratorOperations delegate)
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
	public org.asam.ods.ElemResultSetExt nextOne() throws org.asam.ods.AoException
	{
		return _delegate.nextOne();
	}

	public org.asam.ods.ElemResultSetExt[] nextN(int how_many) throws org.asam.ods.AoException
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
