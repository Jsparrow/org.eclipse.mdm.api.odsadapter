package org.asam.ods;


/**
 * Generated from IDL interface "BaseElement".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public interface BaseElementOperations
{
	/* constants */
	/* operations  */
	org.asam.ods.BaseRelation[] getAllRelations() throws org.asam.ods.AoException;
	org.asam.ods.BaseAttribute[] getAttributes(java.lang.String baPattern) throws org.asam.ods.AoException;
	org.asam.ods.BaseElement[] getRelatedElementsByRelationship(org.asam.ods.Relationship brRelationship) throws org.asam.ods.AoException;
	org.asam.ods.BaseRelation[] getRelationsByType(org.asam.ods.RelationType brRelationType) throws org.asam.ods.AoException;
	java.lang.String getType() throws org.asam.ods.AoException;
	boolean isTopLevel() throws org.asam.ods.AoException;
	java.lang.String[] listAttributes(java.lang.String baPattern) throws org.asam.ods.AoException;
	java.lang.String[] listRelatedElementsByRelationship(org.asam.ods.Relationship brRelationship) throws org.asam.ods.AoException;
}
