package org.asam.ods;

import org.omg.PortableServer.POA;

/**
 * Generated from IDL interface "QueryEvaluator".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public class QueryEvaluatorPOATie
	extends QueryEvaluatorPOA
{
	private QueryEvaluatorOperations _delegate;

	private POA _poa;
	public QueryEvaluatorPOATie(QueryEvaluatorOperations delegate)
	{
		_delegate = delegate;
	}
	public QueryEvaluatorPOATie(QueryEvaluatorOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.asam.ods.QueryEvaluator _this()
	{
		return org.asam.ods.QueryEvaluatorHelper.narrow(_this_object());
	}
	public org.asam.ods.QueryEvaluator _this(org.omg.CORBA.ORB orb)
	{
		return org.asam.ods.QueryEvaluatorHelper.narrow(_this_object(orb));
	}
	public QueryEvaluatorOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(QueryEvaluatorOperations delegate)
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
	public org.asam.ods.Query createQuery(java.lang.String queryStr, org.asam.ods.NameValue[] params) throws org.asam.ods.AoException
	{
		return _delegate.createQuery(queryStr,params);
	}

	public org.asam.ods.NameValueSeqUnit[] getTable(java.lang.String queryStr, org.asam.ods.NameValue[] params) throws org.asam.ods.AoException
	{
		return _delegate.getTable(queryStr,params);
	}

	public org.asam.ods.NameValueUnitSequenceIterator getTableRows(java.lang.String queryStr, org.asam.ods.NameValue[] params) throws org.asam.ods.AoException
	{
		return _delegate.getTableRows(queryStr,params);
	}

	public org.asam.ods.InstanceElementIterator getInstances(java.lang.String queryStr, org.asam.ods.NameValue[] params) throws org.asam.ods.AoException
	{
		return _delegate.getInstances(queryStr,params);
	}

}
