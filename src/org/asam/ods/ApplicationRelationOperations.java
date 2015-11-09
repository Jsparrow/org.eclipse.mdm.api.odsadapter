package org.asam.ods;


/**
 * Generated from IDL interface "ApplicationRelation".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public interface ApplicationRelationOperations
{
	/* constants */
	/* operations  */
	org.asam.ods.BaseRelation getBaseRelation() throws org.asam.ods.AoException;
	org.asam.ods.ApplicationElement getElem1() throws org.asam.ods.AoException;
	org.asam.ods.ApplicationElement getElem2() throws org.asam.ods.AoException;
	org.asam.ods.RelationRange getInverseRelationRange() throws org.asam.ods.AoException;
	org.asam.ods.Relationship getInverseRelationship() throws org.asam.ods.AoException;
	java.lang.String getRelationName() throws org.asam.ods.AoException;
	org.asam.ods.RelationRange getRelationRange() throws org.asam.ods.AoException;
	org.asam.ods.Relationship getRelationship() throws org.asam.ods.AoException;
	org.asam.ods.RelationType getRelationType() throws org.asam.ods.AoException;
	void setBaseRelation(org.asam.ods.BaseRelation baseRel) throws org.asam.ods.AoException;
	void setElem1(org.asam.ods.ApplicationElement applElem) throws org.asam.ods.AoException;
	void setElem2(org.asam.ods.ApplicationElement applElem) throws org.asam.ods.AoException;
	void setInverseRelationRange(org.asam.ods.RelationRange arRelationRange) throws org.asam.ods.AoException;
	void setRelationName(java.lang.String arName) throws org.asam.ods.AoException;
	void setRelationRange(org.asam.ods.RelationRange arRelationRange) throws org.asam.ods.AoException;
	void setRelationType(org.asam.ods.RelationType arRelationType) throws org.asam.ods.AoException;
	java.lang.String getInverseRelationName() throws org.asam.ods.AoException;
	void setInverseRelationName(java.lang.String arInvName) throws org.asam.ods.AoException;
}
