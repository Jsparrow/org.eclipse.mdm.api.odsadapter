package org.asam.ods;
/**
 * Generated from IDL enum "RelationType".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class RelationTypeHolder
	implements org.omg.CORBA.portable.Streamable
{
	public RelationType value;

	public RelationTypeHolder ()
	{
	}
	public RelationTypeHolder (final RelationType initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return RelationTypeHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = RelationTypeHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		RelationTypeHelper.write (out,value);
	}
}
