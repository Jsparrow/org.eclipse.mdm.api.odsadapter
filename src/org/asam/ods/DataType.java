package org.asam.ods;
/**
 * Generated from IDL enum "DataType".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class DataType
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _DT_UNKNOWN = 0;
	public static final DataType DT_UNKNOWN = new DataType(_DT_UNKNOWN);
	public static final int _DT_STRING = 1;
	public static final DataType DT_STRING = new DataType(_DT_STRING);
	public static final int _DT_SHORT = 2;
	public static final DataType DT_SHORT = new DataType(_DT_SHORT);
	public static final int _DT_FLOAT = 3;
	public static final DataType DT_FLOAT = new DataType(_DT_FLOAT);
	public static final int _DT_BOOLEAN = 4;
	public static final DataType DT_BOOLEAN = new DataType(_DT_BOOLEAN);
	public static final int _DT_BYTE = 5;
	public static final DataType DT_BYTE = new DataType(_DT_BYTE);
	public static final int _DT_LONG = 6;
	public static final DataType DT_LONG = new DataType(_DT_LONG);
	public static final int _DT_DOUBLE = 7;
	public static final DataType DT_DOUBLE = new DataType(_DT_DOUBLE);
	public static final int _DT_LONGLONG = 8;
	public static final DataType DT_LONGLONG = new DataType(_DT_LONGLONG);
	public static final int _DT_ID = 9;
	public static final DataType DT_ID = new DataType(_DT_ID);
	public static final int _DT_DATE = 10;
	public static final DataType DT_DATE = new DataType(_DT_DATE);
	public static final int _DT_BYTESTR = 11;
	public static final DataType DT_BYTESTR = new DataType(_DT_BYTESTR);
	public static final int _DT_BLOB = 12;
	public static final DataType DT_BLOB = new DataType(_DT_BLOB);
	public static final int _DT_COMPLEX = 13;
	public static final DataType DT_COMPLEX = new DataType(_DT_COMPLEX);
	public static final int _DT_DCOMPLEX = 14;
	public static final DataType DT_DCOMPLEX = new DataType(_DT_DCOMPLEX);
	public static final int _DS_STRING = 15;
	public static final DataType DS_STRING = new DataType(_DS_STRING);
	public static final int _DS_SHORT = 16;
	public static final DataType DS_SHORT = new DataType(_DS_SHORT);
	public static final int _DS_FLOAT = 17;
	public static final DataType DS_FLOAT = new DataType(_DS_FLOAT);
	public static final int _DS_BOOLEAN = 18;
	public static final DataType DS_BOOLEAN = new DataType(_DS_BOOLEAN);
	public static final int _DS_BYTE = 19;
	public static final DataType DS_BYTE = new DataType(_DS_BYTE);
	public static final int _DS_LONG = 20;
	public static final DataType DS_LONG = new DataType(_DS_LONG);
	public static final int _DS_DOUBLE = 21;
	public static final DataType DS_DOUBLE = new DataType(_DS_DOUBLE);
	public static final int _DS_LONGLONG = 22;
	public static final DataType DS_LONGLONG = new DataType(_DS_LONGLONG);
	public static final int _DS_COMPLEX = 23;
	public static final DataType DS_COMPLEX = new DataType(_DS_COMPLEX);
	public static final int _DS_DCOMPLEX = 24;
	public static final DataType DS_DCOMPLEX = new DataType(_DS_DCOMPLEX);
	public static final int _DS_ID = 25;
	public static final DataType DS_ID = new DataType(_DS_ID);
	public static final int _DS_DATE = 26;
	public static final DataType DS_DATE = new DataType(_DS_DATE);
	public static final int _DS_BYTESTR = 27;
	public static final DataType DS_BYTESTR = new DataType(_DS_BYTESTR);
	public static final int _DT_EXTERNALREFERENCE = 28;
	public static final DataType DT_EXTERNALREFERENCE = new DataType(_DT_EXTERNALREFERENCE);
	public static final int _DS_EXTERNALREFERENCE = 29;
	public static final DataType DS_EXTERNALREFERENCE = new DataType(_DS_EXTERNALREFERENCE);
	public static final int _DT_ENUM = 30;
	public static final DataType DT_ENUM = new DataType(_DT_ENUM);
	public static final int _DS_ENUM = 31;
	public static final DataType DS_ENUM = new DataType(_DS_ENUM);
	public int value()
	{
		return value;
	}
	public static DataType from_int(int value)
	{
		switch (value) {
			case _DT_UNKNOWN: return DT_UNKNOWN;
			case _DT_STRING: return DT_STRING;
			case _DT_SHORT: return DT_SHORT;
			case _DT_FLOAT: return DT_FLOAT;
			case _DT_BOOLEAN: return DT_BOOLEAN;
			case _DT_BYTE: return DT_BYTE;
			case _DT_LONG: return DT_LONG;
			case _DT_DOUBLE: return DT_DOUBLE;
			case _DT_LONGLONG: return DT_LONGLONG;
			case _DT_ID: return DT_ID;
			case _DT_DATE: return DT_DATE;
			case _DT_BYTESTR: return DT_BYTESTR;
			case _DT_BLOB: return DT_BLOB;
			case _DT_COMPLEX: return DT_COMPLEX;
			case _DT_DCOMPLEX: return DT_DCOMPLEX;
			case _DS_STRING: return DS_STRING;
			case _DS_SHORT: return DS_SHORT;
			case _DS_FLOAT: return DS_FLOAT;
			case _DS_BOOLEAN: return DS_BOOLEAN;
			case _DS_BYTE: return DS_BYTE;
			case _DS_LONG: return DS_LONG;
			case _DS_DOUBLE: return DS_DOUBLE;
			case _DS_LONGLONG: return DS_LONGLONG;
			case _DS_COMPLEX: return DS_COMPLEX;
			case _DS_DCOMPLEX: return DS_DCOMPLEX;
			case _DS_ID: return DS_ID;
			case _DS_DATE: return DS_DATE;
			case _DS_BYTESTR: return DS_BYTESTR;
			case _DT_EXTERNALREFERENCE: return DT_EXTERNALREFERENCE;
			case _DS_EXTERNALREFERENCE: return DS_EXTERNALREFERENCE;
			case _DT_ENUM: return DT_ENUM;
			case _DS_ENUM: return DS_ENUM;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	public String toString()
	{
		switch (value) {
			case _DT_UNKNOWN: return "DT_UNKNOWN";
			case _DT_STRING: return "DT_STRING";
			case _DT_SHORT: return "DT_SHORT";
			case _DT_FLOAT: return "DT_FLOAT";
			case _DT_BOOLEAN: return "DT_BOOLEAN";
			case _DT_BYTE: return "DT_BYTE";
			case _DT_LONG: return "DT_LONG";
			case _DT_DOUBLE: return "DT_DOUBLE";
			case _DT_LONGLONG: return "DT_LONGLONG";
			case _DT_ID: return "DT_ID";
			case _DT_DATE: return "DT_DATE";
			case _DT_BYTESTR: return "DT_BYTESTR";
			case _DT_BLOB: return "DT_BLOB";
			case _DT_COMPLEX: return "DT_COMPLEX";
			case _DT_DCOMPLEX: return "DT_DCOMPLEX";
			case _DS_STRING: return "DS_STRING";
			case _DS_SHORT: return "DS_SHORT";
			case _DS_FLOAT: return "DS_FLOAT";
			case _DS_BOOLEAN: return "DS_BOOLEAN";
			case _DS_BYTE: return "DS_BYTE";
			case _DS_LONG: return "DS_LONG";
			case _DS_DOUBLE: return "DS_DOUBLE";
			case _DS_LONGLONG: return "DS_LONGLONG";
			case _DS_COMPLEX: return "DS_COMPLEX";
			case _DS_DCOMPLEX: return "DS_DCOMPLEX";
			case _DS_ID: return "DS_ID";
			case _DS_DATE: return "DS_DATE";
			case _DS_BYTESTR: return "DS_BYTESTR";
			case _DT_EXTERNALREFERENCE: return "DT_EXTERNALREFERENCE";
			case _DS_EXTERNALREFERENCE: return "DS_EXTERNALREFERENCE";
			case _DT_ENUM: return "DT_ENUM";
			case _DS_ENUM: return "DS_ENUM";
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected DataType(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
