package org.asam.ods;

/**
 * Generated from IDL struct "T_LONGLONG".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class T_LONGLONG
	implements org.omg.CORBA.portable.IDLEntity
{
	public T_LONGLONG(){}
	public int high;
	public int low;
	public T_LONGLONG(int high, int low)
	{
		this.high = high;
		this.low = low;
	}
}
