package org.asam.ods;

/**
 * Generated from IDL struct "RelationRange".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class RelationRange
	implements org.omg.CORBA.portable.IDLEntity
{
	public RelationRange(){}
	public short min;
	public short max;
	public RelationRange(short min, short max)
	{
		this.min = min;
		this.max = max;
	}
}
