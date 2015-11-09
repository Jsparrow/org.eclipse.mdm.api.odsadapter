package org.asam.ods;
/**
 * Generated from IDL enum "SetType".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class SetType
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _APPEND = 0;
	public static final SetType APPEND = new SetType(_APPEND);
	public static final int _INSERT = 1;
	public static final SetType INSERT = new SetType(_INSERT);
	public static final int _UPDATE = 2;
	public static final SetType UPDATE = new SetType(_UPDATE);
	public static final int _REMOVE = 3;
	public static final SetType REMOVE = new SetType(_REMOVE);
	public int value()
	{
		return value;
	}
	public static SetType from_int(int value)
	{
		switch (value) {
			case _APPEND: return APPEND;
			case _INSERT: return INSERT;
			case _UPDATE: return UPDATE;
			case _REMOVE: return REMOVE;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	public String toString()
	{
		switch (value) {
			case _APPEND: return "APPEND";
			case _INSERT: return "INSERT";
			case _UPDATE: return "UPDATE";
			case _REMOVE: return "REMOVE";
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected SetType(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
