package org.asam.ods;

/**
 * Generated from IDL alias "SelOrderSequence".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class SelOrderSequenceHelper
{
	private static org.omg.CORBA.TypeCode _type = null;

	public static void insert (org.omg.CORBA.Any any, org.asam.ods.SelOrder[] s)
	{
		any.type (type ());
		write (any.create_output_stream (), s);
	}

	public static org.asam.ods.SelOrder[] extract (final org.omg.CORBA.Any any)
	{
		return read (any.create_input_stream ());
	}

	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_alias_tc(org.asam.ods.SelOrderSequenceHelper.id(), "SelOrderSequence",org.omg.CORBA.ORB.init().create_sequence_tc(0, org.asam.ods.SelOrderHelper.type()));
		}
		return _type;
	}

	public static String id()
	{
		return "IDL:org/asam/ods/SelOrderSequence:1.0";
	}
	public static org.asam.ods.SelOrder[] read (final org.omg.CORBA.portable.InputStream _in)
	{
		org.asam.ods.SelOrder[] _result;
		int _l_result48 = _in.read_long();
		try
		{
			 int x = _in.available();
			 if ( x > 0 && _l_result48 > x )
				{
					throw new org.omg.CORBA.MARSHAL("Sequence length too large. Only " + x + " available and trying to assign " + _l_result48);
				}
		}
		catch (java.io.IOException e)
		{
		}
		_result = new org.asam.ods.SelOrder[_l_result48];
		for (int i=0;i<_result.length;i++)
		{
			_result[i]=org.asam.ods.SelOrderHelper.read(_in);
		}

		return _result;
	}

	public static void write (final org.omg.CORBA.portable.OutputStream _out, org.asam.ods.SelOrder[] _s)
	{
		
		_out.write_long(_s.length);
		for (int i=0; i<_s.length;i++)
		{
			org.asam.ods.SelOrderHelper.write(_out,_s[i]);
		}

	}
}
