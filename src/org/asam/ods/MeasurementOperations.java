package org.asam.ods;


/**
 * Generated from IDL interface "Measurement".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public interface MeasurementOperations
	extends org.asam.ods.InstanceElementOperations
{
	/* constants */
	/* operations  */
	org.asam.ods.SMatLink createSMatLink() throws org.asam.ods.AoException;
	org.asam.ods.SMatLink[] getSMatLinks() throws org.asam.ods.AoException;
	org.asam.ods.ValueMatrix getValueMatrix() throws org.asam.ods.AoException;
	void removeSMatLink(org.asam.ods.SMatLink smLink) throws org.asam.ods.AoException;
	org.asam.ods.ValueMatrix getValueMatrixInMode(org.asam.ods.ValueMatrixMode vmMode) throws org.asam.ods.AoException;
}
