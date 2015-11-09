package org.asam.ods;

/**
 * Generated from IDL struct "EnumerationAttributeStructure".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class EnumerationAttributeStructure
	implements org.omg.CORBA.portable.IDLEntity
{
	public EnumerationAttributeStructure(){}
	public org.asam.ods.T_LONGLONG aid;
	public java.lang.String aaName;
	public java.lang.String enumName;
	public EnumerationAttributeStructure(org.asam.ods.T_LONGLONG aid, java.lang.String aaName, java.lang.String enumName)
	{
		this.aid = aid;
		this.aaName = aaName;
		this.enumName = enumName;
	}
}
