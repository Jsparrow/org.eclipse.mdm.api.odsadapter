package org.asam.ods;

/**
 * Generated from IDL struct "QueryStructureExt".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class QueryStructureExt
	implements org.omg.CORBA.portable.IDLEntity
{
	public QueryStructureExt(){}
	public org.asam.ods.SelAIDNameUnitId[] anuSeq;
	public org.asam.ods.SelItem[] condSeq;
	public org.asam.ods.JoinDef[] joinSeq;
	public org.asam.ods.SelOrder[] orderBy;
	public org.asam.ods.AIDName[] groupBy;
	public QueryStructureExt(org.asam.ods.SelAIDNameUnitId[] anuSeq, org.asam.ods.SelItem[] condSeq, org.asam.ods.JoinDef[] joinSeq, org.asam.ods.SelOrder[] orderBy, org.asam.ods.AIDName[] groupBy)
	{
		this.anuSeq = anuSeq;
		this.condSeq = condSeq;
		this.joinSeq = joinSeq;
		this.orderBy = orderBy;
		this.groupBy = groupBy;
	}
}
