package org.asam.ods;

/**
 * Generated from IDL alias "ApplicationRelationInstanceElementSeqSequence".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class ApplicationRelationInstanceElementSeqSequenceHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.asam.ods.ApplicationRelationInstanceElementSeq[] value;

	public ApplicationRelationInstanceElementSeqSequenceHolder ()
	{
	}
	public ApplicationRelationInstanceElementSeqSequenceHolder (final org.asam.ods.ApplicationRelationInstanceElementSeq[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return ApplicationRelationInstanceElementSeqSequenceHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = ApplicationRelationInstanceElementSeqSequenceHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		ApplicationRelationInstanceElementSeqSequenceHelper.write (out,value);
	}
}
