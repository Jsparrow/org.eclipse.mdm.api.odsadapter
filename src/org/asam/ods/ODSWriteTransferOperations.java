package org.asam.ods;


/**
 * Generated from IDL interface "ODSWriteTransfer".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public interface ODSWriteTransferOperations
{
	/* constants */
	/* operations  */
	void close() throws org.asam.ods.AoException;
	void putOctectSeq(byte[] buffer) throws org.asam.ods.AoException;
	org.asam.ods.T_LONGLONG getPosition() throws org.asam.ods.AoException;
}
