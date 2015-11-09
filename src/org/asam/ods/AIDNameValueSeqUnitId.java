package org.asam.ods;

/**
 * Generated from IDL struct "AIDNameValueSeqUnitId".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class AIDNameValueSeqUnitId
	implements org.omg.CORBA.portable.IDLEntity
{
	public AIDNameValueSeqUnitId(){}
	public org.asam.ods.AIDName attr;
	public org.asam.ods.T_LONGLONG unitId;
	public org.asam.ods.TS_ValueSeq values;
	public AIDNameValueSeqUnitId(org.asam.ods.AIDName attr, org.asam.ods.T_LONGLONG unitId, org.asam.ods.TS_ValueSeq values)
	{
		this.attr = attr;
		this.unitId = unitId;
		this.values = values;
	}
}
