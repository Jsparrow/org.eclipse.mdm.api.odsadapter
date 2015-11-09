package org.asam.ods;
/**
 * Generated from IDL enum "QueryStatus".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class QueryStatus
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _COMPLETE = 0;
	public static final QueryStatus COMPLETE = new QueryStatus(_COMPLETE);
	public static final int _INCOMPLETE = 1;
	public static final QueryStatus INCOMPLETE = new QueryStatus(_INCOMPLETE);
	public int value()
	{
		return value;
	}
	public static QueryStatus from_int(int value)
	{
		switch (value) {
			case _COMPLETE: return COMPLETE;
			case _INCOMPLETE: return INCOMPLETE;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	public String toString()
	{
		switch (value) {
			case _COMPLETE: return "COMPLETE";
			case _INCOMPLETE: return "INCOMPLETE";
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected QueryStatus(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
