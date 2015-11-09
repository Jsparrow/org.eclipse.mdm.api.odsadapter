package org.asam.ods;

/**
 * Generated from IDL interface "ElemResultSetExtSeqIterator".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class ElemResultSetExtSeqIteratorHolder	implements org.omg.CORBA.portable.Streamable{
	 public ElemResultSetExtSeqIterator value;
	public ElemResultSetExtSeqIteratorHolder()
	{
	}
	public ElemResultSetExtSeqIteratorHolder (final ElemResultSetExtSeqIterator initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return ElemResultSetExtSeqIteratorHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = ElemResultSetExtSeqIteratorHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		ElemResultSetExtSeqIteratorHelper.write (_out,value);
	}
}
