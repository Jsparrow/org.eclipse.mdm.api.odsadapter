package org.asam.ods;

/**
 * Generated from IDL struct "AIDName".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class AIDName
	implements org.omg.CORBA.portable.IDLEntity
{
	public AIDName(){}
	public org.asam.ods.T_LONGLONG aid;
	public java.lang.String aaName;
	public AIDName(org.asam.ods.T_LONGLONG aid, java.lang.String aaName)
	{
		this.aid = aid;
		this.aaName = aaName;
	}
}
