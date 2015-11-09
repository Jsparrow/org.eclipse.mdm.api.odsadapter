package org.asam.ods;

import org.omg.PortableServer.POA;

/**
 * Generated from IDL interface "ApplicationAttribute".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public class ApplicationAttributePOATie
	extends ApplicationAttributePOA
{
	private ApplicationAttributeOperations _delegate;

	private POA _poa;
	public ApplicationAttributePOATie(ApplicationAttributeOperations delegate)
	{
		_delegate = delegate;
	}
	public ApplicationAttributePOATie(ApplicationAttributeOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.asam.ods.ApplicationAttribute _this()
	{
		return org.asam.ods.ApplicationAttributeHelper.narrow(_this_object());
	}
	public org.asam.ods.ApplicationAttribute _this(org.omg.CORBA.ORB orb)
	{
		return org.asam.ods.ApplicationAttributeHelper.narrow(_this_object(orb));
	}
	public ApplicationAttributeOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(ApplicationAttributeOperations delegate)
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
	public void setBaseAttribute(org.asam.ods.BaseAttribute baseAttr) throws org.asam.ods.AoException
	{
_delegate.setBaseAttribute(baseAttr);
	}

	public void setLength(int aaLength) throws org.asam.ods.AoException
	{
_delegate.setLength(aaLength);
	}

	public java.lang.String getName() throws org.asam.ods.AoException
	{
		return _delegate.getName();
	}

	public void setRights(org.asam.ods.InstanceElement usergroup, int rights, org.asam.ods.RightsSet set) throws org.asam.ods.AoException
	{
_delegate.setRights(usergroup,rights,set);
	}

	public void setUnit(org.asam.ods.T_LONGLONG aaUnit) throws org.asam.ods.AoException
	{
_delegate.setUnit(aaUnit);
	}

	public boolean isUnique() throws org.asam.ods.AoException
	{
		return _delegate.isUnique();
	}

	public boolean hasUnit() throws org.asam.ods.AoException
	{
		return _delegate.hasUnit();
	}

	public void setIsUnique(boolean aaIsUnique) throws org.asam.ods.AoException
	{
_delegate.setIsUnique(aaIsUnique);
	}

	public org.asam.ods.ApplicationElement getApplicationElement() throws org.asam.ods.AoException
	{
		return _delegate.getApplicationElement();
	}

	public org.asam.ods.BaseAttribute getBaseAttribute() throws org.asam.ods.AoException
	{
		return _delegate.getBaseAttribute();
	}

	public void withValueFlag(boolean withValueFlag) throws org.asam.ods.AoException
	{
_delegate.withValueFlag(withValueFlag);
	}

	public boolean isObligatory() throws org.asam.ods.AoException
	{
		return _delegate.isObligatory();
	}

	public int getLength() throws org.asam.ods.AoException
	{
		return _delegate.getLength();
	}

	public org.asam.ods.ACL[] getRights() throws org.asam.ods.AoException
	{
		return _delegate.getRights();
	}

	public boolean hasValueFlag() throws org.asam.ods.AoException
	{
		return _delegate.hasValueFlag();
	}

	public void setName(java.lang.String aaName) throws org.asam.ods.AoException
	{
_delegate.setName(aaName);
	}

	public org.asam.ods.EnumerationDefinition getEnumerationDefinition() throws org.asam.ods.AoException
	{
		return _delegate.getEnumerationDefinition();
	}

	public org.asam.ods.DataType getDataType() throws org.asam.ods.AoException
	{
		return _delegate.getDataType();
	}

	public boolean isAutogenerated() throws org.asam.ods.AoException
	{
		return _delegate.isAutogenerated();
	}

	public org.asam.ods.T_LONGLONG getUnit() throws org.asam.ods.AoException
	{
		return _delegate.getUnit();
	}

	public void withUnit(boolean withUnit) throws org.asam.ods.AoException
	{
_delegate.withUnit(withUnit);
	}

	public void setDataType(org.asam.ods.DataType aaDataType) throws org.asam.ods.AoException
	{
_delegate.setDataType(aaDataType);
	}

	public void setEnumerationDefinition(org.asam.ods.EnumerationDefinition enumDef) throws org.asam.ods.AoException
	{
_delegate.setEnumerationDefinition(enumDef);
	}

	public void setIsAutogenerated(boolean isAutogenerated) throws org.asam.ods.AoException
	{
_delegate.setIsAutogenerated(isAutogenerated);
	}

	public void setIsObligatory(boolean aaIsObligatory) throws org.asam.ods.AoException
	{
_delegate.setIsObligatory(aaIsObligatory);
	}

}
