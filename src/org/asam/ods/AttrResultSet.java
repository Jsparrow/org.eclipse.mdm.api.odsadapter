package org.asam.ods;

/**
 * Generated from IDL struct "AttrResultSet".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class AttrResultSet
	implements org.omg.CORBA.portable.IDLEntity
{
	public AttrResultSet(){}
	public org.asam.ods.NameValueSeqUnitId attrValues;
	public org.asam.ods.NameValueUnitIdIterator rest;
	public AttrResultSet(org.asam.ods.NameValueSeqUnitId attrValues, org.asam.ods.NameValueUnitIdIterator rest)
	{
		this.attrValues = attrValues;
		this.rest = rest;
	}
}
