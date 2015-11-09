package org.asam.ods;

import org.omg.PortableServer.POA;

/**
 * Generated from IDL interface "Query".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public class QueryPOATie
	extends QueryPOA
{
	private QueryOperations _delegate;

	private POA _poa;
	public QueryPOATie(QueryOperations delegate)
	{
		_delegate = delegate;
	}
	public QueryPOATie(QueryOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.asam.ods.Query _this()
	{
		return org.asam.ods.QueryHelper.narrow(_this_object());
	}
	public org.asam.ods.Query _this(org.omg.CORBA.ORB orb)
	{
		return org.asam.ods.QueryHelper.narrow(_this_object(orb));
	}
	public QueryOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(QueryOperations delegate)
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
	public org.asam.ods.NameValueSeqUnit[] getTable() throws org.asam.ods.AoException
	{
		return _delegate.getTable();
	}

	public org.asam.ods.InstanceElementIterator getInstances() throws org.asam.ods.AoException
	{
		return _delegate.getInstances();
	}

	public void executeQuery(org.asam.ods.NameValue[] params) throws org.asam.ods.AoException
	{
_delegate.executeQuery(params);
	}

	public org.asam.ods.QueryEvaluator getQueryEvaluator() throws org.asam.ods.AoException
	{
		return _delegate.getQueryEvaluator();
	}

	public org.asam.ods.QueryStatus getStatus() throws org.asam.ods.AoException
	{
		return _delegate.getStatus();
	}

	public void prepareQuery(org.asam.ods.NameValue[] params) throws org.asam.ods.AoException
	{
_delegate.prepareQuery(params);
	}

	public org.asam.ods.NameValueUnitSequenceIterator getTableRows() throws org.asam.ods.AoException
	{
		return _delegate.getTableRows();
	}

}
