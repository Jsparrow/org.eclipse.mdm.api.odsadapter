package org.asam.ods;
/**
 * Generated from IDL enum "AttrType".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class AttrType
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _APPLATTR_ONLY = 0;
	public static final AttrType APPLATTR_ONLY = new AttrType(_APPLATTR_ONLY);
	public static final int _INSTATTR_ONLY = 1;
	public static final AttrType INSTATTR_ONLY = new AttrType(_INSTATTR_ONLY);
	public static final int _ALL = 2;
	public static final AttrType ALL = new AttrType(_ALL);
	public int value()
	{
		return value;
	}
	public static AttrType from_int(int value)
	{
		switch (value) {
			case _APPLATTR_ONLY: return APPLATTR_ONLY;
			case _INSTATTR_ONLY: return INSTATTR_ONLY;
			case _ALL: return ALL;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	public String toString()
	{
		switch (value) {
			case _APPLATTR_ONLY: return "APPLATTR_ONLY";
			case _INSTATTR_ONLY: return "INSTATTR_ONLY";
			case _ALL: return "ALL";
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected AttrType(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
