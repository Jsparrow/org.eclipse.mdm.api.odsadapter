package org.asam.ods;
/**
 * Generated from IDL enum "SeverityFlag".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class SeverityFlag
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _SUCCESS = 0;
	public static final SeverityFlag SUCCESS = new SeverityFlag(_SUCCESS);
	public static final int _INFORMATION = 1;
	public static final SeverityFlag INFORMATION = new SeverityFlag(_INFORMATION);
	public static final int _WARNING = 2;
	public static final SeverityFlag WARNING = new SeverityFlag(_WARNING);
	public static final int _ERROR = 3;
	public static final SeverityFlag ERROR = new SeverityFlag(_ERROR);
	public int value()
	{
		return value;
	}
	public static SeverityFlag from_int(int value)
	{
		switch (value) {
			case _SUCCESS: return SUCCESS;
			case _INFORMATION: return INFORMATION;
			case _WARNING: return WARNING;
			case _ERROR: return ERROR;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	public String toString()
	{
		switch (value) {
			case _SUCCESS: return "SUCCESS";
			case _INFORMATION: return "INFORMATION";
			case _WARNING: return "WARNING";
			case _ERROR: return "ERROR";
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected SeverityFlag(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
