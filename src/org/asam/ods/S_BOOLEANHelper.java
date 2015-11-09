package org.asam.ods;

/**
 * Generated from IDL alias "S_BOOLEAN".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class S_BOOLEANHelper
{
	private static org.omg.CORBA.TypeCode _type = null;

	public static void insert (org.omg.CORBA.Any any, boolean[] s)
	{
		any.type (type ());
		write (any.create_output_stream (), s);
	}

	public static boolean[] extract (final org.omg.CORBA.Any any)
	{
		return read (any.create_input_stream ());
	}

	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_alias_tc(org.asam.ods.S_BOOLEANHelper.id(), "S_BOOLEAN",org.omg.CORBA.ORB.init().create_sequence_tc(0, org.asam.ods.T_BOOLEANHelper.type()));
		}
		return _type;
	}

	public static String id()
	{
		return "IDL:org/asam/ods/S_BOOLEAN:1.0";
	}
	public static boolean[] read (final org.omg.CORBA.portable.InputStream _in)
	{
		boolean[] _result;
		int _l_result1 = _in.read_long();
		try
		{
			 int x = _in.available();
			 if ( x > 0 && _l_result1 > x )
				{
					throw new org.omg.CORBA.MARSHAL("Sequence length too large. Only " + x + " available and trying to assign " + _l_result1);
				}
		}
		catch (java.io.IOException e)
		{
		}
		_result = new boolean[_l_result1];
		_in.read_boolean_array(_result,0,_l_result1);
		return _result;
	}

	public static void write (final org.omg.CORBA.portable.OutputStream _out, boolean[] _s)
	{
		
		_out.write_long(_s.length);
		_out.write_boolean_array(_s,0,_s.length);
	}
}
