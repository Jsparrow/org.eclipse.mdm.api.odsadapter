package org.asam.ods;

/**
 * Generated from IDL alias "AIDNameUnitIdSequence".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class AIDNameUnitIdSequenceHelper
{
	private static org.omg.CORBA.TypeCode _type = null;

	public static void insert (org.omg.CORBA.Any any, org.asam.ods.AIDNameUnitId[] s)
	{
		any.type (type ());
		write (any.create_output_stream (), s);
	}

	public static org.asam.ods.AIDNameUnitId[] extract (final org.omg.CORBA.Any any)
	{
		return read (any.create_input_stream ());
	}

	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_alias_tc(org.asam.ods.AIDNameUnitIdSequenceHelper.id(), "AIDNameUnitIdSequence",org.omg.CORBA.ORB.init().create_sequence_tc(0, org.asam.ods.AIDNameUnitIdHelper.type()));
		}
		return _type;
	}

	public static String id()
	{
		return "IDL:org/asam/ods/AIDNameUnitIdSequence:1.0";
	}
	public static org.asam.ods.AIDNameUnitId[] read (final org.omg.CORBA.portable.InputStream _in)
	{
		org.asam.ods.AIDNameUnitId[] _result;
		int _l_result46 = _in.read_long();
		try
		{
			 int x = _in.available();
			 if ( x > 0 && _l_result46 > x )
				{
					throw new org.omg.CORBA.MARSHAL("Sequence length too large. Only " + x + " available and trying to assign " + _l_result46);
				}
		}
		catch (java.io.IOException e)
		{
		}
		_result = new org.asam.ods.AIDNameUnitId[_l_result46];
		for (int i=0;i<_result.length;i++)
		{
			_result[i]=org.asam.ods.AIDNameUnitIdHelper.read(_in);
		}

		return _result;
	}

	public static void write (final org.omg.CORBA.portable.OutputStream _out, org.asam.ods.AIDNameUnitId[] _s)
	{
		
		_out.write_long(_s.length);
		for (int i=0; i<_s.length;i++)
		{
			org.asam.ods.AIDNameUnitIdHelper.write(_out,_s[i]);
		}

	}
}
