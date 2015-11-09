package org.asam.ods;
/**
 * Generated from IDL enum "SelType".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class SelType
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _SEL_VALUE_TYPE = 0;
	public static final SelType SEL_VALUE_TYPE = new SelType(_SEL_VALUE_TYPE);
	public static final int _SEL_OPERATOR_TYPE = 1;
	public static final SelType SEL_OPERATOR_TYPE = new SelType(_SEL_OPERATOR_TYPE);
	public int value()
	{
		return value;
	}
	public static SelType from_int(int value)
	{
		switch (value) {
			case _SEL_VALUE_TYPE: return SEL_VALUE_TYPE;
			case _SEL_OPERATOR_TYPE: return SEL_OPERATOR_TYPE;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	public String toString()
	{
		switch (value) {
			case _SEL_VALUE_TYPE: return "SEL_VALUE_TYPE";
			case _SEL_OPERATOR_TYPE: return "SEL_OPERATOR_TYPE";
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected SelType(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
