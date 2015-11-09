package org.asam.ods;


/**
 * Generated from IDL interface "BaseAttribute".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public interface BaseAttributeOperations
{
	/* constants */
	/* operations  */
	org.asam.ods.DataType getDataType() throws org.asam.ods.AoException;
	java.lang.String getName() throws org.asam.ods.AoException;
	boolean isObligatory() throws org.asam.ods.AoException;
	boolean isUnique() throws org.asam.ods.AoException;
	org.asam.ods.BaseElement getBaseElement() throws org.asam.ods.AoException;
	org.asam.ods.EnumerationDefinition getEnumerationDefinition() throws org.asam.ods.AoException;
}
