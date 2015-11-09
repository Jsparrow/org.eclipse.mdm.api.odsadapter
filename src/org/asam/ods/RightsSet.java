package org.asam.ods;
/**
 * Generated from IDL enum "RightsSet".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class RightsSet
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _SET_RIGHT = 0;
	public static final RightsSet SET_RIGHT = new RightsSet(_SET_RIGHT);
	public static final int _ADD_RIGHT = 1;
	public static final RightsSet ADD_RIGHT = new RightsSet(_ADD_RIGHT);
	public static final int _REMOVE_RIGHT = 2;
	public static final RightsSet REMOVE_RIGHT = new RightsSet(_REMOVE_RIGHT);
	public int value()
	{
		return value;
	}
	public static RightsSet from_int(int value)
	{
		switch (value) {
			case _SET_RIGHT: return SET_RIGHT;
			case _ADD_RIGHT: return ADD_RIGHT;
			case _REMOVE_RIGHT: return REMOVE_RIGHT;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	public String toString()
	{
		switch (value) {
			case _SET_RIGHT: return "SET_RIGHT";
			case _ADD_RIGHT: return "ADD_RIGHT";
			case _REMOVE_RIGHT: return "REMOVE_RIGHT";
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected RightsSet(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
