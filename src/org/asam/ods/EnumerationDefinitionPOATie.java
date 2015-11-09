package org.asam.ods;

import org.omg.PortableServer.POA;

/**
 * Generated from IDL interface "EnumerationDefinition".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public class EnumerationDefinitionPOATie
	extends EnumerationDefinitionPOA
{
	private EnumerationDefinitionOperations _delegate;

	private POA _poa;
	public EnumerationDefinitionPOATie(EnumerationDefinitionOperations delegate)
	{
		_delegate = delegate;
	}
	public EnumerationDefinitionPOATie(EnumerationDefinitionOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.asam.ods.EnumerationDefinition _this()
	{
		return org.asam.ods.EnumerationDefinitionHelper.narrow(_this_object());
	}
	public org.asam.ods.EnumerationDefinition _this(org.omg.CORBA.ORB orb)
	{
		return org.asam.ods.EnumerationDefinitionHelper.narrow(_this_object(orb));
	}
	public EnumerationDefinitionOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(EnumerationDefinitionOperations delegate)
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
	public int getIndex() throws org.asam.ods.AoException
	{
		return _delegate.getIndex();
	}

	public void setName(java.lang.String enumName) throws org.asam.ods.AoException
	{
_delegate.setName(enumName);
	}

	public java.lang.String getItemName(int item) throws org.asam.ods.AoException
	{
		return _delegate.getItemName(item);
	}

	public void renameItem(java.lang.String oldItemName, java.lang.String newItemName) throws org.asam.ods.AoException
	{
_delegate.renameItem(oldItemName,newItemName);
	}

	public java.lang.String[] listItemNames() throws org.asam.ods.AoException
	{
		return _delegate.listItemNames();
	}

	public int getItem(java.lang.String itemName) throws org.asam.ods.AoException
	{
		return _delegate.getItem(itemName);
	}

	public void addItem(java.lang.String itemName) throws org.asam.ods.AoException
	{
_delegate.addItem(itemName);
	}

	public java.lang.String getName() throws org.asam.ods.AoException
	{
		return _delegate.getName();
	}

}
