package com.highqsoft.corbafileserver.generated;


/**
* com/highqsoft/corbafileserver/generated/InputStreamIFPOATie.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from src/main/idl/corbafileserver.idl
* Donnerstag, 16. Juni 2016 10:30 Uhr MESZ
*/

public class InputStreamIFPOATie extends InputStreamIFPOA
{

  // Constructors

  public InputStreamIFPOATie ( com.highqsoft.corbafileserver.generated.InputStreamIFOperations delegate ) {
      this._impl = delegate;
  }
  public InputStreamIFPOATie ( com.highqsoft.corbafileserver.generated.InputStreamIFOperations delegate , org.omg.PortableServer.POA poa ) {
      this._impl = delegate;
      this._poa      = poa;
  }
  public com.highqsoft.corbafileserver.generated.InputStreamIFOperations _delegate() {
      return this._impl;
  }
  public void _delegate (com.highqsoft.corbafileserver.generated.InputStreamIFOperations delegate ) {
      this._impl = delegate;
  }
  @Override
public org.omg.PortableServer.POA _default_POA() {
      if(_poa != null) {
          return _poa;
      }
      else {
          return super._default_POA();
      }
  }

  /**
        * Reads up to len bytes of data from the input stream into an array
        * of bytes. An attempt is made to read as many as len bytes, but a
        * smaller number may be read, possibly zero. The number of bytes
        * actually read is returned as an integer.
        *
        * This method blocks until input data is available, end of file
        * is detected, or an exception is thrown.
        *
        * If b is null, a NullPointerException is thrown.
        *
        * If off is negative, or len is negative, or off+len is greater
        * than the length of the array b, then an IndexOutOfBoundsException is thrown.
        *
        * If len is zero, then no bytes are read and 0 is returned;
        * otherwise, there is an attempt to read at least one byte.
        * If no byte is available because the stream is at end of file,
        * the value -1 is returned; otherwise, at least one byte is read and stored into b.
        *
        * The first byte read is stored into element b[off], the next one into b[off+1],
        * and so on. The number of bytes read is, at most, equal to len. Let k be the number
        * of bytes actually read; these bytes will be stored in elements b[off] through b[off+k-1],
        * leaving elements b[off+k] through b[off+len-1] unaffected.
        *
        * In every case, elements b[0] through b[off] and elements b[off+len]
        * through b[b.length-1] are unaffected.
        *
        * If the first byte cannot be read for any reason other than end of file,
        * then an IOException is thrown. In particular, an IOException is thrown
        * if the input stream has been closed.
        *
        * The read(b, off, len) method for class InputStream simply calls the method
        * read() repeatedly. If the first such call results in an IOException,
        * that exception is returned from the call to the read(b, off, len) method.
        * If any subsequent call to read() results in a IOException, the exception
        * is caught and treated as if it were end of file; the bytes read up to that
        * point are stored into b and the number of bytes read before the exception
        * occurred is returned. Subclasses are encouraged to provide a more efficient
        * implementation of this method.
        *
        * @param b - the buffer into which the data is read.
        * @param off - the start offset in array b  at which the data is written.
        * @param len - the maximum number of bytes to read.
        * @return the total number of bytes read into the buffer,
        *         or -1 is there is no more data because the end
        *         of the stream has been reached.
        * @throws CORBAFileServerException if an IO exception occurs.
        */
  @Override
public int read (com.highqsoft.corbafileserver.generated.DS_BYTEHolder b, int off, int len) throws com.highqsoft.corbafileserver.generated.CORBAFileServerException
  {
    return _impl.read(b, off, len);
  } // read


  /**
        * Close the input stream.
        *
        * @throws CORBAFileServerException if an IO exception occurs.
        */
  @Override
public void close () throws com.highqsoft.corbafileserver.generated.CORBAFileServerException
  {
    _impl.close();
  } // close


  /**
        * Get the length of the input stream.
        *
        * @return the length of the file to be transferd.
        * @throws CORBAFileServerException if an IO exception occurs.
        */
  @Override
public int length () throws com.highqsoft.corbafileserver.generated.CORBAFileServerException
  {
    return _impl.length();
  } // length


  /**
        * Reset the stream
        *
        * @throws CORBAFileServerException if an IO exception occurs.
        */
  @Override
public void reset () throws com.highqsoft.corbafileserver.generated.CORBAFileServerException
  {
    _impl.reset();
  } // reset

  private com.highqsoft.corbafileserver.generated.InputStreamIFOperations _impl;
  private org.omg.PortableServer.POA _poa;

} // class InputStreamIFPOATie
