package org.asam.ods;
/**
 * Generated from IDL enum "BuildUpFunction".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class BuildUpFunction
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _BUP_JOIN = 0;
	public static final BuildUpFunction BUP_JOIN = new BuildUpFunction(_BUP_JOIN);
	public static final int _BUP_MERGE = 1;
	public static final BuildUpFunction BUP_MERGE = new BuildUpFunction(_BUP_MERGE);
	public static final int _BUP_SORT = 2;
	public static final BuildUpFunction BUP_SORT = new BuildUpFunction(_BUP_SORT);
	public int value()
	{
		return value;
	}
	public static BuildUpFunction from_int(int value)
	{
		switch (value) {
			case _BUP_JOIN: return BUP_JOIN;
			case _BUP_MERGE: return BUP_MERGE;
			case _BUP_SORT: return BUP_SORT;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	public String toString()
	{
		switch (value) {
			case _BUP_JOIN: return "BUP_JOIN";
			case _BUP_MERGE: return "BUP_MERGE";
			case _BUP_SORT: return "BUP_SORT";
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected BuildUpFunction(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
