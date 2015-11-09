package org.asam.ods;

/**
 * Generated from IDL struct "AIDNameUnitId".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class AIDNameUnitId
	implements org.omg.CORBA.portable.IDLEntity
{
	public AIDNameUnitId(){}
	public org.asam.ods.AIDName attr;
	public org.asam.ods.T_LONGLONG unitId;
	public AIDNameUnitId(org.asam.ods.AIDName attr, org.asam.ods.T_LONGLONG unitId)
	{
		this.attr = attr;
		this.unitId = unitId;
	}
}
