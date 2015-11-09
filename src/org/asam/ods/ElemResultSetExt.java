package org.asam.ods;

/**
 * Generated from IDL struct "ElemResultSetExt".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class ElemResultSetExt
	implements org.omg.CORBA.portable.IDLEntity
{
	public ElemResultSetExt(){}
	public org.asam.ods.T_LONGLONG aid;
	public org.asam.ods.NameValueSeqUnitId[] values;
	public ElemResultSetExt(org.asam.ods.T_LONGLONG aid, org.asam.ods.NameValueSeqUnitId[] values)
	{
		this.aid = aid;
		this.values = values;
	}
}
