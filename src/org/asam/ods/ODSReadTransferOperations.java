package org.asam.ods;


/**
 * Generated from IDL interface "ODSReadTransfer".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public interface ODSReadTransferOperations
{
	/* constants */
	int READALL = -1;
	/* operations  */
	void close() throws org.asam.ods.AoException;
	byte[] getOctetSeq(int maxOctets) throws org.asam.ods.AoException;
	org.asam.ods.T_LONGLONG skipOctets(org.asam.ods.T_LONGLONG numOctets) throws org.asam.ods.AoException;
	org.asam.ods.T_LONGLONG getPosition() throws org.asam.ods.AoException;
}
