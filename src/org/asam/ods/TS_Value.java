package org.asam.ods;

/**
 * Generated from IDL struct "TS_Value".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class TS_Value
	implements org.omg.CORBA.portable.IDLEntity
{
	public TS_Value(){}
	public org.asam.ods.TS_Union u;
	public short flag;
	public TS_Value(org.asam.ods.TS_Union u, short flag)
	{
		this.u = u;
		this.flag = flag;
	}
}
