package org.asam.ods;


/**
 * Generated from IDL interface "Blob".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public interface BlobOperations
{
	/* constants */
	/* operations  */
	void append(byte[] value) throws org.asam.ods.AoException;
	boolean compare(org.asam.ods.Blob aBlob) throws org.asam.ods.AoException;
	byte[] get(int offset, int length) throws org.asam.ods.AoException;
	java.lang.String getHeader() throws org.asam.ods.AoException;
	int getLength() throws org.asam.ods.AoException;
	void set(byte[] value) throws org.asam.ods.AoException;
	void setHeader(java.lang.String header) throws org.asam.ods.AoException;
	void destroy() throws org.asam.ods.AoException;
}
