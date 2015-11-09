package org.asam.ods;

/**
 * Generated from IDL struct "NameValueUnit".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class NameValueUnit
	implements org.omg.CORBA.portable.IDLEntity
{
	public NameValueUnit(){}
	public java.lang.String valName;
	public org.asam.ods.TS_Value value;
	public java.lang.String unit;
	public NameValueUnit(java.lang.String valName, org.asam.ods.TS_Value value, java.lang.String unit)
	{
		this.valName = valName;
		this.value = value;
		this.unit = unit;
	}
}
