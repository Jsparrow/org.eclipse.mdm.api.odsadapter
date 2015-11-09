package org.asam.ods;

/**
 * Generated from IDL alias "SMatLinkSequence".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class SMatLinkSequenceHelper
{
	private static org.omg.CORBA.TypeCode _type = null;

	public static void insert (org.omg.CORBA.Any any, org.asam.ods.SMatLink[] s)
	{
		any.type (type ());
		write (any.create_output_stream (), s);
	}

	public static org.asam.ods.SMatLink[] extract (final org.omg.CORBA.Any any)
	{
		return read (any.create_input_stream ());
	}

	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_alias_tc(org.asam.ods.SMatLinkSequenceHelper.id(), "SMatLinkSequence",org.omg.CORBA.ORB.init().create_sequence_tc(0, org.omg.CORBA.ORB.init().create_interface_tc("IDL:org/asam/ods/SMatLink:1.0", "SMatLink")));
		}
		return _type;
	}

	public static String id()
	{
		return "IDL:org/asam/ods/SMatLinkSequence:1.0";
	}
	public static org.asam.ods.SMatLink[] read (final org.omg.CORBA.portable.InputStream _in)
	{
		org.asam.ods.SMatLink[] _result;
		int _l_result11 = _in.read_long();
		try
		{
			 int x = _in.available();
			 if ( x > 0 && _l_result11 > x )
				{
					throw new org.omg.CORBA.MARSHAL("Sequence length too large. Only " + x + " available and trying to assign " + _l_result11);
				}
		}
		catch (java.io.IOException e)
		{
		}
		_result = new org.asam.ods.SMatLink[_l_result11];
		for (int i=0;i<_result.length;i++)
		{
			_result[i]=org.asam.ods.SMatLinkHelper.read(_in);
		}

		return _result;
	}

	public static void write (final org.omg.CORBA.portable.OutputStream _out, org.asam.ods.SMatLink[] _s)
	{
		
		_out.write_long(_s.length);
		for (int i=0; i<_s.length;i++)
		{
			org.asam.ods.SMatLinkHelper.write(_out,_s[i]);
		}

	}
}
