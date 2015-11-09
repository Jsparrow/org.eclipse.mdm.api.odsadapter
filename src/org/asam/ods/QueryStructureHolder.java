package org.asam.ods;

/**
 * Generated from IDL struct "QueryStructure".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class QueryStructureHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.asam.ods.QueryStructure value;

	public QueryStructureHolder ()
	{
	}
	public QueryStructureHolder(final org.asam.ods.QueryStructure initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.asam.ods.QueryStructureHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.asam.ods.QueryStructureHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.asam.ods.QueryStructureHelper.write(_out, value);
	}
}
