package org.asam.ods;

/**
 * Generated from IDL alias "ACLSequence".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class ACLSequenceHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.asam.ods.ACL[] value;

	public ACLSequenceHolder ()
	{
	}
	public ACLSequenceHolder (final org.asam.ods.ACL[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return ACLSequenceHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = ACLSequenceHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		ACLSequenceHelper.write (out,value);
	}
}
