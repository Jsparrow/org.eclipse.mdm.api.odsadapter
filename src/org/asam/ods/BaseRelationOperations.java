package org.asam.ods;


/**
 * Generated from IDL interface "BaseRelation".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public interface BaseRelationOperations
{
	/* constants */
	/* operations  */
	org.asam.ods.BaseElement getElem1() throws org.asam.ods.AoException;
	org.asam.ods.BaseElement getElem2() throws org.asam.ods.AoException;
	org.asam.ods.RelationRange getInverseRelationRange() throws org.asam.ods.AoException;
	org.asam.ods.Relationship getInverseRelationship() throws org.asam.ods.AoException;
	java.lang.String getRelationName() throws org.asam.ods.AoException;
	org.asam.ods.RelationRange getRelationRange() throws org.asam.ods.AoException;
	org.asam.ods.Relationship getRelationship() throws org.asam.ods.AoException;
	org.asam.ods.RelationType getRelationType() throws org.asam.ods.AoException;
	java.lang.String getInverseRelationName() throws org.asam.ods.AoException;
}
