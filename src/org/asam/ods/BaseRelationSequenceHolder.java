package org.asam.ods;

/**
 * Generated from IDL alias "BaseRelationSequence".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class BaseRelationSequenceHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.asam.ods.BaseRelation[] value;

	public BaseRelationSequenceHolder ()
	{
	}
	public BaseRelationSequenceHolder (final org.asam.ods.BaseRelation[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return BaseRelationSequenceHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = BaseRelationSequenceHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		BaseRelationSequenceHelper.write (out,value);
	}
}
