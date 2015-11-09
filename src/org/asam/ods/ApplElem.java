package org.asam.ods;

/**
 * Generated from IDL struct "ApplElem".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class ApplElem
	implements org.omg.CORBA.portable.IDLEntity
{
	public ApplElem(){}
	public org.asam.ods.T_LONGLONG aid;
	public java.lang.String beName;
	public java.lang.String aeName;
	public org.asam.ods.ApplAttr[] attributes;
	public ApplElem(org.asam.ods.T_LONGLONG aid, java.lang.String beName, java.lang.String aeName, org.asam.ods.ApplAttr[] attributes)
	{
		this.aid = aid;
		this.beName = beName;
		this.aeName = aeName;
		this.attributes = attributes;
	}
}
