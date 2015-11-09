package org.asam.ods;
/**
 * Generated from IDL enum "SelOpcode".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class SelOpcode
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _EQ = 0;
	public static final SelOpcode EQ = new SelOpcode(_EQ);
	public static final int _NEQ = 1;
	public static final SelOpcode NEQ = new SelOpcode(_NEQ);
	public static final int _LT = 2;
	public static final SelOpcode LT = new SelOpcode(_LT);
	public static final int _GT = 3;
	public static final SelOpcode GT = new SelOpcode(_GT);
	public static final int _LTE = 4;
	public static final SelOpcode LTE = new SelOpcode(_LTE);
	public static final int _GTE = 5;
	public static final SelOpcode GTE = new SelOpcode(_GTE);
	public static final int _INSET = 6;
	public static final SelOpcode INSET = new SelOpcode(_INSET);
	public static final int _NOTINSET = 7;
	public static final SelOpcode NOTINSET = new SelOpcode(_NOTINSET);
	public static final int _LIKE = 8;
	public static final SelOpcode LIKE = new SelOpcode(_LIKE);
	public static final int _CI_EQ = 9;
	public static final SelOpcode CI_EQ = new SelOpcode(_CI_EQ);
	public static final int _CI_NEQ = 10;
	public static final SelOpcode CI_NEQ = new SelOpcode(_CI_NEQ);
	public static final int _CI_LT = 11;
	public static final SelOpcode CI_LT = new SelOpcode(_CI_LT);
	public static final int _CI_GT = 12;
	public static final SelOpcode CI_GT = new SelOpcode(_CI_GT);
	public static final int _CI_LTE = 13;
	public static final SelOpcode CI_LTE = new SelOpcode(_CI_LTE);
	public static final int _CI_GTE = 14;
	public static final SelOpcode CI_GTE = new SelOpcode(_CI_GTE);
	public static final int _CI_INSET = 15;
	public static final SelOpcode CI_INSET = new SelOpcode(_CI_INSET);
	public static final int _CI_NOTINSET = 16;
	public static final SelOpcode CI_NOTINSET = new SelOpcode(_CI_NOTINSET);
	public static final int _CI_LIKE = 17;
	public static final SelOpcode CI_LIKE = new SelOpcode(_CI_LIKE);
	public static final int _IS_NULL = 18;
	public static final SelOpcode IS_NULL = new SelOpcode(_IS_NULL);
	public static final int _IS_NOT_NULL = 19;
	public static final SelOpcode IS_NOT_NULL = new SelOpcode(_IS_NOT_NULL);
	public static final int _NOTLIKE = 20;
	public static final SelOpcode NOTLIKE = new SelOpcode(_NOTLIKE);
	public static final int _CI_NOTLIKE = 21;
	public static final SelOpcode CI_NOTLIKE = new SelOpcode(_CI_NOTLIKE);
	public static final int _BETWEEN = 22;
	public static final SelOpcode BETWEEN = new SelOpcode(_BETWEEN);
	public int value()
	{
		return value;
	}
	public static SelOpcode from_int(int value)
	{
		switch (value) {
			case _EQ: return EQ;
			case _NEQ: return NEQ;
			case _LT: return LT;
			case _GT: return GT;
			case _LTE: return LTE;
			case _GTE: return GTE;
			case _INSET: return INSET;
			case _NOTINSET: return NOTINSET;
			case _LIKE: return LIKE;
			case _CI_EQ: return CI_EQ;
			case _CI_NEQ: return CI_NEQ;
			case _CI_LT: return CI_LT;
			case _CI_GT: return CI_GT;
			case _CI_LTE: return CI_LTE;
			case _CI_GTE: return CI_GTE;
			case _CI_INSET: return CI_INSET;
			case _CI_NOTINSET: return CI_NOTINSET;
			case _CI_LIKE: return CI_LIKE;
			case _IS_NULL: return IS_NULL;
			case _IS_NOT_NULL: return IS_NOT_NULL;
			case _NOTLIKE: return NOTLIKE;
			case _CI_NOTLIKE: return CI_NOTLIKE;
			case _BETWEEN: return BETWEEN;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	public String toString()
	{
		switch (value) {
			case _EQ: return "EQ";
			case _NEQ: return "NEQ";
			case _LT: return "LT";
			case _GT: return "GT";
			case _LTE: return "LTE";
			case _GTE: return "GTE";
			case _INSET: return "INSET";
			case _NOTINSET: return "NOTINSET";
			case _LIKE: return "LIKE";
			case _CI_EQ: return "CI_EQ";
			case _CI_NEQ: return "CI_NEQ";
			case _CI_LT: return "CI_LT";
			case _CI_GT: return "CI_GT";
			case _CI_LTE: return "CI_LTE";
			case _CI_GTE: return "CI_GTE";
			case _CI_INSET: return "CI_INSET";
			case _CI_NOTINSET: return "CI_NOTINSET";
			case _CI_LIKE: return "CI_LIKE";
			case _IS_NULL: return "IS_NULL";
			case _IS_NOT_NULL: return "IS_NOT_NULL";
			case _NOTLIKE: return "NOTLIKE";
			case _CI_NOTLIKE: return "CI_NOTLIKE";
			case _BETWEEN: return "BETWEEN";
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected SelOpcode(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
