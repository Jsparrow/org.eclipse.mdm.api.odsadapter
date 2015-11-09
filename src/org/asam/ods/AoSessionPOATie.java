package org.asam.ods;

import org.omg.PortableServer.POA;

/**
 * Generated from IDL interface "AoSession".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public class AoSessionPOATie
	extends AoSessionPOA
{
	private AoSessionOperations _delegate;

	private POA _poa;
	public AoSessionPOATie(AoSessionOperations delegate)
	{
		_delegate = delegate;
	}
	public AoSessionPOATie(AoSessionOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.asam.ods.AoSession _this()
	{
		return org.asam.ods.AoSessionHelper.narrow(_this_object());
	}
	public org.asam.ods.AoSession _this(org.omg.CORBA.ORB orb)
	{
		return org.asam.ods.AoSessionHelper.narrow(_this_object(orb));
	}
	public AoSessionOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(AoSessionOperations delegate)
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
	public void commitTransaction() throws org.asam.ods.AoException
	{
_delegate.commitTransaction();
	}

	public org.asam.ods.EnumerationStructure[] getEnumerationStructure() throws org.asam.ods.AoException
	{
		return _delegate.getEnumerationStructure();
	}

	public org.asam.ods.EnumerationAttributeStructure[] getEnumerationAttributes() throws org.asam.ods.AoException
	{
		return _delegate.getEnumerationAttributes();
	}

	public org.asam.ods.InstanceElement getUser() throws org.asam.ods.AoException
	{
		return _delegate.getUser();
	}

	public java.lang.String getName() throws org.asam.ods.AoException
	{
		return _delegate.getName();
	}

	public java.lang.String getType() throws org.asam.ods.AoException
	{
		return _delegate.getType();
	}

	public org.asam.ods.NameValueIterator getContext(java.lang.String varPattern) throws org.asam.ods.AoException
	{
		return _delegate.getContext(varPattern);
	}

	public void setContext(org.asam.ods.NameValue contextVariable) throws org.asam.ods.AoException
	{
_delegate.setContext(contextVariable);
	}

	public short getLockMode() throws org.asam.ods.AoException
	{
		return _delegate.getLockMode();
	}

	public void setLockMode(short lockMode) throws org.asam.ods.AoException
	{
_delegate.setLockMode(lockMode);
	}

	public int getId() throws org.asam.ods.AoException
	{
		return _delegate.getId();
	}

	public void startTransaction() throws org.asam.ods.AoException
	{
_delegate.startTransaction();
	}

	public void flush() throws org.asam.ods.AoException
	{
_delegate.flush();
	}

	public java.lang.String getDescription() throws org.asam.ods.AoException
	{
		return _delegate.getDescription();
	}

	public void setPassword(java.lang.String username, java.lang.String oldPassword, java.lang.String newPassword) throws org.asam.ods.AoException
	{
_delegate.setPassword(username,oldPassword,newPassword);
	}

	public org.asam.ods.AoSession createCoSession() throws org.asam.ods.AoException
	{
		return _delegate.createCoSession();
	}

	public void close() throws org.asam.ods.AoException
	{
_delegate.close();
	}

	public org.asam.ods.QueryEvaluator createQueryEvaluator() throws org.asam.ods.AoException
	{
		return _delegate.createQueryEvaluator();
	}

	public org.asam.ods.ApplElemAccess getApplElemAccess() throws org.asam.ods.AoException
	{
		return _delegate.getApplElemAccess();
	}

	public org.asam.ods.BaseStructure getBaseStructure() throws org.asam.ods.AoException
	{
		return _delegate.getBaseStructure();
	}

	public void setContextString(java.lang.String varName, java.lang.String value) throws org.asam.ods.AoException
	{
_delegate.setContextString(varName,value);
	}

	public org.asam.ods.NameValue getContextByName(java.lang.String varName) throws org.asam.ods.AoException
	{
		return _delegate.getContextByName(varName);
	}

	public org.asam.ods.ApplicationStructure getApplicationStructure() throws org.asam.ods.AoException
	{
		return _delegate.getApplicationStructure();
	}

	public org.asam.ods.Blob createBlob() throws org.asam.ods.AoException
	{
		return _delegate.createBlob();
	}

	public org.asam.ods.ApplicationStructureValue getApplicationStructureValue() throws org.asam.ods.AoException
	{
		return _delegate.getApplicationStructureValue();
	}

	public void setCurrentInitialRights(org.asam.ods.InitialRight[] irlEntries, boolean set) throws org.asam.ods.AoException
	{
_delegate.setCurrentInitialRights(irlEntries,set);
	}

	public org.asam.ods.NameIterator listContext(java.lang.String varPattern) throws org.asam.ods.AoException
	{
		return _delegate.listContext(varPattern);
	}

	public void removeContext(java.lang.String varPattern) throws org.asam.ods.AoException
	{
_delegate.removeContext(varPattern);
	}

	public void abortTransaction() throws org.asam.ods.AoException
	{
_delegate.abortTransaction();
	}

}
