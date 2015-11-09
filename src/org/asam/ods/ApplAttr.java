package org.asam.ods;

/**
 * Generated from IDL struct "ApplAttr".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class ApplAttr
	implements org.omg.CORBA.portable.IDLEntity
{
	public ApplAttr(){}
	public java.lang.String aaName;
	public java.lang.String baName;
	public org.asam.ods.DataType dType;
	public int length;
	public boolean isObligatory;
	public boolean isUnique;
	public org.asam.ods.T_LONGLONG unitId;
	public ApplAttr(java.lang.String aaName, java.lang.String baName, org.asam.ods.DataType dType, int length, boolean isObligatory, boolean isUnique, org.asam.ods.T_LONGLONG unitId)
	{
		this.aaName = aaName;
		this.baName = baName;
		this.dType = dType;
		this.length = length;
		this.isObligatory = isObligatory;
		this.isUnique = isUnique;
		this.unitId = unitId;
	}
}
