package org.asam.ods;

/**
 * Generated from IDL struct "NameValueUnitId".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class NameValueUnitId
	implements org.omg.CORBA.portable.IDLEntity
{
	public NameValueUnitId(){}
	public java.lang.String valName;
	public org.asam.ods.TS_Value value;
	public org.asam.ods.T_LONGLONG unitId;
	public NameValueUnitId(java.lang.String valName, org.asam.ods.TS_Value value, org.asam.ods.T_LONGLONG unitId)
	{
		this.valName = valName;
		this.value = value;
		this.unitId = unitId;
	}
}
