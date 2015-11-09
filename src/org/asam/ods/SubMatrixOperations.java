package org.asam.ods;


/**
 * Generated from IDL interface "SubMatrix".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public interface SubMatrixOperations
	extends org.asam.ods.InstanceElementOperations
{
	/* constants */
	/* operations  */
	org.asam.ods.Column[] getColumns(java.lang.String colPattern) throws org.asam.ods.AoException;
	org.asam.ods.ValueMatrix getValueMatrix() throws org.asam.ods.AoException;
	java.lang.String[] listColumns(java.lang.String colPattern) throws org.asam.ods.AoException;
	org.asam.ods.ValueMatrix getValueMatrixInMode(org.asam.ods.ValueMatrixMode vmMode) throws org.asam.ods.AoException;
}
