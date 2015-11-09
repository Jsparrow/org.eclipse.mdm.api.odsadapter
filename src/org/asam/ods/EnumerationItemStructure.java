package org.asam.ods;

/**
 * Generated from IDL struct "EnumerationItemStructure".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class EnumerationItemStructure
	implements org.omg.CORBA.portable.IDLEntity
{
	public EnumerationItemStructure(){}
	public int index;
	public java.lang.String itemName;
	public EnumerationItemStructure(int index, java.lang.String itemName)
	{
		this.index = index;
		this.itemName = itemName;
	}
}
