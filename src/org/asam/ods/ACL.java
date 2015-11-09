package org.asam.ods;

/**
 * Generated from IDL struct "ACL".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class ACL
	implements org.omg.CORBA.portable.IDLEntity
{
	public ACL(){}
	public org.asam.ods.T_LONGLONG usergroupId;
	public int rights;
	public ACL(org.asam.ods.T_LONGLONG usergroupId, int rights)
	{
		this.usergroupId = usergroupId;
		this.rights = rights;
	}
}
