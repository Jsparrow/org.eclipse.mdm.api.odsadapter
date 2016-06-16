package com.highqsoft.corbafileserver.generated;


/**
* com/highqsoft/corbafileserver/generated/CORBAFileServerIFPOA.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from src/main/idl/corbafileserver.idl
* Donnerstag, 16. Juni 2016 10:30 Uhr MESZ
*/

public abstract class CORBAFileServerIFPOA extends org.omg.PortableServer.Servant
 implements com.highqsoft.corbafileserver.generated.CORBAFileServerIFOperations, org.omg.CORBA.portable.InvokeHandler
{

  // Constructors

  private static java.util.Hashtable _methods = new java.util.Hashtable ();
  static
  {
    _methods.put ("save", new java.lang.Integer (0));
    _methods.put ("saveForInstance", new java.lang.Integer (1));
    _methods.put ("saveForInstanceName", new java.lang.Integer (2));
    _methods.put ("delete", new java.lang.Integer (3));
    _methods.put ("move", new java.lang.Integer (4));
    _methods.put ("deleteForInstance", new java.lang.Integer (5));
    _methods.put ("moveForInstance", new java.lang.Integer (6));
    _methods.put ("getBySocket", new java.lang.Integer (7));
    _methods.put ("getForInstanceBySocket", new java.lang.Integer (8));
    _methods.put ("saveBySocket", new java.lang.Integer (9));
    _methods.put ("saveForInstanceBySocket", new java.lang.Integer (10));
    _methods.put ("saveForInstanceNameBySocket", new java.lang.Integer (11));
    _methods.put ("read", new java.lang.Integer (12));
    _methods.put ("readForInstance", new java.lang.Integer (13));
    _methods.put ("getSize", new java.lang.Integer (14));
    _methods.put ("getSizeForInstance", new java.lang.Integer (15));
    _methods.put ("terminate", new java.lang.Integer (16));
    _methods.put ("terminateForInstance", new java.lang.Integer (17));
    _methods.put ("getHostname", new java.lang.Integer (18));
    _methods.put ("getContext", new java.lang.Integer (19));
    _methods.put ("setContext", new java.lang.Integer (20));
    _methods.put ("removeContext", new java.lang.Integer (21));
    _methods.put ("listContext", new java.lang.Integer (22));
    _methods.put ("getInterfaceVersion", new java.lang.Integer (23));
    _methods.put ("getSizes", new java.lang.Integer (24));
  }

  public org.omg.CORBA.portable.OutputStream _invoke (String $method,
                                org.omg.CORBA.portable.InputStream in,
                                org.omg.CORBA.portable.ResponseHandler $rh)
  {
    org.omg.CORBA.portable.OutputStream out = null;
    java.lang.Integer __method = (java.lang.Integer)_methods.get ($method);
    if (__method == null)
      throw new org.omg.CORBA.BAD_OPERATION (0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);

    switch (__method.intValue ())
    {

  /**
        * Save the data associated with the given intput stream.
        *
        * @throws CORBAFileServerException
        * with the following possible error codes:
        *    FILESERVER_CONNECT_FAILED
        *    FILESERVER_BAD_PARAMETER
        *    FILESERVER_CONNECTION_LOST
        *    FILESERVER_IMPLEMENTATION_PROBLEM
        *    FILESERVER_NOT_IMPLEMENTED
        *    FILESERVER_NO_MEMORY
        *
        * @param  aoSess the ASAM ODS session.
        * @param  name the name of the file.
        * @param  subDir an alternative sub directory, that can be specified,
        *                if the filename should not used to determine the destination folder.
        * @param  stream the input stream, ready to read by the server.
        * @return the url string of the created file.
        */
       case 0:  // com/highqsoft/corbafileserver/generated/CORBAFileServerIF/save
       {
         try {
           org.asam.ods.AoSession aoSess = org.asam.ods.AoSessionHelper.read (in);
           String name = in.read_string ();
           String subDir = in.read_string ();
           com.highqsoft.corbafileserver.generated.InputStreamIF stream = com.highqsoft.corbafileserver.generated.InputStreamIFHelper.read (in);
           String $result = null;
           $result = this.save (aoSess, name, subDir, stream);
           out = $rh.createReply();
           out.write_string ($result);
         } catch (com.highqsoft.corbafileserver.generated.CORBAFileServerException $ex) {
           out = $rh.createExceptionReply ();
           com.highqsoft.corbafileserver.generated.CORBAFileServerExceptionHelper.write (out, $ex);
         }
         break;
       }


  /**
        * Save the data associated with the given intput stream.
        * Specify the ApplicationElement id and the InstanceElement id
        * of the component that holds the external reference.
        *
        * @throws CORBAFileServerException
        * with the following possible error codes:
        *    FILESERVER_CONNECT_FAILED
        *    FILESERVER_BAD_PARAMETER
        *    FILESERVER_CONNECTION_LOST
        *    FILESERVER_IMPLEMENTATION_PROBLEM
        *    FILESERVER_NOT_IMPLEMENTED
        *    FILESERVER_NO_MEMORY
        *
        * @param  aoSess the ASAM ODS session.
        * @param  name the name of the file.
        * @param  subDir an alternative sub directory, that can be specified,
        *                if the filename should not used to determine the destination folder.
        * @param  aid the application element id.
        * @param  iid the instance element id.
        * @param  stream the input stream, ready to read by the server.
        * @return the url string of the created file.
        */
       case 1:  // com/highqsoft/corbafileserver/generated/CORBAFileServerIF/saveForInstance
       {
         try {
           org.asam.ods.AoSession aoSess = org.asam.ods.AoSessionHelper.read (in);
           String name = in.read_string ();
           String subDir = in.read_string ();
           org.asam.ods.T_LONGLONG aid = org.asam.ods.T_LONGLONGHelper.read (in);
           org.asam.ods.T_LONGLONG iid = org.asam.ods.T_LONGLONGHelper.read (in);
           com.highqsoft.corbafileserver.generated.InputStreamIF stream = com.highqsoft.corbafileserver.generated.InputStreamIFHelper.read (in);
           String $result = null;
           $result = this.saveForInstance (aoSess, name, subDir, aid, iid, stream);
           out = $rh.createReply();
           out.write_string ($result);
         } catch (com.highqsoft.corbafileserver.generated.CORBAFileServerException $ex) {
           out = $rh.createExceptionReply ();
           com.highqsoft.corbafileserver.generated.CORBAFileServerExceptionHelper.write (out, $ex);
         }
         break;
       }


  /**
        * Save the data associated with the given intput stream.
        * Specify the name of an applciation element and the name of the instance element
        * that holds the external reference
        *
        * @throws CORBAFileServerException
        * with the following possible error codes:
        *    FILESERVER_CONNECT_FAILED
        *    FILESERVER_BAD_PARAMETER
        *    FILESERVER_CONNECTION_LOST
        *    FILESERVER_IMPLEMENTATION_PROBLEM
        *    FILESERVER_NOT_IMPLEMENTED
        *    FILESERVER_NO_MEMORY
        *
        * @param  aoSess the ASAM ODS session.
        * @param  name the name of the file.
        * @param  subDir an alternative sub directory, that can be specified,
        *                if the filename should not used to determine the destination folder.
        * @param  aeName the application element name.
        * @param  ieName the instance element name.
        * @param  stream the input stream, ready to read by the server.
        * @return the url string of the created file.
        */
       case 2:  // com/highqsoft/corbafileserver/generated/CORBAFileServerIF/saveForInstanceName
       {
         try {
           org.asam.ods.AoSession aoSess = org.asam.ods.AoSessionHelper.read (in);
           String name = in.read_string ();
           String subDir = in.read_string ();
           String aeName = in.read_string ();
           String ieName = in.read_string ();
           com.highqsoft.corbafileserver.generated.InputStreamIF stream = com.highqsoft.corbafileserver.generated.InputStreamIFHelper.read (in);
           String $result = null;
           $result = this.saveForInstanceName (aoSess, name, subDir, aeName, ieName, stream);
           out = $rh.createReply();
           out.write_string ($result);
         } catch (com.highqsoft.corbafileserver.generated.CORBAFileServerException $ex) {
           out = $rh.createExceptionReply ();
           com.highqsoft.corbafileserver.generated.CORBAFileServerExceptionHelper.write (out, $ex);
         }
         break;
       }


  /**
        * Delete the data associated with the given name.
        *
        * @throws CORBAFileServerException
        * with the following possible error codes:
        *    FILESERVER_CONNECT_FAILED
        *    FILESERVER_BAD_PARAMETER
        *    FILESERVER_CONNECTION_LOST
        *    FILESERVER_IMPLEMENTATION_PROBLEM
        *    FILESERVER_NOT_IMPLEMENTED
        *    FILESERVER_NO_MEMORY
        *
        * @param  aoSess the ASAM ODS session.
        * @param  name the name of the file.
        */
       case 3:  // com/highqsoft/corbafileserver/generated/CORBAFileServerIF/delete
       {
         try {
           org.asam.ods.AoSession aoSess = org.asam.ods.AoSessionHelper.read (in);
           String name = in.read_string ();
           this.delete (aoSess, name);
           out = $rh.createReply();
         } catch (com.highqsoft.corbafileserver.generated.CORBAFileServerException $ex) {
           out = $rh.createExceptionReply ();
           com.highqsoft.corbafileserver.generated.CORBAFileServerExceptionHelper.write (out, $ex);
         }
         break;
       }


  /**
        * Move the data associated with the given name.
        *
        * @throws CORBAFileServerException
        * with the following possible error codes:
        *    FILESERVER_CONNECT_FAILED
        *    FILESERVER_BAD_PARAMETER
        *    FILESERVER_CONNECTION_LOST
        *    FILESERVER_IMPLEMENTATION_PROBLEM
        *    FILESERVER_NOT_IMPLEMENTED
        *    FILESERVER_NO_MEMORY
        *
        * @param  aoSess the ASAM ODS session.
        * @param  urlo the url of the file.
        */
       case 4:  // com/highqsoft/corbafileserver/generated/CORBAFileServerIF/move
       {
         try {
           org.asam.ods.AoSession aoSess = org.asam.ods.AoSessionHelper.read (in);
           String url = in.read_string ();
           this.move (aoSess, url);
           out = $rh.createReply();
         } catch (com.highqsoft.corbafileserver.generated.CORBAFileServerException $ex) {
           out = $rh.createExceptionReply ();
           com.highqsoft.corbafileserver.generated.CORBAFileServerExceptionHelper.write (out, $ex);
         }
         break;
       }


  /**
        * Delete the data associated with the given name.
        * Specify the ApplicationElement id and the InstanceElement id
        * of the component that holds the external reference.
        *
        * @throws CORBAFileServerException
        * with the following possible error codes:
        *    FILESERVER_CONNECT_FAILED
        *    FILESERVER_BAD_PARAMETER
        *    FILESERVER_CONNECTION_LOST
        *    FILESERVER_IMPLEMENTATION_PROBLEM
        *    FILESERVER_NOT_IMPLEMENTED
        *    FILESERVER_NO_MEMORY
        *
        * @param  aoSess the ASAM ODS session.
        * @param  url the url of the file.
        * @param  aid the application element id.
        * @param  iid the instance element id.
        */
       case 5:  // com/highqsoft/corbafileserver/generated/CORBAFileServerIF/deleteForInstance
       {
         try {
           org.asam.ods.AoSession aoSess = org.asam.ods.AoSessionHelper.read (in);
           String url = in.read_string ();
           org.asam.ods.T_LONGLONG aid = org.asam.ods.T_LONGLONGHelper.read (in);
           org.asam.ods.T_LONGLONG iid = org.asam.ods.T_LONGLONGHelper.read (in);
           this.deleteForInstance (aoSess, url, aid, iid);
           out = $rh.createReply();
         } catch (com.highqsoft.corbafileserver.generated.CORBAFileServerException $ex) {
           out = $rh.createExceptionReply ();
           com.highqsoft.corbafileserver.generated.CORBAFileServerExceptionHelper.write (out, $ex);
         }
         break;
       }


  /**
        * Move the data associated with the given name.
        * Specify the ApplicationElement id and the InstanceElement id
        * of the component that holds the external reference.
        *
        * @throws CORBAFileServerException
        * with the following possible error codes:
        *    FILESERVER_CONNECT_FAILED
        *    FILESERVER_BAD_PARAMETER
        *    FILESERVER_CONNECTION_LOST
        *    FILESERVER_IMPLEMENTATION_PROBLEM
        *    FILESERVER_NOT_IMPLEMENTED
        *    FILESERVER_NO_MEMORY
        *
        * @param  aoSess the ASAM ODS session.
        * @param  name the name of the file.
        * @param  aid the application element id.
        * @param  iid the instance element id.
        */
       case 6:  // com/highqsoft/corbafileserver/generated/CORBAFileServerIF/moveForInstance
       {
         try {
           org.asam.ods.AoSession aoSess = org.asam.ods.AoSessionHelper.read (in);
           String name = in.read_string ();
           org.asam.ods.T_LONGLONG aid = org.asam.ods.T_LONGLONGHelper.read (in);
           org.asam.ods.T_LONGLONG iid = org.asam.ods.T_LONGLONGHelper.read (in);
           this.moveForInstance (aoSess, name, aid, iid);
           out = $rh.createReply();
         } catch (com.highqsoft.corbafileserver.generated.CORBAFileServerException $ex) {
           out = $rh.createExceptionReply ();
           com.highqsoft.corbafileserver.generated.CORBAFileServerExceptionHelper.write (out, $ex);
         }
         break;
       }


  /**
         * Get the data using a socket.
         *
         * @throws CORBAFileServerException
         * with the following possible error codes:
         *    FILESERVER_CONNECT_FAILED
         *    FILESERVER_BAD_PARAMETER
         *    FILESERVER_CONNECTION_LOST
         *    FILESERVER_IMPLEMENTATION_PROBLEM
         *    FILESERVER_NOT_IMPLEMENTED
         *    FILESERVER_NO_MEMORY
         *
         * @param  aoSess the ASAM ODS session.
         * @param  name the name of the file.
         * @param  host the hostname for the socket connection.
         * @param  port the port for the socket connection.
         */
       case 7:  // com/highqsoft/corbafileserver/generated/CORBAFileServerIF/getBySocket
       {
         try {
           org.asam.ods.AoSession aoSess = org.asam.ods.AoSessionHelper.read (in);
           String name = in.read_string ();
           String host = in.read_string ();
           int aPort = in.read_long ();
           this.getBySocket (aoSess, name, host, aPort);
           out = $rh.createReply();
         } catch (com.highqsoft.corbafileserver.generated.CORBAFileServerException $ex) {
           out = $rh.createExceptionReply ();
           com.highqsoft.corbafileserver.generated.CORBAFileServerExceptionHelper.write (out, $ex);
         }
         break;
       }


  /**
         * Get the data using a socket.
         *
         * @throws CORBAFileServerException
         * with the following possible error codes:
         *    FILESERVER_CONNECT_FAILED
         *    FILESERVER_BAD_PARAMETER
         *    FILESERVER_CONNECTION_LOST
         *    FILESERVER_IMPLEMENTATION_PROBLEM
         *    FILESERVER_NOT_IMPLEMENTED
         *    FILESERVER_NO_MEMORY
         *
         * @param  aoSess the ASAM ODS session.
         * @param  name the url specification of the file.
         * @param  aid the application element id.
         * @param  iid the instance element id.
         * @param  host the hostname for the socket connection.
         * @param  port the port for the socket connection.
         */
       case 8:  // com/highqsoft/corbafileserver/generated/CORBAFileServerIF/getForInstanceBySocket
       {
         try {
           org.asam.ods.AoSession aoSess = org.asam.ods.AoSessionHelper.read (in);
           String name = in.read_string ();
           org.asam.ods.T_LONGLONG aid = org.asam.ods.T_LONGLONGHelper.read (in);
           org.asam.ods.T_LONGLONG iid = org.asam.ods.T_LONGLONGHelper.read (in);
           String host = in.read_string ();
           int aPort = in.read_long ();
           this.getForInstanceBySocket (aoSess, name, aid, iid, host, aPort);
           out = $rh.createReply();
         } catch (com.highqsoft.corbafileserver.generated.CORBAFileServerException $ex) {
           out = $rh.createExceptionReply ();
           com.highqsoft.corbafileserver.generated.CORBAFileServerExceptionHelper.write (out, $ex);
         }
         break;
       }


  /**
         * Save the data using a socket.
         *
         * @throws CORBAFileServerException
         * with the following possible error codes:
         *    FILESERVER_CONNECT_FAILED
         *    FILESERVER_BAD_PARAMETER
         *    FILESERVER_CONNECTION_LOST
         *    FILESERVER_IMPLEMENTATION_PROBLEM
         *    FILESERVER_NOT_IMPLEMENTED
         *    FILESERVER_NO_MEMORY
         *
         * @param  aoSess the ASAM ODS session.
         * @param  name the name of the file.
         * @param  subDir an alternative sub directory, that can be specified,
         *                if the filename should not used to determine the destination folder.
         * @param  host the hostname for the socket connection.
         * @param  port the port for the socket connection.
         * @return the url string of the created file.
         */
       case 9:  // com/highqsoft/corbafileserver/generated/CORBAFileServerIF/saveBySocket
       {
         try {
           org.asam.ods.AoSession aoSess = org.asam.ods.AoSessionHelper.read (in);
           String name = in.read_string ();
           String subDir = in.read_string ();
           String host = in.read_string ();
           int aPort = in.read_long ();
           String $result = null;
           $result = this.saveBySocket (aoSess, name, subDir, host, aPort);
           out = $rh.createReply();
           out.write_string ($result);
         } catch (com.highqsoft.corbafileserver.generated.CORBAFileServerException $ex) {
           out = $rh.createExceptionReply ();
           com.highqsoft.corbafileserver.generated.CORBAFileServerExceptionHelper.write (out, $ex);
         }
         break;
       }


  /**
         * Save the data using a socket.
         *
         * @throws CORBAFileServerException
         * with the following possible error codes:
         *    FILESERVER_CONNECT_FAILED
         *    FILESERVER_BAD_PARAMETER
         *    FILESERVER_CONNECTION_LOST
         *    FILESERVER_IMPLEMENTATION_PROBLEM
         *    FILESERVER_NOT_IMPLEMENTED
         *    FILESERVER_NO_MEMORY
         *
         * @param  aoSess the ASAM ODS session.
         * @param  name the name of the file.
         * @param  subDir an alternative sub directory, that can be specified,
         *                if the filename should not used to determine the destination folder.
         * @param  aid the application element id.
         * @param  iid the instance element id.
         * @param  host the hostname for the socket connection.
         * @param  port the port for the socket connection.
         * @return the url string of the created file.
         */
       case 10:  // com/highqsoft/corbafileserver/generated/CORBAFileServerIF/saveForInstanceBySocket
       {
         try {
           org.asam.ods.AoSession aoSess = org.asam.ods.AoSessionHelper.read (in);
           String name = in.read_string ();
           String subDir = in.read_string ();
           org.asam.ods.T_LONGLONG aid = org.asam.ods.T_LONGLONGHelper.read (in);
           org.asam.ods.T_LONGLONG iid = org.asam.ods.T_LONGLONGHelper.read (in);
           String host = in.read_string ();
           int aPort = in.read_long ();
           String $result = null;
           $result = this.saveForInstanceBySocket (aoSess, name, subDir, aid, iid, host, aPort);
           out = $rh.createReply();
           out.write_string ($result);
         } catch (com.highqsoft.corbafileserver.generated.CORBAFileServerException $ex) {
           out = $rh.createExceptionReply ();
           com.highqsoft.corbafileserver.generated.CORBAFileServerExceptionHelper.write (out, $ex);
         }
         break;
       }


  /**
         * Save the data using a socket.
         *
         * @throws CORBAFileServerException
         * with the following possible error codes:
         *    FILESERVER_CONNECT_FAILED
         *    FILESERVER_BAD_PARAMETER
         *    FILESERVER_CONNECTION_LOST
         *    FILESERVER_IMPLEMENTATION_PROBLEM
         *    FILESERVER_NOT_IMPLEMENTED
         *    FILESERVER_NO_MEMORY
         *
         * @param  aoSess the ASAM ODS session.
         * @param  name the name of the file.
         * @param  subDir an alternative sub directory, that can be specified,
         *                if the filename should not used to determine the destination folder.
         * @param  aeName the application element name.
         * @param  ieName the instance element name.
         * @param  host the hostname for the socket connection.
         * @param  port the port for the socket connection.
         * @return the url string of the created file.
         */
       case 11:  // com/highqsoft/corbafileserver/generated/CORBAFileServerIF/saveForInstanceNameBySocket
       {
         try {
           org.asam.ods.AoSession aoSess = org.asam.ods.AoSessionHelper.read (in);
           String name = in.read_string ();
           String subDir = in.read_string ();
           String aeName = in.read_string ();
           String ieName = in.read_string ();
           String host = in.read_string ();
           int aPort = in.read_long ();
           String $result = null;
           $result = this.saveForInstanceNameBySocket (aoSess, name, subDir, aeName, ieName, host, aPort);
           out = $rh.createReply();
           out.write_string ($result);
         } catch (com.highqsoft.corbafileserver.generated.CORBAFileServerException $ex) {
           out = $rh.createExceptionReply ();
           com.highqsoft.corbafileserver.generated.CORBAFileServerExceptionHelper.write (out, $ex);
         }
         break;
       }


  /**
        * Read the data associated with the given name.
        *
        * @throws CORBAFileServerException
        * with the following possible error codes:
        *    FILESERVER_CONNECT_FAILED
        *    FILESERVER_BAD_PARAMETER
        *    FILESERVER_CONNECTION_LOST
        *    FILESERVER_IMPLEMENTATION_PROBLEM
        *    FILESERVER_NOT_IMPLEMENTED
        *    FILESERVER_NO_MEMORY
        *
        * @param  aoSess the ASAM ODS session.
        * @param  name the name of the file.
        * @param  stream the input stream, ready to read by the server.
        */
       case 12:  // com/highqsoft/corbafileserver/generated/CORBAFileServerIF/read
       {
         try {
           org.asam.ods.AoSession aoSess = org.asam.ods.AoSessionHelper.read (in);
           String name = in.read_string ();
           com.highqsoft.corbafileserver.generated.InputStreamIF $result = null;
           $result = this.read (aoSess, name);
           out = $rh.createReply();
           com.highqsoft.corbafileserver.generated.InputStreamIFHelper.write (out, $result);
         } catch (com.highqsoft.corbafileserver.generated.CORBAFileServerException $ex) {
           out = $rh.createExceptionReply ();
           com.highqsoft.corbafileserver.generated.CORBAFileServerExceptionHelper.write (out, $ex);
         }
         break;
       }


  /**
        * Read the data associated with the given name.
        *
        * @throws CORBAFileServerException
        * with the following possible error codes:
        *    FILESERVER_CONNECT_FAILED
        *    FILESERVER_BAD_PARAMETER
        *    FILESERVER_CONNECTION_LOST
        *    FILESERVER_IMPLEMENTATION_PROBLEM
        *    FILESERVER_NOT_IMPLEMENTED
        *    FILESERVER_NO_MEMORY
        *
        * @param  aoSess the ASAM ODS session.
        * @param  name the name of the file.
        * @param  aid the application element id.
        * @param  iid the instance element id.
        * @param  stream the input stream, ready to read by the server.
        */
       case 13:  // com/highqsoft/corbafileserver/generated/CORBAFileServerIF/readForInstance
       {
         try {
           org.asam.ods.AoSession aoSess = org.asam.ods.AoSessionHelper.read (in);
           String name = in.read_string ();
           org.asam.ods.T_LONGLONG aid = org.asam.ods.T_LONGLONGHelper.read (in);
           org.asam.ods.T_LONGLONG iid = org.asam.ods.T_LONGLONGHelper.read (in);
           com.highqsoft.corbafileserver.generated.InputStreamIF $result = null;
           $result = this.readForInstance (aoSess, name, aid, iid);
           out = $rh.createReply();
           com.highqsoft.corbafileserver.generated.InputStreamIFHelper.write (out, $result);
         } catch (com.highqsoft.corbafileserver.generated.CORBAFileServerException $ex) {
           out = $rh.createExceptionReply ();
           com.highqsoft.corbafileserver.generated.CORBAFileServerExceptionHelper.write (out, $ex);
         }
         break;
       }


  /**
        *  Get size of the file associated with the given name.
        *
        * @throws CORBAFileServerException
        * with the following possible error codes:
        *    FILESERVER_CONNECT_FAILED
        *    FILESERVER_BAD_PARAMETER
        *    FILESERVER_CONNECTION_LOST
        *    FILESERVER_IMPLEMENTATION_PROBLEM
        *    FILESERVER_NOT_IMPLEMENTED
        *    FILESERVER_NO_MEMORY
        *
        * @param  aoSess the ASAM ODS session.
        * @param  name the name of the file.
        * @param  size the size of the input stream.
        */
       case 14:  // com/highqsoft/corbafileserver/generated/CORBAFileServerIF/getSize
       {
         try {
           org.asam.ods.AoSession aoSess = org.asam.ods.AoSessionHelper.read (in);
           String name = in.read_string ();
           long $result = (long)0;
           $result = this.getSize (aoSess, name);
           out = $rh.createReply();
           out.write_longlong ($result);
         } catch (com.highqsoft.corbafileserver.generated.CORBAFileServerException $ex) {
           out = $rh.createExceptionReply ();
           com.highqsoft.corbafileserver.generated.CORBAFileServerExceptionHelper.write (out, $ex);
         }
         break;
       }


  /**
        *  Get size of the file associated with the given name.
        *
        * @throws CORBAFileServerException
        * with the following possible error codes:
        *    FILESERVER_CONNECT_FAILED
        *    FILESERVER_BAD_PARAMETER
        *    FILESERVER_CONNECTION_LOST
        *    FILESERVER_IMPLEMENTATION_PROBLEM
        *    FILESERVER_NOT_IMPLEMENTED
        *    FILESERVER_NO_MEMORY
        *
        * @param  aoSess the ASAM ODS session.
        * @param  name the name of the file.
        * @param  aid the application element id.
        * @param  iid the instance element id.
        * @param  size the size of the input stream.
        */
       case 15:  // com/highqsoft/corbafileserver/generated/CORBAFileServerIF/getSizeForInstance
       {
         try {
           org.asam.ods.AoSession aoSess = org.asam.ods.AoSessionHelper.read (in);
           String name = in.read_string ();
           org.asam.ods.T_LONGLONG aid = org.asam.ods.T_LONGLONGHelper.read (in);
           org.asam.ods.T_LONGLONG iid = org.asam.ods.T_LONGLONGHelper.read (in);
           long $result = (long)0;
           $result = this.getSizeForInstance (aoSess, name, aid, iid);
           out = $rh.createReply();
           out.write_longlong ($result);
         } catch (com.highqsoft.corbafileserver.generated.CORBAFileServerException $ex) {
           out = $rh.createExceptionReply ();
           com.highqsoft.corbafileserver.generated.CORBAFileServerExceptionHelper.write (out, $ex);
         }
         break;
       }


  /**
        * This method can be called by the client when the server should be start a termination process.
        *
        * @throws CORBAFileServerException
        * with the following possible error codes:
        *    FILESERVER_CONNECT_FAILED
        *    FILESERVER_BAD_PARAMETER
        *    FILESERVER_CONNECTION_LOST
        *    FILESERVER_IMPLEMENTATION_PROBLEM
        *    FILESERVER_NOT_IMPLEMENTED
        *    FILESERVER_NO_MEMORY
        *
        * @param  aoSess the ASAM ODS session.
        * @param  name the name of the file.
        * @param  parameter the parameter string. The content depends on the
        *         server side terminate implementation.
        */
       case 16:  // com/highqsoft/corbafileserver/generated/CORBAFileServerIF/terminate
       {
         try {
           org.asam.ods.AoSession aoSess = org.asam.ods.AoSessionHelper.read (in);
           String name = in.read_string ();
           String parameter = in.read_string ();
           this.terminate (aoSess, name, parameter);
           out = $rh.createReply();
         } catch (com.highqsoft.corbafileserver.generated.CORBAFileServerException $ex) {
           out = $rh.createExceptionReply ();
           com.highqsoft.corbafileserver.generated.CORBAFileServerExceptionHelper.write (out, $ex);
         }
         break;
       }


  /**
        * This method can be called by the client when the server should be start a termination process.
        *
        * @throws CORBAFileServerException
        * with the following possible error codes:
        *    FILESERVER_CONNECT_FAILED
        *    FILESERVER_BAD_PARAMETER
        *    FILESERVER_CONNECTION_LOST
        *    FILESERVER_IMPLEMENTATION_PROBLEM
        *    FILESERVER_NOT_IMPLEMENTED
        *    FILESERVER_NO_MEMORY
        *
        * @param  aoSess the ASAM ODS session.
        * @param  name the name of the file.
        * @param  aid the application element id.
        * @param  iid the instance element id.
        * @param  parameter the parameter string. The content depends on the
        *         server side terminate implementation.
        */
       case 17:  // com/highqsoft/corbafileserver/generated/CORBAFileServerIF/terminateForInstance
       {
         try {
           org.asam.ods.AoSession aoSess = org.asam.ods.AoSessionHelper.read (in);
           String name = in.read_string ();
           org.asam.ods.T_LONGLONG aid = org.asam.ods.T_LONGLONGHelper.read (in);
           org.asam.ods.T_LONGLONG iid = org.asam.ods.T_LONGLONGHelper.read (in);
           String parameter = in.read_string ();
           this.terminateForInstance (aoSess, name, aid, iid, parameter);
           out = $rh.createReply();
         } catch (com.highqsoft.corbafileserver.generated.CORBAFileServerException $ex) {
           out = $rh.createExceptionReply ();
           com.highqsoft.corbafileserver.generated.CORBAFileServerExceptionHelper.write (out, $ex);
         }
         break;
       }


  /**
        * Get the name of the host where the server is running
        *
        * @param  aoSess the ASAM ODS session.
        * @return the hostname
        * @throws CORBAFileServerException
        * with the following possible error codes:
        *    FILESERVER_CONNECTION_LOST
        *    FILESERVER_IMPLEMENTATION_PROBLEM
        *    FILESERVER_NOT_IMPLEMENTED
        *    FILESERVER_NO_MEMORY
        */
       case 18:  // com/highqsoft/corbafileserver/generated/CORBAFileServerIF/getHostname
       {
         try {
           org.asam.ods.AoSession aoSess = org.asam.ods.AoSessionHelper.read (in);
           String $result = null;
           $result = this.getHostname (aoSess);
           out = $rh.createReply();
           out.write_string ($result);
         } catch (com.highqsoft.corbafileserver.generated.CORBAFileServerException $ex) {
           out = $rh.createExceptionReply ();
           com.highqsoft.corbafileserver.generated.CORBAFileServerExceptionHelper.write (out, $ex);
         }
         break;
       }


  /**
        * Get a context variable.
        *
        * @param  aoSess the ASAM ODS session.
        * @param key the keyword of the context value.
        * @return the context value
        * @throws CORBAFileServerException
        * with the following possible error codes:
        *    FILESERVER_CONNECTION_LOST
        *    FILESERVER_IMPLEMENTATION_PROBLEM
        *    FILESERVER_NOT_IMPLEMENTED
        *    FILESERVER_NO_MEMORY,
        *    FILESERVER_NOT_FOUND
        */
       case 19:  // com/highqsoft/corbafileserver/generated/CORBAFileServerIF/getContext
       {
         try {
           org.asam.ods.AoSession aoSess = org.asam.ods.AoSessionHelper.read (in);
           String key = in.read_string ();
           String $result = null;
           $result = this.getContext (aoSess, key);
           out = $rh.createReply();
           out.write_string ($result);
         } catch (com.highqsoft.corbafileserver.generated.CORBAFileServerException $ex) {
           out = $rh.createExceptionReply ();
           com.highqsoft.corbafileserver.generated.CORBAFileServerExceptionHelper.write (out, $ex);
         }
         break;
       }


  /**
        * Set a context variable.
        *
        * @param  aoSess the ASAM ODS session.
        * @param key the keyword of the context value.
        * @param value the context value.
        * @throws CORBAFileServerException
        * with the following possible error codes:
        *    FILESERVER_CONNECTION_LOST
        *    FILESERVER_IMPLEMENTATION_PROBLEM
        *    FILESERVER_NOT_IMPLEMENTED
        *    FILESERVER_NO_MEMORY
        */
       case 20:  // com/highqsoft/corbafileserver/generated/CORBAFileServerIF/setContext
       {
         try {
           org.asam.ods.AoSession aoSess = org.asam.ods.AoSessionHelper.read (in);
           String key = in.read_string ();
           String value = in.read_string ();
           this.setContext (aoSess, key, value);
           out = $rh.createReply();
         } catch (com.highqsoft.corbafileserver.generated.CORBAFileServerException $ex) {
           out = $rh.createExceptionReply ();
           com.highqsoft.corbafileserver.generated.CORBAFileServerExceptionHelper.write (out, $ex);
         }
         break;
       }


  /**
        * Remove a context variable.
        *
        * @param  aoSess the ASAM ODS session.
        * @param key the keyword of the context value.
        * @throws CORBAFileServerException
        * with the following possible error codes:
        *    FILESERVER_CONNECTION_LOST
        *    FILESERVER_IMPLEMENTATION_PROBLEM
        *    FILESERVER_NOT_IMPLEMENTED
        *    FILESERVER_NO_MEMORY
        */
       case 21:  // com/highqsoft/corbafileserver/generated/CORBAFileServerIF/removeContext
       {
         try {
           org.asam.ods.AoSession aoSess = org.asam.ods.AoSessionHelper.read (in);
           String key = in.read_string ();
           this.removeContext (aoSess, key);
           out = $rh.createReply();
         } catch (com.highqsoft.corbafileserver.generated.CORBAFileServerException $ex) {
           out = $rh.createExceptionReply ();
           com.highqsoft.corbafileserver.generated.CORBAFileServerExceptionHelper.write (out, $ex);
         }
         break;
       }


  /**
        * List all context keywords.
        *
        * @param  aoSess the ASAM ODS session.
        * @return a sequence of strings.
        * @throws CORBAFileServerException
        * with the following possible error codes:
        *    FILESERVER_CONNECTION_LOST
        *    FILESERVER_IMPLEMENTATION_PROBLEM
        *    FILESERVER_NOT_IMPLEMENTED
        *    FILESERVER_NO_MEMORY
        */
       case 22:  // com/highqsoft/corbafileserver/generated/CORBAFileServerIF/listContext
       {
         try {
           org.asam.ods.AoSession aoSess = org.asam.ods.AoSessionHelper.read (in);
           String $result[] = null;
           $result = this.listContext (aoSess);
           out = $rh.createReply();
           com.highqsoft.corbafileserver.generated.DS_STRINGHelper.write (out, $result);
         } catch (com.highqsoft.corbafileserver.generated.CORBAFileServerException $ex) {
           out = $rh.createExceptionReply ();
           com.highqsoft.corbafileserver.generated.CORBAFileServerExceptionHelper.write (out, $ex);
         }
         break;
       }


  /**
        * Get the version of the CorbaFileServerIF.
        * Returns getVersion of CorbaFileServer.
        *
        * @throws CORBAFileServerException
        * with the following possible error codes:
        *    FILESERVER_CONNECTION_LOST
        *    FILESERVER_IMPLEMENTATION_PROBLEM
        *    FILESERVER_NOT_IMPLEMENTED
        *    FILESERVER_NO_MEMORY
        *
        * @return  The interface version of the CorbaFileServerIF.
        *
        */
       case 23:  // com/highqsoft/corbafileserver/generated/CORBAFileServerIF/getInterfaceVersion
       {
         try {
           String $result = null;
           $result = this.getInterfaceVersion ();
           out = $rh.createReply();
           out.write_string ($result);
         } catch (com.highqsoft.corbafileserver.generated.CORBAFileServerException $ex) {
           out = $rh.createExceptionReply ();
           com.highqsoft.corbafileserver.generated.CORBAFileServerExceptionHelper.write (out, $ex);
         }
         break;
       }


  /**
        * Returns an array of long values representing the length
        * of the files that were provided in the String array.
        * The order of the long values must match with the order
        * of the filenames.
        *
        * @param aoSess the aoSession of the caller
        * @param names the String array of filenames for 
        *              which to get the sizes
        * @return an Array of long values containing the file sizes
        * @throws CORBAFileServerException
        * with the following possible error codes:
        *    FILESERVER_CONNECTION_LOST
        *    FILESERVER_IMPLEMENTATION_PROBLEM
        *    FILESERVER_NOT_IMPLEMENTED
        *    FILESERVER_NO_MEMORY
        *
        *
        */
       case 24:  // com/highqsoft/corbafileserver/generated/CORBAFileServerIF/getSizes
       {
         try {
           org.asam.ods.AoSession aoSess = org.asam.ods.AoSessionHelper.read (in);
           String names[] = com.highqsoft.corbafileserver.generated.DS_STRINGHelper.read (in);
           long $result[] = null;
           $result = this.getSizes (aoSess, names);
           out = $rh.createReply();
           com.highqsoft.corbafileserver.generated.LONG_ARRAYHelper.write (out, $result);
         } catch (com.highqsoft.corbafileserver.generated.CORBAFileServerException $ex) {
           out = $rh.createExceptionReply ();
           com.highqsoft.corbafileserver.generated.CORBAFileServerExceptionHelper.write (out, $ex);
         }
         break;
       }

       default:
         throw new org.omg.CORBA.BAD_OPERATION (0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);
    }

    return out;
  } // _invoke

  // Type-specific CORBA::Object operations
  private static String[] __ids = {
    "IDL:com/highqsoft/corbafileserver/generated/CORBAFileServerIF:1.0"};

  public String[] _all_interfaces (org.omg.PortableServer.POA poa, byte[] objectId)
  {
    return (String[])__ids.clone ();
  }

  public CORBAFileServerIF _this() 
  {
    return CORBAFileServerIFHelper.narrow(
    super._this_object());
  }

  public CORBAFileServerIF _this(org.omg.CORBA.ORB orb) 
  {
    return CORBAFileServerIFHelper.narrow(
    super._this_object(orb));
  }


} // class CORBAFileServerIFPOA