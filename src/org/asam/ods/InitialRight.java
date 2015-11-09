package org.asam.ods;

/**
 * Generated from IDL struct "InitialRight".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class InitialRight
	implements org.omg.CORBA.portable.IDLEntity
{
	public InitialRight(){}
	public int rights;
	public org.asam.ods.T_LONGLONG usergroupId;
	public org.asam.ods.T_LONGLONG refAid;
	public InitialRight(int rights, org.asam.ods.T_LONGLONG usergroupId, org.asam.ods.T_LONGLONG refAid)
	{
		this.rights = rights;
		this.usergroupId = usergroupId;
		this.refAid = refAid;
	}
}
