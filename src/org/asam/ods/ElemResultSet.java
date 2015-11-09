package org.asam.ods;

/**
 * Generated from IDL struct "ElemResultSet".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class ElemResultSet
	implements org.omg.CORBA.portable.IDLEntity
{
	public ElemResultSet(){}
	public org.asam.ods.T_LONGLONG aid;
	public org.asam.ods.AttrResultSet[] attrValues;
	public ElemResultSet(org.asam.ods.T_LONGLONG aid, org.asam.ods.AttrResultSet[] attrValues)
	{
		this.aid = aid;
		this.attrValues = attrValues;
	}
}
