package org.asam.ods;


/**
 * Generated from IDL interface "ApplicationStructure".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public interface ApplicationStructureOperations
{
	/* constants */
	/* operations  */
	void check() throws org.asam.ods.AoException;
	org.asam.ods.ApplicationElement createElement(org.asam.ods.BaseElement baseElem) throws org.asam.ods.AoException;
	org.asam.ods.ApplicationRelation createRelation() throws org.asam.ods.AoException;
	org.asam.ods.ApplicationElement getElementById(org.asam.ods.T_LONGLONG aeId) throws org.asam.ods.AoException;
	org.asam.ods.ApplicationElement getElementByName(java.lang.String aeName) throws org.asam.ods.AoException;
	org.asam.ods.ApplicationElement[] getElements(java.lang.String aePattern) throws org.asam.ods.AoException;
	org.asam.ods.ApplicationElement[] getElementsByBaseType(java.lang.String aeType) throws org.asam.ods.AoException;
	org.asam.ods.InstanceElement getInstanceByAsamPath(java.lang.String asamPath) throws org.asam.ods.AoException;
	org.asam.ods.ApplicationRelation[] getRelations(org.asam.ods.ApplicationElement applElem1, org.asam.ods.ApplicationElement applElem2) throws org.asam.ods.AoException;
	org.asam.ods.ApplicationElement[] getTopLevelElements(java.lang.String aeType) throws org.asam.ods.AoException;
	java.lang.String[] listElements(java.lang.String aePattern) throws org.asam.ods.AoException;
	java.lang.String[] listElementsByBaseType(java.lang.String aeType) throws org.asam.ods.AoException;
	java.lang.String[] listTopLevelElements(java.lang.String aeType) throws org.asam.ods.AoException;
	void removeElement(org.asam.ods.ApplicationElement applElem) throws org.asam.ods.AoException;
	void removeRelation(org.asam.ods.ApplicationRelation applRel) throws org.asam.ods.AoException;
	org.asam.ods.InstanceElement[] getInstancesById(org.asam.ods.ElemId[] ieIds) throws org.asam.ods.AoException;
	org.asam.ods.AoSession getSession() throws org.asam.ods.AoException;
	org.asam.ods.EnumerationDefinition createEnumerationDefinition(java.lang.String enumName) throws org.asam.ods.AoException;
	void removeEnumerationDefinition(java.lang.String enumName) throws org.asam.ods.AoException;
	java.lang.String[] listEnumerations() throws org.asam.ods.AoException;
	org.asam.ods.EnumerationDefinition getEnumerationDefinition(java.lang.String enumName) throws org.asam.ods.AoException;
	void createInstanceRelations(org.asam.ods.ApplicationRelation applRel, org.asam.ods.InstanceElement[] elemList1, org.asam.ods.InstanceElement[] elemList2) throws org.asam.ods.AoException;
}
