package org.asam.ods;


/**
 * Generated from IDL interface "Column".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public interface ColumnOperations
{
	/* constants */
	/* operations  */
	java.lang.String getFormula() throws org.asam.ods.AoException;
	java.lang.String getName() throws org.asam.ods.AoException;
	org.asam.ods.InstanceElement getSourceMQ() throws org.asam.ods.AoException;
	java.lang.String getUnit() throws org.asam.ods.AoException;
	void setFormula(java.lang.String formula) throws org.asam.ods.AoException;
	void setUnit(java.lang.String unit) throws org.asam.ods.AoException;
	boolean isIndependent() throws org.asam.ods.AoException;
	boolean isScaling() throws org.asam.ods.AoException;
	void setIndependent(boolean independent) throws org.asam.ods.AoException;
	void setScaling(boolean scaling) throws org.asam.ods.AoException;
	org.asam.ods.DataType getDataType() throws org.asam.ods.AoException;
	void destroy() throws org.asam.ods.AoException;
	int getSequenceRepresentation() throws org.asam.ods.AoException;
	void setSequenceRepresentation(int sequenceRepresentation) throws org.asam.ods.AoException;
	org.asam.ods.TS_Union getGenerationParameters() throws org.asam.ods.AoException;
	void setGenerationParameters(org.asam.ods.TS_Union generationParameters) throws org.asam.ods.AoException;
	org.asam.ods.DataType getRawDataType() throws org.asam.ods.AoException;
}
