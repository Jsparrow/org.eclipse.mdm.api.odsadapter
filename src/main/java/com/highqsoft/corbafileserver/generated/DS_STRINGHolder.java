package com.highqsoft.corbafileserver.generated;


/**
* com/highqsoft/corbafileserver/generated/DS_STRINGHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from src/main/idl/corbafileserver.idl
* Donnerstag, 16. Juni 2016 10:30 Uhr MESZ
*/

public final class DS_STRINGHolder implements org.omg.CORBA.portable.Streamable
{
  public String value[] = null;

  public DS_STRINGHolder ()
  {
  }

  public DS_STRINGHolder (String[] initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = com.highqsoft.corbafileserver.generated.DS_STRINGHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    com.highqsoft.corbafileserver.generated.DS_STRINGHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return com.highqsoft.corbafileserver.generated.DS_STRINGHelper.type ();
  }

}