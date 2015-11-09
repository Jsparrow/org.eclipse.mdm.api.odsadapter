package org.asam.ods;

/**
 * Generated from IDL alias "S_BOOLEAN".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class S_BOOLEANHolder
	implements org.omg.CORBA.portable.Streamable
{
	public boolean[] value;

	public S_BOOLEANHolder ()
	{
	}
	public S_BOOLEANHolder (final boolean[] initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return S_BOOLEANHelper.type ();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = S_BOOLEANHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream out)
	{
		S_BOOLEANHelper.write (out,value);
	}
}
