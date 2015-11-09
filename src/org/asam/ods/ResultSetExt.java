package org.asam.ods;

/**
 * Generated from IDL struct "ResultSetExt".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class ResultSetExt
	implements org.omg.CORBA.portable.IDLEntity
{
	public ResultSetExt(){}
	public org.asam.ods.ElemResultSetExt[] firstElems;
	public org.asam.ods.ElemResultSetExtSeqIterator restElems;
	public ResultSetExt(org.asam.ods.ElemResultSetExt[] firstElems, org.asam.ods.ElemResultSetExtSeqIterator restElems)
	{
		this.firstElems = firstElems;
		this.restElems = restElems;
	}
}
