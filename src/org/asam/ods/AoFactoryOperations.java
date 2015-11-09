package org.asam.ods;


/**
 * Generated from IDL interface "AoFactory".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public interface AoFactoryOperations
{
	/* constants */
	/* operations  */
	java.lang.String getDescription() throws org.asam.ods.AoException;
	java.lang.String getInterfaceVersion() throws org.asam.ods.AoException;
	java.lang.String getName() throws org.asam.ods.AoException;
	java.lang.String getType() throws org.asam.ods.AoException;
	org.asam.ods.AoSession newSession(java.lang.String auth) throws org.asam.ods.AoException;
	org.asam.ods.AoSession newSessionNameValue(org.asam.ods.NameValue[] auth) throws org.asam.ods.AoException;
}
