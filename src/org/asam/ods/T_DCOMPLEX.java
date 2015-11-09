package org.asam.ods;

/**
 * Generated from IDL struct "T_DCOMPLEX".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class T_DCOMPLEX
	implements org.omg.CORBA.portable.IDLEntity
{
	public T_DCOMPLEX(){}
	public double r;
	public double i;
	public T_DCOMPLEX(double r, double i)
	{
		this.r = r;
		this.i = i;
	}
}
