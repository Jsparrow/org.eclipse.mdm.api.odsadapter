package org.asam.ods;
/**
 * Generated from IDL enum "ValueMatrixMode".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class ValueMatrixMode
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _CALCULATED = 0;
	public static final ValueMatrixMode CALCULATED = new ValueMatrixMode(_CALCULATED);
	public static final int _STORAGE = 1;
	public static final ValueMatrixMode STORAGE = new ValueMatrixMode(_STORAGE);
	public int value()
	{
		return value;
	}
	public static ValueMatrixMode from_int(int value)
	{
		switch (value) {
			case _CALCULATED: return CALCULATED;
			case _STORAGE: return STORAGE;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	public String toString()
	{
		switch (value) {
			case _CALCULATED: return "CALCULATED";
			case _STORAGE: return "STORAGE";
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected ValueMatrixMode(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
