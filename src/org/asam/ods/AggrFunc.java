package org.asam.ods;
/**
 * Generated from IDL enum "AggrFunc".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class AggrFunc
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _NONE = 0;
	public static final AggrFunc NONE = new AggrFunc(_NONE);
	public static final int _COUNT = 1;
	public static final AggrFunc COUNT = new AggrFunc(_COUNT);
	public static final int _DCOUNT = 2;
	public static final AggrFunc DCOUNT = new AggrFunc(_DCOUNT);
	public static final int _MIN = 3;
	public static final AggrFunc MIN = new AggrFunc(_MIN);
	public static final int _MAX = 4;
	public static final AggrFunc MAX = new AggrFunc(_MAX);
	public static final int _AVG = 5;
	public static final AggrFunc AVG = new AggrFunc(_AVG);
	public static final int _STDDEV = 6;
	public static final AggrFunc STDDEV = new AggrFunc(_STDDEV);
	public static final int _SUM = 7;
	public static final AggrFunc SUM = new AggrFunc(_SUM);
	public static final int _DISTINCT = 8;
	public static final AggrFunc DISTINCT = new AggrFunc(_DISTINCT);
	public static final int _POINT = 9;
	public static final AggrFunc POINT = new AggrFunc(_POINT);
	public int value()
	{
		return value;
	}
	public static AggrFunc from_int(int value)
	{
		switch (value) {
			case _NONE: return NONE;
			case _COUNT: return COUNT;
			case _DCOUNT: return DCOUNT;
			case _MIN: return MIN;
			case _MAX: return MAX;
			case _AVG: return AVG;
			case _STDDEV: return STDDEV;
			case _SUM: return SUM;
			case _DISTINCT: return DISTINCT;
			case _POINT: return POINT;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	public String toString()
	{
		switch (value) {
			case _NONE: return "NONE";
			case _COUNT: return "COUNT";
			case _DCOUNT: return "DCOUNT";
			case _MIN: return "MIN";
			case _MAX: return "MAX";
			case _AVG: return "AVG";
			case _STDDEV: return "STDDEV";
			case _SUM: return "SUM";
			case _DISTINCT: return "DISTINCT";
			case _POINT: return "POINT";
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected AggrFunc(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
