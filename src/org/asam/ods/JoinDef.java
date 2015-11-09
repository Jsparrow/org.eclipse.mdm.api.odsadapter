package org.asam.ods;

/**
 * Generated from IDL struct "JoinDef".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class JoinDef
	implements org.omg.CORBA.portable.IDLEntity
{
	public JoinDef(){}
	public org.asam.ods.T_LONGLONG fromAID;
	public org.asam.ods.T_LONGLONG toAID;
	public java.lang.String refName;
	public org.asam.ods.JoinType joiningType;
	public JoinDef(org.asam.ods.T_LONGLONG fromAID, org.asam.ods.T_LONGLONG toAID, java.lang.String refName, org.asam.ods.JoinType joiningType)
	{
		this.fromAID = fromAID;
		this.toAID = toAID;
		this.refName = refName;
		this.joiningType = joiningType;
	}
}
