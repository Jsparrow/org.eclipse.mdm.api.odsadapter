package com.highqsoft.corbafileserver.generated;

/**
* com/highqsoft/corbafileserver/generated/ErrorCodeHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from src/main/idl/corbafileserver.idl
* Donnerstag, 16. Juni 2016 10:30 Uhr MESZ
*/


/**
   * The error code.
   */
public final class ErrorCodeHolder implements org.omg.CORBA.portable.Streamable
{
  public com.highqsoft.corbafileserver.generated.ErrorCode value = null;

  public ErrorCodeHolder ()
  {
  }

  public ErrorCodeHolder (com.highqsoft.corbafileserver.generated.ErrorCode initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = com.highqsoft.corbafileserver.generated.ErrorCodeHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    com.highqsoft.corbafileserver.generated.ErrorCodeHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return com.highqsoft.corbafileserver.generated.ErrorCodeHelper.type ();
  }

}
