package org.asam.ods;

/**
 * Generated from IDL struct "SelOrder".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class SelOrder
	implements org.omg.CORBA.portable.IDLEntity
{
	public SelOrder(){}
	public org.asam.ods.AIDName attr;
	public boolean ascending;
	public SelOrder(org.asam.ods.AIDName attr, boolean ascending)
	{
		this.attr = attr;
		this.ascending = ascending;
	}
}
