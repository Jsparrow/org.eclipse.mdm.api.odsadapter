package org.asam.ods;


/**
 * Generated from IDL interface "BaseStructure".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public interface BaseStructureOperations
{
	/* constants */
	/* operations  */
	org.asam.ods.BaseElement getElementByType(java.lang.String beType) throws org.asam.ods.AoException;
	org.asam.ods.BaseElement[] getElements(java.lang.String bePattern) throws org.asam.ods.AoException;
	org.asam.ods.BaseRelation getRelation(org.asam.ods.BaseElement elem1, org.asam.ods.BaseElement elem2) throws org.asam.ods.AoException;
	org.asam.ods.BaseElement[] getTopLevelElements(java.lang.String bePattern) throws org.asam.ods.AoException;
	java.lang.String getVersion() throws org.asam.ods.AoException;
	java.lang.String[] listElements(java.lang.String bePattern) throws org.asam.ods.AoException;
	java.lang.String[] listTopLevelElements(java.lang.String bePattern) throws org.asam.ods.AoException;
	org.asam.ods.BaseRelation[] getRelations(org.asam.ods.BaseElement elem1, org.asam.ods.BaseElement elem2) throws org.asam.ods.AoException;
}
