package com.highqsoft.corbafileserver.generated;

/**
* com/highqsoft/corbafileserver/generated/InputStreamIFHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from src/main/idl/corbafileserver.idl
* Donnerstag, 16. Juni 2016 10:30 Uhr MESZ
*/

public final class InputStreamIFHolder implements org.omg.CORBA.portable.Streamable
{
  public com.highqsoft.corbafileserver.generated.InputStreamIF value = null;

  public InputStreamIFHolder ()
  {
  }

  public InputStreamIFHolder (com.highqsoft.corbafileserver.generated.InputStreamIF initialValue)
  {
    value = initialValue;
  }

  @Override
public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = com.highqsoft.corbafileserver.generated.InputStreamIFHelper.read (i);
  }

  @Override
public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    com.highqsoft.corbafileserver.generated.InputStreamIFHelper.write (o, value);
  }

  @Override
public org.omg.CORBA.TypeCode _type ()
  {
    return com.highqsoft.corbafileserver.generated.InputStreamIFHelper.type ();
  }

}
