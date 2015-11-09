package org.asam.ods;
/**
 * Generated from IDL enum "ErrorCode".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class ErrorCode
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _AO_UNKNOWN_ERROR = 0;
	public static final ErrorCode AO_UNKNOWN_ERROR = new ErrorCode(_AO_UNKNOWN_ERROR);
	public static final int _AO_ACCESS_DENIED = 1;
	public static final ErrorCode AO_ACCESS_DENIED = new ErrorCode(_AO_ACCESS_DENIED);
	public static final int _AO_BAD_OPERATION = 2;
	public static final ErrorCode AO_BAD_OPERATION = new ErrorCode(_AO_BAD_OPERATION);
	public static final int _AO_BAD_PARAMETER = 3;
	public static final ErrorCode AO_BAD_PARAMETER = new ErrorCode(_AO_BAD_PARAMETER);
	public static final int _AO_CONNECT_FAILED = 4;
	public static final ErrorCode AO_CONNECT_FAILED = new ErrorCode(_AO_CONNECT_FAILED);
	public static final int _AO_CONNECT_REFUSED = 5;
	public static final ErrorCode AO_CONNECT_REFUSED = new ErrorCode(_AO_CONNECT_REFUSED);
	public static final int _AO_CONNECTION_LOST = 6;
	public static final ErrorCode AO_CONNECTION_LOST = new ErrorCode(_AO_CONNECTION_LOST);
	public static final int _AO_DUPLICATE_BASE_ATTRIBUTE = 7;
	public static final ErrorCode AO_DUPLICATE_BASE_ATTRIBUTE = new ErrorCode(_AO_DUPLICATE_BASE_ATTRIBUTE);
	public static final int _AO_DUPLICATE_NAME = 8;
	public static final ErrorCode AO_DUPLICATE_NAME = new ErrorCode(_AO_DUPLICATE_NAME);
	public static final int _AO_DUPLICATE_VALUE = 9;
	public static final ErrorCode AO_DUPLICATE_VALUE = new ErrorCode(_AO_DUPLICATE_VALUE);
	public static final int _AO_HAS_INSTANCES = 10;
	public static final ErrorCode AO_HAS_INSTANCES = new ErrorCode(_AO_HAS_INSTANCES);
	public static final int _AO_HAS_REFERENCES = 11;
	public static final ErrorCode AO_HAS_REFERENCES = new ErrorCode(_AO_HAS_REFERENCES);
	public static final int _AO_IMPLEMENTATION_PROBLEM = 12;
	public static final ErrorCode AO_IMPLEMENTATION_PROBLEM = new ErrorCode(_AO_IMPLEMENTATION_PROBLEM);
	public static final int _AO_INCOMPATIBLE_UNITS = 13;
	public static final ErrorCode AO_INCOMPATIBLE_UNITS = new ErrorCode(_AO_INCOMPATIBLE_UNITS);
	public static final int _AO_INVALID_ASAM_PATH = 14;
	public static final ErrorCode AO_INVALID_ASAM_PATH = new ErrorCode(_AO_INVALID_ASAM_PATH);
	public static final int _AO_INVALID_ATTRIBUTE_TYPE = 15;
	public static final ErrorCode AO_INVALID_ATTRIBUTE_TYPE = new ErrorCode(_AO_INVALID_ATTRIBUTE_TYPE);
	public static final int _AO_INVALID_BASE_ELEMENT = 16;
	public static final ErrorCode AO_INVALID_BASE_ELEMENT = new ErrorCode(_AO_INVALID_BASE_ELEMENT);
	public static final int _AO_INVALID_BASETYPE = 17;
	public static final ErrorCode AO_INVALID_BASETYPE = new ErrorCode(_AO_INVALID_BASETYPE);
	public static final int _AO_INVALID_BUILDUP_FUNCTION = 18;
	public static final ErrorCode AO_INVALID_BUILDUP_FUNCTION = new ErrorCode(_AO_INVALID_BUILDUP_FUNCTION);
	public static final int _AO_INVALID_COLUMN = 19;
	public static final ErrorCode AO_INVALID_COLUMN = new ErrorCode(_AO_INVALID_COLUMN);
	public static final int _AO_INVALID_COUNT = 20;
	public static final ErrorCode AO_INVALID_COUNT = new ErrorCode(_AO_INVALID_COUNT);
	public static final int _AO_INVALID_DATATYPE = 21;
	public static final ErrorCode AO_INVALID_DATATYPE = new ErrorCode(_AO_INVALID_DATATYPE);
	public static final int _AO_INVALID_ELEMENT = 22;
	public static final ErrorCode AO_INVALID_ELEMENT = new ErrorCode(_AO_INVALID_ELEMENT);
	public static final int _AO_INVALID_LENGTH = 23;
	public static final ErrorCode AO_INVALID_LENGTH = new ErrorCode(_AO_INVALID_LENGTH);
	public static final int _AO_INVALID_ORDINALNUMBER = 24;
	public static final ErrorCode AO_INVALID_ORDINALNUMBER = new ErrorCode(_AO_INVALID_ORDINALNUMBER);
	public static final int _AO_INVALID_RELATION = 25;
	public static final ErrorCode AO_INVALID_RELATION = new ErrorCode(_AO_INVALID_RELATION);
	public static final int _AO_INVALID_RELATION_RANGE = 26;
	public static final ErrorCode AO_INVALID_RELATION_RANGE = new ErrorCode(_AO_INVALID_RELATION_RANGE);
	public static final int _AO_INVALID_RELATION_TYPE = 27;
	public static final ErrorCode AO_INVALID_RELATION_TYPE = new ErrorCode(_AO_INVALID_RELATION_TYPE);
	public static final int _AO_INVALID_RELATIONSHIP = 28;
	public static final ErrorCode AO_INVALID_RELATIONSHIP = new ErrorCode(_AO_INVALID_RELATIONSHIP);
	public static final int _AO_INVALID_SET_TYPE = 29;
	public static final ErrorCode AO_INVALID_SET_TYPE = new ErrorCode(_AO_INVALID_SET_TYPE);
	public static final int _AO_INVALID_SMATLINK = 30;
	public static final ErrorCode AO_INVALID_SMATLINK = new ErrorCode(_AO_INVALID_SMATLINK);
	public static final int _AO_INVALID_SUBMATRIX = 31;
	public static final ErrorCode AO_INVALID_SUBMATRIX = new ErrorCode(_AO_INVALID_SUBMATRIX);
	public static final int _AO_IS_BASE_ATTRIBUTE = 32;
	public static final ErrorCode AO_IS_BASE_ATTRIBUTE = new ErrorCode(_AO_IS_BASE_ATTRIBUTE);
	public static final int _AO_IS_BASE_RELATION = 33;
	public static final ErrorCode AO_IS_BASE_RELATION = new ErrorCode(_AO_IS_BASE_RELATION);
	public static final int _AO_IS_MEASUREMENT_MATRIX = 34;
	public static final ErrorCode AO_IS_MEASUREMENT_MATRIX = new ErrorCode(_AO_IS_MEASUREMENT_MATRIX);
	public static final int _AO_MATH_ERROR = 35;
	public static final ErrorCode AO_MATH_ERROR = new ErrorCode(_AO_MATH_ERROR);
	public static final int _AO_MISSING_APPLICATION_ELEMENT = 36;
	public static final ErrorCode AO_MISSING_APPLICATION_ELEMENT = new ErrorCode(_AO_MISSING_APPLICATION_ELEMENT);
	public static final int _AO_MISSING_ATTRIBUTE = 37;
	public static final ErrorCode AO_MISSING_ATTRIBUTE = new ErrorCode(_AO_MISSING_ATTRIBUTE);
	public static final int _AO_MISSING_RELATION = 38;
	public static final ErrorCode AO_MISSING_RELATION = new ErrorCode(_AO_MISSING_RELATION);
	public static final int _AO_MISSING_VALUE = 39;
	public static final ErrorCode AO_MISSING_VALUE = new ErrorCode(_AO_MISSING_VALUE);
	public static final int _AO_NO_MEMORY = 40;
	public static final ErrorCode AO_NO_MEMORY = new ErrorCode(_AO_NO_MEMORY);
	public static final int _AO_NO_PATH_TO_ELEMENT = 41;
	public static final ErrorCode AO_NO_PATH_TO_ELEMENT = new ErrorCode(_AO_NO_PATH_TO_ELEMENT);
	public static final int _AO_NOT_FOUND = 42;
	public static final ErrorCode AO_NOT_FOUND = new ErrorCode(_AO_NOT_FOUND);
	public static final int _AO_NOT_IMPLEMENTED = 43;
	public static final ErrorCode AO_NOT_IMPLEMENTED = new ErrorCode(_AO_NOT_IMPLEMENTED);
	public static final int _AO_NOT_UNIQUE = 44;
	public static final ErrorCode AO_NOT_UNIQUE = new ErrorCode(_AO_NOT_UNIQUE);
	public static final int _AO_OPEN_MODE_NOT_SUPPORTED = 45;
	public static final ErrorCode AO_OPEN_MODE_NOT_SUPPORTED = new ErrorCode(_AO_OPEN_MODE_NOT_SUPPORTED);
	public static final int _AO_SESSION_LIMIT_REACHED = 46;
	public static final ErrorCode AO_SESSION_LIMIT_REACHED = new ErrorCode(_AO_SESSION_LIMIT_REACHED);
	public static final int _AO_SESSION_NOT_ACTIVE = 47;
	public static final ErrorCode AO_SESSION_NOT_ACTIVE = new ErrorCode(_AO_SESSION_NOT_ACTIVE);
	public static final int _AO_TRANSACTION_ALREADY_ACTIVE = 48;
	public static final ErrorCode AO_TRANSACTION_ALREADY_ACTIVE = new ErrorCode(_AO_TRANSACTION_ALREADY_ACTIVE);
	public static final int _AO_TRANSACTION_NOT_ACTIVE = 49;
	public static final ErrorCode AO_TRANSACTION_NOT_ACTIVE = new ErrorCode(_AO_TRANSACTION_NOT_ACTIVE);
	public static final int _AO_HAS_BASE_RELATION = 50;
	public static final ErrorCode AO_HAS_BASE_RELATION = new ErrorCode(_AO_HAS_BASE_RELATION);
	public static final int _AO_HAS_BASE_ATTRIBUTE = 51;
	public static final ErrorCode AO_HAS_BASE_ATTRIBUTE = new ErrorCode(_AO_HAS_BASE_ATTRIBUTE);
	public static final int _AO_UNKNOWN_UNIT = 52;
	public static final ErrorCode AO_UNKNOWN_UNIT = new ErrorCode(_AO_UNKNOWN_UNIT);
	public static final int _AO_NO_SCALING_COLUMN = 53;
	public static final ErrorCode AO_NO_SCALING_COLUMN = new ErrorCode(_AO_NO_SCALING_COLUMN);
	public static final int _AO_QUERY_TYPE_INVALID = 54;
	public static final ErrorCode AO_QUERY_TYPE_INVALID = new ErrorCode(_AO_QUERY_TYPE_INVALID);
	public static final int _AO_QUERY_INVALID = 55;
	public static final ErrorCode AO_QUERY_INVALID = new ErrorCode(_AO_QUERY_INVALID);
	public static final int _AO_QUERY_PROCESSING_ERROR = 56;
	public static final ErrorCode AO_QUERY_PROCESSING_ERROR = new ErrorCode(_AO_QUERY_PROCESSING_ERROR);
	public static final int _AO_QUERY_TIMEOUT_EXCEEDED = 57;
	public static final ErrorCode AO_QUERY_TIMEOUT_EXCEEDED = new ErrorCode(_AO_QUERY_TIMEOUT_EXCEEDED);
	public static final int _AO_QUERY_INCOMPLETE = 58;
	public static final ErrorCode AO_QUERY_INCOMPLETE = new ErrorCode(_AO_QUERY_INCOMPLETE);
	public static final int _AO_QUERY_INVALID_RESULTTYPE = 59;
	public static final ErrorCode AO_QUERY_INVALID_RESULTTYPE = new ErrorCode(_AO_QUERY_INVALID_RESULTTYPE);
	public static final int _AO_INVALID_VALUEMATRIX_STRUCTURE = 60;
	public static final ErrorCode AO_INVALID_VALUEMATRIX_STRUCTURE = new ErrorCode(_AO_INVALID_VALUEMATRIX_STRUCTURE);
	public static final int _AO_FILE_LOCKED = 61;
	public static final ErrorCode AO_FILE_LOCKED = new ErrorCode(_AO_FILE_LOCKED);
	public static final int _AO_SYSTEM_PROBLEM = 62;
	public static final ErrorCode AO_SYSTEM_PROBLEM = new ErrorCode(_AO_SYSTEM_PROBLEM);
	public int value()
	{
		return value;
	}
	public static ErrorCode from_int(int value)
	{
		switch (value) {
			case _AO_UNKNOWN_ERROR: return AO_UNKNOWN_ERROR;
			case _AO_ACCESS_DENIED: return AO_ACCESS_DENIED;
			case _AO_BAD_OPERATION: return AO_BAD_OPERATION;
			case _AO_BAD_PARAMETER: return AO_BAD_PARAMETER;
			case _AO_CONNECT_FAILED: return AO_CONNECT_FAILED;
			case _AO_CONNECT_REFUSED: return AO_CONNECT_REFUSED;
			case _AO_CONNECTION_LOST: return AO_CONNECTION_LOST;
			case _AO_DUPLICATE_BASE_ATTRIBUTE: return AO_DUPLICATE_BASE_ATTRIBUTE;
			case _AO_DUPLICATE_NAME: return AO_DUPLICATE_NAME;
			case _AO_DUPLICATE_VALUE: return AO_DUPLICATE_VALUE;
			case _AO_HAS_INSTANCES: return AO_HAS_INSTANCES;
			case _AO_HAS_REFERENCES: return AO_HAS_REFERENCES;
			case _AO_IMPLEMENTATION_PROBLEM: return AO_IMPLEMENTATION_PROBLEM;
			case _AO_INCOMPATIBLE_UNITS: return AO_INCOMPATIBLE_UNITS;
			case _AO_INVALID_ASAM_PATH: return AO_INVALID_ASAM_PATH;
			case _AO_INVALID_ATTRIBUTE_TYPE: return AO_INVALID_ATTRIBUTE_TYPE;
			case _AO_INVALID_BASE_ELEMENT: return AO_INVALID_BASE_ELEMENT;
			case _AO_INVALID_BASETYPE: return AO_INVALID_BASETYPE;
			case _AO_INVALID_BUILDUP_FUNCTION: return AO_INVALID_BUILDUP_FUNCTION;
			case _AO_INVALID_COLUMN: return AO_INVALID_COLUMN;
			case _AO_INVALID_COUNT: return AO_INVALID_COUNT;
			case _AO_INVALID_DATATYPE: return AO_INVALID_DATATYPE;
			case _AO_INVALID_ELEMENT: return AO_INVALID_ELEMENT;
			case _AO_INVALID_LENGTH: return AO_INVALID_LENGTH;
			case _AO_INVALID_ORDINALNUMBER: return AO_INVALID_ORDINALNUMBER;
			case _AO_INVALID_RELATION: return AO_INVALID_RELATION;
			case _AO_INVALID_RELATION_RANGE: return AO_INVALID_RELATION_RANGE;
			case _AO_INVALID_RELATION_TYPE: return AO_INVALID_RELATION_TYPE;
			case _AO_INVALID_RELATIONSHIP: return AO_INVALID_RELATIONSHIP;
			case _AO_INVALID_SET_TYPE: return AO_INVALID_SET_TYPE;
			case _AO_INVALID_SMATLINK: return AO_INVALID_SMATLINK;
			case _AO_INVALID_SUBMATRIX: return AO_INVALID_SUBMATRIX;
			case _AO_IS_BASE_ATTRIBUTE: return AO_IS_BASE_ATTRIBUTE;
			case _AO_IS_BASE_RELATION: return AO_IS_BASE_RELATION;
			case _AO_IS_MEASUREMENT_MATRIX: return AO_IS_MEASUREMENT_MATRIX;
			case _AO_MATH_ERROR: return AO_MATH_ERROR;
			case _AO_MISSING_APPLICATION_ELEMENT: return AO_MISSING_APPLICATION_ELEMENT;
			case _AO_MISSING_ATTRIBUTE: return AO_MISSING_ATTRIBUTE;
			case _AO_MISSING_RELATION: return AO_MISSING_RELATION;
			case _AO_MISSING_VALUE: return AO_MISSING_VALUE;
			case _AO_NO_MEMORY: return AO_NO_MEMORY;
			case _AO_NO_PATH_TO_ELEMENT: return AO_NO_PATH_TO_ELEMENT;
			case _AO_NOT_FOUND: return AO_NOT_FOUND;
			case _AO_NOT_IMPLEMENTED: return AO_NOT_IMPLEMENTED;
			case _AO_NOT_UNIQUE: return AO_NOT_UNIQUE;
			case _AO_OPEN_MODE_NOT_SUPPORTED: return AO_OPEN_MODE_NOT_SUPPORTED;
			case _AO_SESSION_LIMIT_REACHED: return AO_SESSION_LIMIT_REACHED;
			case _AO_SESSION_NOT_ACTIVE: return AO_SESSION_NOT_ACTIVE;
			case _AO_TRANSACTION_ALREADY_ACTIVE: return AO_TRANSACTION_ALREADY_ACTIVE;
			case _AO_TRANSACTION_NOT_ACTIVE: return AO_TRANSACTION_NOT_ACTIVE;
			case _AO_HAS_BASE_RELATION: return AO_HAS_BASE_RELATION;
			case _AO_HAS_BASE_ATTRIBUTE: return AO_HAS_BASE_ATTRIBUTE;
			case _AO_UNKNOWN_UNIT: return AO_UNKNOWN_UNIT;
			case _AO_NO_SCALING_COLUMN: return AO_NO_SCALING_COLUMN;
			case _AO_QUERY_TYPE_INVALID: return AO_QUERY_TYPE_INVALID;
			case _AO_QUERY_INVALID: return AO_QUERY_INVALID;
			case _AO_QUERY_PROCESSING_ERROR: return AO_QUERY_PROCESSING_ERROR;
			case _AO_QUERY_TIMEOUT_EXCEEDED: return AO_QUERY_TIMEOUT_EXCEEDED;
			case _AO_QUERY_INCOMPLETE: return AO_QUERY_INCOMPLETE;
			case _AO_QUERY_INVALID_RESULTTYPE: return AO_QUERY_INVALID_RESULTTYPE;
			case _AO_INVALID_VALUEMATRIX_STRUCTURE: return AO_INVALID_VALUEMATRIX_STRUCTURE;
			case _AO_FILE_LOCKED: return AO_FILE_LOCKED;
			case _AO_SYSTEM_PROBLEM: return AO_SYSTEM_PROBLEM;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	public String toString()
	{
		switch (value) {
			case _AO_UNKNOWN_ERROR: return "AO_UNKNOWN_ERROR";
			case _AO_ACCESS_DENIED: return "AO_ACCESS_DENIED";
			case _AO_BAD_OPERATION: return "AO_BAD_OPERATION";
			case _AO_BAD_PARAMETER: return "AO_BAD_PARAMETER";
			case _AO_CONNECT_FAILED: return "AO_CONNECT_FAILED";
			case _AO_CONNECT_REFUSED: return "AO_CONNECT_REFUSED";
			case _AO_CONNECTION_LOST: return "AO_CONNECTION_LOST";
			case _AO_DUPLICATE_BASE_ATTRIBUTE: return "AO_DUPLICATE_BASE_ATTRIBUTE";
			case _AO_DUPLICATE_NAME: return "AO_DUPLICATE_NAME";
			case _AO_DUPLICATE_VALUE: return "AO_DUPLICATE_VALUE";
			case _AO_HAS_INSTANCES: return "AO_HAS_INSTANCES";
			case _AO_HAS_REFERENCES: return "AO_HAS_REFERENCES";
			case _AO_IMPLEMENTATION_PROBLEM: return "AO_IMPLEMENTATION_PROBLEM";
			case _AO_INCOMPATIBLE_UNITS: return "AO_INCOMPATIBLE_UNITS";
			case _AO_INVALID_ASAM_PATH: return "AO_INVALID_ASAM_PATH";
			case _AO_INVALID_ATTRIBUTE_TYPE: return "AO_INVALID_ATTRIBUTE_TYPE";
			case _AO_INVALID_BASE_ELEMENT: return "AO_INVALID_BASE_ELEMENT";
			case _AO_INVALID_BASETYPE: return "AO_INVALID_BASETYPE";
			case _AO_INVALID_BUILDUP_FUNCTION: return "AO_INVALID_BUILDUP_FUNCTION";
			case _AO_INVALID_COLUMN: return "AO_INVALID_COLUMN";
			case _AO_INVALID_COUNT: return "AO_INVALID_COUNT";
			case _AO_INVALID_DATATYPE: return "AO_INVALID_DATATYPE";
			case _AO_INVALID_ELEMENT: return "AO_INVALID_ELEMENT";
			case _AO_INVALID_LENGTH: return "AO_INVALID_LENGTH";
			case _AO_INVALID_ORDINALNUMBER: return "AO_INVALID_ORDINALNUMBER";
			case _AO_INVALID_RELATION: return "AO_INVALID_RELATION";
			case _AO_INVALID_RELATION_RANGE: return "AO_INVALID_RELATION_RANGE";
			case _AO_INVALID_RELATION_TYPE: return "AO_INVALID_RELATION_TYPE";
			case _AO_INVALID_RELATIONSHIP: return "AO_INVALID_RELATIONSHIP";
			case _AO_INVALID_SET_TYPE: return "AO_INVALID_SET_TYPE";
			case _AO_INVALID_SMATLINK: return "AO_INVALID_SMATLINK";
			case _AO_INVALID_SUBMATRIX: return "AO_INVALID_SUBMATRIX";
			case _AO_IS_BASE_ATTRIBUTE: return "AO_IS_BASE_ATTRIBUTE";
			case _AO_IS_BASE_RELATION: return "AO_IS_BASE_RELATION";
			case _AO_IS_MEASUREMENT_MATRIX: return "AO_IS_MEASUREMENT_MATRIX";
			case _AO_MATH_ERROR: return "AO_MATH_ERROR";
			case _AO_MISSING_APPLICATION_ELEMENT: return "AO_MISSING_APPLICATION_ELEMENT";
			case _AO_MISSING_ATTRIBUTE: return "AO_MISSING_ATTRIBUTE";
			case _AO_MISSING_RELATION: return "AO_MISSING_RELATION";
			case _AO_MISSING_VALUE: return "AO_MISSING_VALUE";
			case _AO_NO_MEMORY: return "AO_NO_MEMORY";
			case _AO_NO_PATH_TO_ELEMENT: return "AO_NO_PATH_TO_ELEMENT";
			case _AO_NOT_FOUND: return "AO_NOT_FOUND";
			case _AO_NOT_IMPLEMENTED: return "AO_NOT_IMPLEMENTED";
			case _AO_NOT_UNIQUE: return "AO_NOT_UNIQUE";
			case _AO_OPEN_MODE_NOT_SUPPORTED: return "AO_OPEN_MODE_NOT_SUPPORTED";
			case _AO_SESSION_LIMIT_REACHED: return "AO_SESSION_LIMIT_REACHED";
			case _AO_SESSION_NOT_ACTIVE: return "AO_SESSION_NOT_ACTIVE";
			case _AO_TRANSACTION_ALREADY_ACTIVE: return "AO_TRANSACTION_ALREADY_ACTIVE";
			case _AO_TRANSACTION_NOT_ACTIVE: return "AO_TRANSACTION_NOT_ACTIVE";
			case _AO_HAS_BASE_RELATION: return "AO_HAS_BASE_RELATION";
			case _AO_HAS_BASE_ATTRIBUTE: return "AO_HAS_BASE_ATTRIBUTE";
			case _AO_UNKNOWN_UNIT: return "AO_UNKNOWN_UNIT";
			case _AO_NO_SCALING_COLUMN: return "AO_NO_SCALING_COLUMN";
			case _AO_QUERY_TYPE_INVALID: return "AO_QUERY_TYPE_INVALID";
			case _AO_QUERY_INVALID: return "AO_QUERY_INVALID";
			case _AO_QUERY_PROCESSING_ERROR: return "AO_QUERY_PROCESSING_ERROR";
			case _AO_QUERY_TIMEOUT_EXCEEDED: return "AO_QUERY_TIMEOUT_EXCEEDED";
			case _AO_QUERY_INCOMPLETE: return "AO_QUERY_INCOMPLETE";
			case _AO_QUERY_INVALID_RESULTTYPE: return "AO_QUERY_INVALID_RESULTTYPE";
			case _AO_INVALID_VALUEMATRIX_STRUCTURE: return "AO_INVALID_VALUEMATRIX_STRUCTURE";
			case _AO_FILE_LOCKED: return "AO_FILE_LOCKED";
			case _AO_SYSTEM_PROBLEM: return "AO_SYSTEM_PROBLEM";
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected ErrorCode(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
