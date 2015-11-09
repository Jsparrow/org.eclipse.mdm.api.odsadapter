package org.asam.ods;
/**
 * Generated from IDL enum "RelationType".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class RelationType
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _FATHER_CHILD = 0;
	public static final RelationType FATHER_CHILD = new RelationType(_FATHER_CHILD);
	public static final int _INFO = 1;
	public static final RelationType INFO = new RelationType(_INFO);
	public static final int _INHERITANCE = 2;
	public static final RelationType INHERITANCE = new RelationType(_INHERITANCE);
	public int value()
	{
		return value;
	}
	public static RelationType from_int(int value)
	{
		switch (value) {
			case _FATHER_CHILD: return FATHER_CHILD;
			case _INFO: return INFO;
			case _INHERITANCE: return INHERITANCE;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	public String toString()
	{
		switch (value) {
			case _FATHER_CHILD: return "FATHER_CHILD";
			case _INFO: return "INFO";
			case _INHERITANCE: return "INHERITANCE";
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected RelationType(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
