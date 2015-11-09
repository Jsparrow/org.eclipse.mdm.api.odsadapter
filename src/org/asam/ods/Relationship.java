package org.asam.ods;
/**
 * Generated from IDL enum "Relationship".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class Relationship
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _FATHER = 0;
	public static final Relationship FATHER = new Relationship(_FATHER);
	public static final int _CHILD = 1;
	public static final Relationship CHILD = new Relationship(_CHILD);
	public static final int _INFO_TO = 2;
	public static final Relationship INFO_TO = new Relationship(_INFO_TO);
	public static final int _INFO_FROM = 3;
	public static final Relationship INFO_FROM = new Relationship(_INFO_FROM);
	public static final int _INFO_REL = 4;
	public static final Relationship INFO_REL = new Relationship(_INFO_REL);
	public static final int _SUPERTYPE = 5;
	public static final Relationship SUPERTYPE = new Relationship(_SUPERTYPE);
	public static final int _SUBTYPE = 6;
	public static final Relationship SUBTYPE = new Relationship(_SUBTYPE);
	public static final int _ALL_REL = 7;
	public static final Relationship ALL_REL = new Relationship(_ALL_REL);
	public int value()
	{
		return value;
	}
	public static Relationship from_int(int value)
	{
		switch (value) {
			case _FATHER: return FATHER;
			case _CHILD: return CHILD;
			case _INFO_TO: return INFO_TO;
			case _INFO_FROM: return INFO_FROM;
			case _INFO_REL: return INFO_REL;
			case _SUPERTYPE: return SUPERTYPE;
			case _SUBTYPE: return SUBTYPE;
			case _ALL_REL: return ALL_REL;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	public String toString()
	{
		switch (value) {
			case _FATHER: return "FATHER";
			case _CHILD: return "CHILD";
			case _INFO_TO: return "INFO_TO";
			case _INFO_FROM: return "INFO_FROM";
			case _INFO_REL: return "INFO_REL";
			case _SUPERTYPE: return "SUPERTYPE";
			case _SUBTYPE: return "SUBTYPE";
			case _ALL_REL: return "ALL_REL";
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected Relationship(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
