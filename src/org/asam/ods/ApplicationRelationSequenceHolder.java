package org.asam.ods;

/**
 * Generated from IDL alias "ApplicationRelationSequence".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class ApplicationRelationSequenceHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.asam.ods.ApplicationRelation[] value;

	public ApplicationRelationSequenceHolder ()
	{
	}
	public ApplicationRelationSequenceHolder (final org.asam.ods.ApplicationRelation[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return ApplicationRelationSequenceHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = ApplicationRelationSequenceHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		ApplicationRelationSequenceHelper.write (out,value);
	}
}
