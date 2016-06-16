package com.highqsoft.corbafileserver.generated;


/**
* com/highqsoft/corbafileserver/generated/CORBAFileServerIFHelper.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from src/main/idl/corbafileserver.idl
* Donnerstag, 16. Juni 2016 10:30 Uhr MESZ
*/

abstract public class CORBAFileServerIFHelper
{
  private static String  _id = "IDL:com/highqsoft/corbafileserver/generated/CORBAFileServerIF:1.0";

  public static void insert (org.omg.CORBA.Any a, com.highqsoft.corbafileserver.generated.CORBAFileServerIF that)
  {
    org.omg.CORBA.portable.OutputStream out = a.create_output_stream ();
    a.type (type ());
    write (out, that);
    a.read_value (out.create_input_stream (), type ());
  }

  public static com.highqsoft.corbafileserver.generated.CORBAFileServerIF extract (org.omg.CORBA.Any a)
  {
    return read (a.create_input_stream ());
  }

  private static org.omg.CORBA.TypeCode __typeCode = null;
  synchronized public static org.omg.CORBA.TypeCode type ()
  {
    if (__typeCode == null)
    {
      __typeCode = org.omg.CORBA.ORB.init ().create_interface_tc (com.highqsoft.corbafileserver.generated.CORBAFileServerIFHelper.id (), "CORBAFileServerIF");
    }
    return __typeCode;
  }

  public static String id ()
  {
    return _id;
  }

  public static com.highqsoft.corbafileserver.generated.CORBAFileServerIF read (org.omg.CORBA.portable.InputStream istream)
  {
    return narrow (istream.read_Object (_CORBAFileServerIFStub.class));
  }

  public static void write (org.omg.CORBA.portable.OutputStream ostream, com.highqsoft.corbafileserver.generated.CORBAFileServerIF value)
  {
    ostream.write_Object ((org.omg.CORBA.Object) value);
  }

  public static com.highqsoft.corbafileserver.generated.CORBAFileServerIF narrow (org.omg.CORBA.Object obj)
  {
    if (obj == null)
      return null;
    else if (obj instanceof com.highqsoft.corbafileserver.generated.CORBAFileServerIF)
      return (com.highqsoft.corbafileserver.generated.CORBAFileServerIF)obj;
    else if (!obj._is_a (id ()))
      throw new org.omg.CORBA.BAD_PARAM ();
    else
    {
      org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate ();
      com.highqsoft.corbafileserver.generated._CORBAFileServerIFStub stub = new com.highqsoft.corbafileserver.generated._CORBAFileServerIFStub ();
      stub._set_delegate(delegate);
      return stub;
    }
  }

  public static com.highqsoft.corbafileserver.generated.CORBAFileServerIF unchecked_narrow (org.omg.CORBA.Object obj)
  {
    if (obj == null)
      return null;
    else if (obj instanceof com.highqsoft.corbafileserver.generated.CORBAFileServerIF)
      return (com.highqsoft.corbafileserver.generated.CORBAFileServerIF)obj;
    else
    {
      org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate ();
      com.highqsoft.corbafileserver.generated._CORBAFileServerIFStub stub = new com.highqsoft.corbafileserver.generated._CORBAFileServerIFStub ();
      stub._set_delegate(delegate);
      return stub;
    }
  }

}