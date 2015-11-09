package org.asam.ods;

/**
 * Generated from IDL struct "TS_ValueSeq".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class TS_ValueSeq
	implements org.omg.CORBA.portable.IDLEntity
{
	public TS_ValueSeq(){}
	public org.asam.ods.TS_UnionSeq u;
	public short[] flag;
	public TS_ValueSeq(org.asam.ods.TS_UnionSeq u, short[] flag)
	{
		this.u = u;
		this.flag = flag;
	}
}
