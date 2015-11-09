package org.asam.ods;

/**
 * Generated from IDL interface "Measurement".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class MeasurementHolder	implements org.omg.CORBA.portable.Streamable{
	 public Measurement value;
	public MeasurementHolder()
	{
	}
	public MeasurementHolder (final Measurement initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return MeasurementHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = MeasurementHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		MeasurementHelper.write (_out,value);
	}
}
