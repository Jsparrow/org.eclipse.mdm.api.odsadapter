package org.asam.ods;


/**
 * Generated from IDL interface "InstanceElement".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public interface InstanceElementOperations
{
	/* constants */
	/* operations  */
	void addInstanceAttribute(org.asam.ods.NameValueUnit instAttr) throws org.asam.ods.AoException;
	void createRelation(org.asam.ods.ApplicationRelation relation, org.asam.ods.InstanceElement instElem) throws org.asam.ods.AoException;
	org.asam.ods.ApplicationElement getApplicationElement() throws org.asam.ods.AoException;
	java.lang.String getAsamPath() throws org.asam.ods.AoException;
	org.asam.ods.T_LONGLONG getId() throws org.asam.ods.AoException;
	java.lang.String getName() throws org.asam.ods.AoException;
	org.asam.ods.InstanceElementIterator getRelatedInstances(org.asam.ods.ApplicationRelation applRel, java.lang.String iePattern) throws org.asam.ods.AoException;
	org.asam.ods.InstanceElementIterator getRelatedInstancesByRelationship(org.asam.ods.Relationship ieRelationship, java.lang.String iePattern) throws org.asam.ods.AoException;
	org.asam.ods.NameValueUnit getValue(java.lang.String attrName) throws org.asam.ods.AoException;
	org.asam.ods.NameValueUnit getValueByBaseName(java.lang.String baseAttrName) throws org.asam.ods.AoException;
	java.lang.String[] listAttributes(java.lang.String iaPattern, org.asam.ods.AttrType aType) throws org.asam.ods.AoException;
	org.asam.ods.NameIterator listRelatedInstances(org.asam.ods.ApplicationRelation ieRelation, java.lang.String iePattern) throws org.asam.ods.AoException;
	org.asam.ods.NameIterator listRelatedInstancesByRelationship(org.asam.ods.Relationship ieRelationship, java.lang.String iePattern) throws org.asam.ods.AoException;
	void removeInstanceAttribute(java.lang.String attrName) throws org.asam.ods.AoException;
	void removeRelation(org.asam.ods.ApplicationRelation applRel, org.asam.ods.InstanceElement instElem_nm) throws org.asam.ods.AoException;
	void renameInstanceAttribute(java.lang.String oldName, java.lang.String newName) throws org.asam.ods.AoException;
	void setName(java.lang.String iaName) throws org.asam.ods.AoException;
	void setValue(org.asam.ods.NameValueUnit value) throws org.asam.ods.AoException;
	org.asam.ods.Measurement upcastMeasurement() throws org.asam.ods.AoException;
	org.asam.ods.SubMatrix upcastSubMatrix() throws org.asam.ods.AoException;
	org.asam.ods.NameValueUnit getValueInUnit(org.asam.ods.NameUnit attr) throws org.asam.ods.AoException;
	void setValueSeq(org.asam.ods.NameValueUnit[] values) throws org.asam.ods.AoException;
	void setRights(org.asam.ods.InstanceElement usergroup, int rights, org.asam.ods.RightsSet set) throws org.asam.ods.AoException;
	org.asam.ods.ACL[] getRights() throws org.asam.ods.AoException;
	org.asam.ods.InitialRight[] getInitialRights() throws org.asam.ods.AoException;
	void setInitialRights(org.asam.ods.InstanceElement usergroup, int rights, org.asam.ods.T_LONGLONG refAid, org.asam.ods.RightsSet set) throws org.asam.ods.AoException;
	org.asam.ods.InstanceElement shallowCopy(java.lang.String newName, java.lang.String newVersion) throws org.asam.ods.AoException;
	org.asam.ods.InstanceElement deepCopy(java.lang.String newName, java.lang.String newVersion) throws org.asam.ods.AoException;
	org.asam.ods.NameValueUnit[] getValueSeq(java.lang.String[] attrNames) throws org.asam.ods.AoException;
	void destroy() throws org.asam.ods.AoException;
	org.asam.ods.T_LONGLONG compare(org.asam.ods.InstanceElement compIeObj) throws org.asam.ods.AoException;
	org.asam.ods.InstanceElement[] createRelatedInstances(org.asam.ods.ApplicationRelation applRel, org.asam.ods.NameValueSeqUnit[] attributes, org.asam.ods.ApplicationRelationInstanceElementSeq[] relatedInstances) throws org.asam.ods.AoException;
	org.asam.ods.ODSFile upcastODSFile() throws org.asam.ods.AoException;
}
