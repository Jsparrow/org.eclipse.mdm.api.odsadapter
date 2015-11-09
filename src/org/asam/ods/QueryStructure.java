package org.asam.ods;

/**
 * Generated from IDL struct "QueryStructure".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class QueryStructure
	implements org.omg.CORBA.portable.IDLEntity
{
	public QueryStructure(){}
	public org.asam.ods.AIDNameUnitId[] anuSeq;
	public org.asam.ods.SelValue[] condSeq;
	public org.asam.ods.SelOperator[] operSeq;
	public org.asam.ods.ElemId relInst;
	public java.lang.String relName;
	public org.asam.ods.SelOrder[] orderBy;
	public QueryStructure(org.asam.ods.AIDNameUnitId[] anuSeq, org.asam.ods.SelValue[] condSeq, org.asam.ods.SelOperator[] operSeq, org.asam.ods.ElemId relInst, java.lang.String relName, org.asam.ods.SelOrder[] orderBy)
	{
		this.anuSeq = anuSeq;
		this.condSeq = condSeq;
		this.operSeq = operSeq;
		this.relInst = relInst;
		this.relName = relName;
		this.orderBy = orderBy;
	}
}
