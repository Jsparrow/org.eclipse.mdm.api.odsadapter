package org.asam.ods;

/**
 * Generated from IDL struct "NameValue".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class NameValue
	implements org.omg.CORBA.portable.IDLEntity
{
	public NameValue(){}
	public java.lang.String valName;
	public org.asam.ods.TS_Value value;
	public NameValue(java.lang.String valName, org.asam.ods.TS_Value value)
	{
		this.valName = valName;
		this.value = value;
	}
}
