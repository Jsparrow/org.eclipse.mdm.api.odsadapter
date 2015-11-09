package org.asam.ods;

/**
 * Generated from IDL struct "ApplRel".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class ApplRel
	implements org.omg.CORBA.portable.IDLEntity
{
	public ApplRel(){}
	public org.asam.ods.T_LONGLONG elem1;
	public org.asam.ods.T_LONGLONG elem2;
	public java.lang.String arName;
	public java.lang.String invName;
	public java.lang.String brName;
	public java.lang.String invBrName;
	public org.asam.ods.RelationType arRelationType;
	public org.asam.ods.RelationRange arRelationRange;
	public org.asam.ods.RelationRange invRelationRange;
	public ApplRel(org.asam.ods.T_LONGLONG elem1, org.asam.ods.T_LONGLONG elem2, java.lang.String arName, java.lang.String invName, java.lang.String brName, java.lang.String invBrName, org.asam.ods.RelationType arRelationType, org.asam.ods.RelationRange arRelationRange, org.asam.ods.RelationRange invRelationRange)
	{
		this.elem1 = elem1;
		this.elem2 = elem2;
		this.arName = arName;
		this.invName = invName;
		this.brName = brName;
		this.invBrName = invBrName;
		this.arRelationType = arRelationType;
		this.arRelationRange = arRelationRange;
		this.invRelationRange = invRelationRange;
	}
}
