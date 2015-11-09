package org.asam.ods;
/**
 * Generated from IDL enum "JoinType".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class JoinType
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _JTDEFAULT = 0;
	public static final JoinType JTDEFAULT = new JoinType(_JTDEFAULT);
	public static final int _JTOUTER = 1;
	public static final JoinType JTOUTER = new JoinType(_JTOUTER);
	public int value()
	{
		return value;
	}
	public static JoinType from_int(int value)
	{
		switch (value) {
			case _JTDEFAULT: return JTDEFAULT;
			case _JTOUTER: return JTOUTER;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	public String toString()
	{
		switch (value) {
			case _JTDEFAULT: return "JTDEFAULT";
			case _JTOUTER: return "JTOUTER";
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected JoinType(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
