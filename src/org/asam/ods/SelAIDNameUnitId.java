package org.asam.ods;

/**
 * Generated from IDL struct "SelAIDNameUnitId".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class SelAIDNameUnitId
	implements org.omg.CORBA.portable.IDLEntity
{
	public SelAIDNameUnitId(){}
	public org.asam.ods.AIDName attr;
	public org.asam.ods.T_LONGLONG unitId;
	public org.asam.ods.AggrFunc aggregate;
	public SelAIDNameUnitId(org.asam.ods.AIDName attr, org.asam.ods.T_LONGLONG unitId, org.asam.ods.AggrFunc aggregate)
	{
		this.attr = attr;
		this.unitId = unitId;
		this.aggregate = aggregate;
	}
}
