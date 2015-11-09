package org.asam.ods;
/**
 * Generated from IDL enum "SelOperator".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class SelOperator
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _AND = 0;
	public static final SelOperator AND = new SelOperator(_AND);
	public static final int _OR = 1;
	public static final SelOperator OR = new SelOperator(_OR);
	public static final int _NOT = 2;
	public static final SelOperator NOT = new SelOperator(_NOT);
	public static final int _OPEN = 3;
	public static final SelOperator OPEN = new SelOperator(_OPEN);
	public static final int _CLOSE = 4;
	public static final SelOperator CLOSE = new SelOperator(_CLOSE);
	public int value()
	{
		return value;
	}
	public static SelOperator from_int(int value)
	{
		switch (value) {
			case _AND: return AND;
			case _OR: return OR;
			case _NOT: return NOT;
			case _OPEN: return OPEN;
			case _CLOSE: return CLOSE;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	public String toString()
	{
		switch (value) {
			case _AND: return "AND";
			case _OR: return "OR";
			case _NOT: return "NOT";
			case _OPEN: return "OPEN";
			case _CLOSE: return "CLOSE";
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected SelOperator(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
