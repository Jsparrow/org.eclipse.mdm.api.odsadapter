package org.asam.ods;


/**
 * Generated from IDL interface "AoSession".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public interface AoSessionOperations
{
	/* constants */
	/* operations  */
	void abortTransaction() throws org.asam.ods.AoException;
	void close() throws org.asam.ods.AoException;
	void commitTransaction() throws org.asam.ods.AoException;
	org.asam.ods.ApplicationStructure getApplicationStructure() throws org.asam.ods.AoException;
	org.asam.ods.ApplicationStructureValue getApplicationStructureValue() throws org.asam.ods.AoException;
	org.asam.ods.BaseStructure getBaseStructure() throws org.asam.ods.AoException;
	org.asam.ods.NameValueIterator getContext(java.lang.String varPattern) throws org.asam.ods.AoException;
	org.asam.ods.NameValue getContextByName(java.lang.String varName) throws org.asam.ods.AoException;
	org.asam.ods.NameIterator listContext(java.lang.String varPattern) throws org.asam.ods.AoException;
	void removeContext(java.lang.String varPattern) throws org.asam.ods.AoException;
	void setContext(org.asam.ods.NameValue contextVariable) throws org.asam.ods.AoException;
	void setContextString(java.lang.String varName, java.lang.String value) throws org.asam.ods.AoException;
	void startTransaction() throws org.asam.ods.AoException;
	void flush() throws org.asam.ods.AoException;
	void setCurrentInitialRights(org.asam.ods.InitialRight[] irlEntries, boolean set) throws org.asam.ods.AoException;
	short getLockMode() throws org.asam.ods.AoException;
	void setLockMode(short lockMode) throws org.asam.ods.AoException;
	org.asam.ods.ApplElemAccess getApplElemAccess() throws org.asam.ods.AoException;
	void setPassword(java.lang.String username, java.lang.String oldPassword, java.lang.String newPassword) throws org.asam.ods.AoException;
	java.lang.String getDescription() throws org.asam.ods.AoException;
	java.lang.String getName() throws org.asam.ods.AoException;
	java.lang.String getType() throws org.asam.ods.AoException;
	org.asam.ods.QueryEvaluator createQueryEvaluator() throws org.asam.ods.AoException;
	org.asam.ods.Blob createBlob() throws org.asam.ods.AoException;
	org.asam.ods.AoSession createCoSession() throws org.asam.ods.AoException;
	org.asam.ods.InstanceElement getUser() throws org.asam.ods.AoException;
	org.asam.ods.EnumerationAttributeStructure[] getEnumerationAttributes() throws org.asam.ods.AoException;
	org.asam.ods.EnumerationStructure[] getEnumerationStructure() throws org.asam.ods.AoException;
	int getId() throws org.asam.ods.AoException;
}
