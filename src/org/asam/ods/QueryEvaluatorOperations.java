package org.asam.ods;


/**
 * Generated from IDL interface "QueryEvaluator".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public interface QueryEvaluatorOperations
{
	/* constants */
	/* operations  */
	org.asam.ods.InstanceElementIterator getInstances(java.lang.String queryStr, org.asam.ods.NameValue[] params) throws org.asam.ods.AoException;
	org.asam.ods.NameValueUnitSequenceIterator getTableRows(java.lang.String queryStr, org.asam.ods.NameValue[] params) throws org.asam.ods.AoException;
	org.asam.ods.NameValueSeqUnit[] getTable(java.lang.String queryStr, org.asam.ods.NameValue[] params) throws org.asam.ods.AoException;
	org.asam.ods.Query createQuery(java.lang.String queryStr, org.asam.ods.NameValue[] params) throws org.asam.ods.AoException;
}
