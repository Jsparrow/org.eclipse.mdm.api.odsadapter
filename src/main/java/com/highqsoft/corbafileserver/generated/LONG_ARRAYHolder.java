package com.highqsoft.corbafileserver.generated;


/**
* com/highqsoft/corbafileserver/generated/LONG_ARRAYHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from src/main/idl/corbafileserver.idl
* Donnerstag, 16. Juni 2016 10:30 Uhr MESZ
*/

public final class LONG_ARRAYHolder implements org.omg.CORBA.portable.Streamable
{
  public long value[] = null;

  public LONG_ARRAYHolder ()
  {
  }

  public LONG_ARRAYHolder (long[] initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = com.highqsoft.corbafileserver.generated.LONG_ARRAYHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    com.highqsoft.corbafileserver.generated.LONG_ARRAYHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return com.highqsoft.corbafileserver.generated.LONG_ARRAYHelper.type ();
  }

}
