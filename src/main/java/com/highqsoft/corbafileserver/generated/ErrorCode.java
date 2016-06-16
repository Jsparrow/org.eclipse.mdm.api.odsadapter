package com.highqsoft.corbafileserver.generated;


/**
* com/highqsoft/corbafileserver/generated/ErrorCode.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from src/main/idl/corbafileserver.idl
* Donnerstag, 16. Juni 2016 10:30 Uhr MESZ
*/


/**
   * The error code.
   */
public class ErrorCode implements org.omg.CORBA.portable.IDLEntity
{
  private        int __value;
  private static int __size = 15;
  private static com.highqsoft.corbafileserver.generated.ErrorCode[] __array = new com.highqsoft.corbafileserver.generated.ErrorCode [__size];

  public static final int _FILESERVER_ASAMODS_EXCEPTION = 0;
  public static final com.highqsoft.corbafileserver.generated.ErrorCode FILESERVER_ASAMODS_EXCEPTION = new com.highqsoft.corbafileserver.generated.ErrorCode(_FILESERVER_ASAMODS_EXCEPTION);
  public static final int _FILESERVER_ACCESS_DENIED = 1;
  public static final com.highqsoft.corbafileserver.generated.ErrorCode FILESERVER_ACCESS_DENIED = new com.highqsoft.corbafileserver.generated.ErrorCode(_FILESERVER_ACCESS_DENIED);
  public static final int _FILESERVER_FILE_NOT_FOUND = 2;
  public static final com.highqsoft.corbafileserver.generated.ErrorCode FILESERVER_FILE_NOT_FOUND = new com.highqsoft.corbafileserver.generated.ErrorCode(_FILESERVER_FILE_NOT_FOUND);
  public static final int _FILESERVER_IO_EXCEPTION = 3;
  public static final com.highqsoft.corbafileserver.generated.ErrorCode FILESERVER_IO_EXCEPTION = new com.highqsoft.corbafileserver.generated.ErrorCode(_FILESERVER_IO_EXCEPTION);
  public static final int _FILESERVER_INFORMATION = 4;
  public static final com.highqsoft.corbafileserver.generated.ErrorCode FILESERVER_INFORMATION = new com.highqsoft.corbafileserver.generated.ErrorCode(_FILESERVER_INFORMATION);
  public static final int _FILESERVER_BAD_PARAMETER = 5;
  public static final com.highqsoft.corbafileserver.generated.ErrorCode FILESERVER_BAD_PARAMETER = new com.highqsoft.corbafileserver.generated.ErrorCode(_FILESERVER_BAD_PARAMETER);
  public static final int _FILESERVER_MISSING_PARAMETER = 6;
  public static final com.highqsoft.corbafileserver.generated.ErrorCode FILESERVER_MISSING_PARAMETER = new com.highqsoft.corbafileserver.generated.ErrorCode(_FILESERVER_MISSING_PARAMETER);
  public static final int _FILESERVER_CONNECT_FAILED = 7;
  public static final com.highqsoft.corbafileserver.generated.ErrorCode FILESERVER_CONNECT_FAILED = new com.highqsoft.corbafileserver.generated.ErrorCode(_FILESERVER_CONNECT_FAILED);
  public static final int _FILESERVER_CONNECT_REFUSED = 8;
  public static final com.highqsoft.corbafileserver.generated.ErrorCode FILESERVER_CONNECT_REFUSED = new com.highqsoft.corbafileserver.generated.ErrorCode(_FILESERVER_CONNECT_REFUSED);
  public static final int _FILESERVER_CONNECTION_LOST = 9;
  public static final com.highqsoft.corbafileserver.generated.ErrorCode FILESERVER_CONNECTION_LOST = new com.highqsoft.corbafileserver.generated.ErrorCode(_FILESERVER_CONNECTION_LOST);
  public static final int _FILESERVER_IMPLEMENTATION_PROBLEM = 10;
  public static final com.highqsoft.corbafileserver.generated.ErrorCode FILESERVER_IMPLEMENTATION_PROBLEM = new com.highqsoft.corbafileserver.generated.ErrorCode(_FILESERVER_IMPLEMENTATION_PROBLEM);
  public static final int _FILESERVER_NOT_IMPLEMENTED = 11;
  public static final com.highqsoft.corbafileserver.generated.ErrorCode FILESERVER_NOT_IMPLEMENTED = new com.highqsoft.corbafileserver.generated.ErrorCode(_FILESERVER_NOT_IMPLEMENTED);
  public static final int _FILESERVER_NO_MEMORY = 12;
  public static final com.highqsoft.corbafileserver.generated.ErrorCode FILESERVER_NO_MEMORY = new com.highqsoft.corbafileserver.generated.ErrorCode(_FILESERVER_NO_MEMORY);
  public static final int _FILESERVER_NULL_PARAMETER = 13;
  public static final com.highqsoft.corbafileserver.generated.ErrorCode FILESERVER_NULL_PARAMETER = new com.highqsoft.corbafileserver.generated.ErrorCode(_FILESERVER_NULL_PARAMETER);
  public static final int _FILESERVER_NOT_FOUND = 14;
  public static final com.highqsoft.corbafileserver.generated.ErrorCode FILESERVER_NOT_FOUND = new com.highqsoft.corbafileserver.generated.ErrorCode(_FILESERVER_NOT_FOUND);

  public int value ()
  {
    return __value;
  }

  public static com.highqsoft.corbafileserver.generated.ErrorCode from_int (int value)
  {
    if (value >= 0 && value < __size)
      return __array[value];
    else
      throw new org.omg.CORBA.BAD_PARAM ();
  }

  protected ErrorCode (int value)
  {
    __value = value;
    __array[__value] = this;
  }
} // class ErrorCode