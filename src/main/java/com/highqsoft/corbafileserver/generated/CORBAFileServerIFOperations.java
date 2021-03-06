package com.highqsoft.corbafileserver.generated;


/**
* com/highqsoft/corbafileserver/generated/CORBAFileServerIFOperations.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from src/main/idl/corbafileserver.idl
* Donnerstag, 16. Juni 2016 10:30 Uhr MESZ
*/

public interface CORBAFileServerIFOperations 
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
  String save (org.asam.ods.AoSession aoSess, String name, String subDir, com.highqsoft.corbafileserver.generated.InputStreamIF stream) throws com.highqsoft.corbafileserver.generated.CORBAFileServerException;

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
  String saveForInstance (org.asam.ods.AoSession aoSess, String name, String subDir, org.asam.ods.T_LONGLONG aid, org.asam.ods.T_LONGLONG iid, com.highqsoft.corbafileserver.generated.InputStreamIF stream) throws com.highqsoft.corbafileserver.generated.CORBAFileServerException;

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
  String saveForInstanceName (org.asam.ods.AoSession aoSess, String name, String subDir, String aeName, String ieName, com.highqsoft.corbafileserver.generated.InputStreamIF stream) throws com.highqsoft.corbafileserver.generated.CORBAFileServerException;

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
  void delete (org.asam.ods.AoSession aoSess, String name) throws com.highqsoft.corbafileserver.generated.CORBAFileServerException;

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
  void move (org.asam.ods.AoSession aoSess, String url) throws com.highqsoft.corbafileserver.generated.CORBAFileServerException;

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
  void deleteForInstance (org.asam.ods.AoSession aoSess, String url, org.asam.ods.T_LONGLONG aid, org.asam.ods.T_LONGLONG iid) throws com.highqsoft.corbafileserver.generated.CORBAFileServerException;

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
  void moveForInstance (org.asam.ods.AoSession aoSess, String name, org.asam.ods.T_LONGLONG aid, org.asam.ods.T_LONGLONG iid) throws com.highqsoft.corbafileserver.generated.CORBAFileServerException;

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
  void getBySocket (org.asam.ods.AoSession aoSess, String name, String host, int aPort) throws com.highqsoft.corbafileserver.generated.CORBAFileServerException;

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
  void getForInstanceBySocket (org.asam.ods.AoSession aoSess, String name, org.asam.ods.T_LONGLONG aid, org.asam.ods.T_LONGLONG iid, String host, int aPort) throws com.highqsoft.corbafileserver.generated.CORBAFileServerException;

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
  String saveBySocket (org.asam.ods.AoSession aoSess, String name, String subDir, String host, int aPort) throws com.highqsoft.corbafileserver.generated.CORBAFileServerException;

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
  String saveForInstanceBySocket (org.asam.ods.AoSession aoSess, String name, String subDir, org.asam.ods.T_LONGLONG aid, org.asam.ods.T_LONGLONG iid, String host, int aPort) throws com.highqsoft.corbafileserver.generated.CORBAFileServerException;

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
  String saveForInstanceNameBySocket (org.asam.ods.AoSession aoSess, String name, String subDir, String aeName, String ieName, String host, int aPort) throws com.highqsoft.corbafileserver.generated.CORBAFileServerException;

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
  com.highqsoft.corbafileserver.generated.InputStreamIF read (org.asam.ods.AoSession aoSess, String name) throws com.highqsoft.corbafileserver.generated.CORBAFileServerException;

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
  com.highqsoft.corbafileserver.generated.InputStreamIF readForInstance (org.asam.ods.AoSession aoSess, String name, org.asam.ods.T_LONGLONG aid, org.asam.ods.T_LONGLONG iid) throws com.highqsoft.corbafileserver.generated.CORBAFileServerException;

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
  long getSize (org.asam.ods.AoSession aoSess, String name) throws com.highqsoft.corbafileserver.generated.CORBAFileServerException;

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
  long getSizeForInstance (org.asam.ods.AoSession aoSess, String name, org.asam.ods.T_LONGLONG aid, org.asam.ods.T_LONGLONG iid) throws com.highqsoft.corbafileserver.generated.CORBAFileServerException;

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
  void terminate (org.asam.ods.AoSession aoSess, String name, String parameter) throws com.highqsoft.corbafileserver.generated.CORBAFileServerException;

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
  void terminateForInstance (org.asam.ods.AoSession aoSess, String name, org.asam.ods.T_LONGLONG aid, org.asam.ods.T_LONGLONG iid, String parameter) throws com.highqsoft.corbafileserver.generated.CORBAFileServerException;

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
  String getHostname (org.asam.ods.AoSession aoSess) throws com.highqsoft.corbafileserver.generated.CORBAFileServerException;

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
  String getContext (org.asam.ods.AoSession aoSess, String key) throws com.highqsoft.corbafileserver.generated.CORBAFileServerException;

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
  void setContext (org.asam.ods.AoSession aoSess, String key, String value) throws com.highqsoft.corbafileserver.generated.CORBAFileServerException;

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
  void removeContext (org.asam.ods.AoSession aoSess, String key) throws com.highqsoft.corbafileserver.generated.CORBAFileServerException;

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
  String[] listContext (org.asam.ods.AoSession aoSess) throws com.highqsoft.corbafileserver.generated.CORBAFileServerException;

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
  String getInterfaceVersion () throws com.highqsoft.corbafileserver.generated.CORBAFileServerException;

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
  long[] getSizes (org.asam.ods.AoSession aoSess, String[] names) throws com.highqsoft.corbafileserver.generated.CORBAFileServerException;
} // interface CORBAFileServerIFOperations
