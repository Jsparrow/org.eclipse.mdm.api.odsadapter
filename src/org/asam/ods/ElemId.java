package org.asam.ods;

/**
 * Generated from IDL struct "ElemId".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class ElemId
	implements org.omg.CORBA.portable.IDLEntity
{
	public ElemId(){}
	public org.asam.ods.T_LONGLONG aid;
	public org.asam.ods.T_LONGLONG iid;
	public ElemId(org.asam.ods.T_LONGLONG aid, org.asam.ods.T_LONGLONG iid)
	{
		this.aid = aid;
		this.iid = iid;
	}
}
