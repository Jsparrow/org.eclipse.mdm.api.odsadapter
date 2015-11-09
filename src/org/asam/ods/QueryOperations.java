package org.asam.ods;


/**
 * Generated from IDL interface "Query".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public interface QueryOperations
{
	/* constants */
	/* operations  */
	org.asam.ods.QueryEvaluator getQueryEvaluator() throws org.asam.ods.AoException;
	void prepareQuery(org.asam.ods.NameValue[] params) throws org.asam.ods.AoException;
	void executeQuery(org.asam.ods.NameValue[] params) throws org.asam.ods.AoException;
	org.asam.ods.QueryStatus getStatus() throws org.asam.ods.AoException;
	org.asam.ods.InstanceElementIterator getInstances() throws org.asam.ods.AoException;
	org.asam.ods.NameValueUnitSequenceIterator getTableRows() throws org.asam.ods.AoException;
	org.asam.ods.NameValueSeqUnit[] getTable() throws org.asam.ods.AoException;
}
