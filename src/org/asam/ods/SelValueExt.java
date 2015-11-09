package org.asam.ods;

/**
 * Generated from IDL struct "SelValueExt".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class SelValueExt
	implements org.omg.CORBA.portable.IDLEntity
{
	public SelValueExt(){}
	public org.asam.ods.AIDNameUnitId attr;
	public org.asam.ods.SelOpcode oper;
	public org.asam.ods.TS_Value value;
	public SelValueExt(org.asam.ods.AIDNameUnitId attr, org.asam.ods.SelOpcode oper, org.asam.ods.TS_Value value)
	{
		this.attr = attr;
		this.oper = oper;
		this.value = value;
	}
}
