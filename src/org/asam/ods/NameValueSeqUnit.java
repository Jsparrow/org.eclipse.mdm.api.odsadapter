package org.asam.ods;

/**
 * Generated from IDL struct "NameValueSeqUnit".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class NameValueSeqUnit
	implements org.omg.CORBA.portable.IDLEntity
{
	public NameValueSeqUnit(){}
	public java.lang.String valName;
	public org.asam.ods.TS_ValueSeq value;
	public java.lang.String unit;
	public NameValueSeqUnit(java.lang.String valName, org.asam.ods.TS_ValueSeq value, java.lang.String unit)
	{
		this.valName = valName;
		this.value = value;
		this.unit = unit;
	}
}
