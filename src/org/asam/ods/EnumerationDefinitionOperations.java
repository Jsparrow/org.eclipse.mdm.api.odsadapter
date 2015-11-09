package org.asam.ods;


/**
 * Generated from IDL interface "EnumerationDefinition".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public interface EnumerationDefinitionOperations
{
	/* constants */
	/* operations  */
	java.lang.String[] listItemNames() throws org.asam.ods.AoException;
	int getItem(java.lang.String itemName) throws org.asam.ods.AoException;
	java.lang.String getItemName(int item) throws org.asam.ods.AoException;
	void addItem(java.lang.String itemName) throws org.asam.ods.AoException;
	void renameItem(java.lang.String oldItemName, java.lang.String newItemName) throws org.asam.ods.AoException;
	java.lang.String getName() throws org.asam.ods.AoException;
	void setName(java.lang.String enumName) throws org.asam.ods.AoException;
	int getIndex() throws org.asam.ods.AoException;
}
