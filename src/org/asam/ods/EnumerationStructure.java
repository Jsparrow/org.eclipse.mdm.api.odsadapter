package org.asam.ods;

/**
 * Generated from IDL struct "EnumerationStructure".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class EnumerationStructure
	implements org.omg.CORBA.portable.IDLEntity
{
	public EnumerationStructure(){}
	public java.lang.String enumName;
	public org.asam.ods.EnumerationItemStructure[] items;
	public EnumerationStructure(java.lang.String enumName, org.asam.ods.EnumerationItemStructure[] items)
	{
		this.enumName = enumName;
		this.items = items;
	}
}
