package org.asam.ods;


/**
 * Generated from IDL interface "ODSFile".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public interface ODSFileOperations
	extends org.asam.ods.InstanceElementOperations
{
	/* constants */
	/* operations  */
	org.asam.ods.ODSWriteTransfer append() throws org.asam.ods.AoException;
	boolean canRead() throws org.asam.ods.AoException;
	boolean canWrite() throws org.asam.ods.AoException;
	org.asam.ods.ODSWriteTransfer create() throws org.asam.ods.AoException;
	boolean exists() throws org.asam.ods.AoException;
	java.lang.String getDate() throws org.asam.ods.AoException;
	org.asam.ods.T_LONGLONG getSize() throws org.asam.ods.AoException;
	void remove() throws org.asam.ods.AoException;
	org.asam.ods.ODSReadTransfer read() throws org.asam.ods.AoException;
	void takeUnderControl(java.lang.String sourceUrl) throws org.asam.ods.AoException;
	void removeFromControl(java.lang.String targetUrl) throws org.asam.ods.AoException;
}
