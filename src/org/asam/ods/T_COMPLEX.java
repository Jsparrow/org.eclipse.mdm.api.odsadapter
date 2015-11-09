package org.asam.ods;

/**
 * Generated from IDL struct "T_COMPLEX".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class T_COMPLEX
	implements org.omg.CORBA.portable.IDLEntity
{
	public T_COMPLEX(){}
	public float r;
	public float i;
	public T_COMPLEX(float r, float i)
	{
		this.r = r;
		this.i = i;
	}
}
