package org.asam.ods;


/**
 * Generated from IDL interface "ValueMatrix".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public interface ValueMatrixOperations
{
	/* constants */
	/* operations  */
	org.asam.ods.Column[] getColumns(java.lang.String colPattern) throws org.asam.ods.AoException;
	int getColumnCount() throws org.asam.ods.AoException;
	org.asam.ods.Column[] getIndependentColumns(java.lang.String colPattern) throws org.asam.ods.AoException;
	int getRowCount() throws org.asam.ods.AoException;
	org.asam.ods.NameValueUnitIterator getValueMeaPoint(int meaPoint) throws org.asam.ods.AoException;
	org.asam.ods.TS_ValueSeq getValueVector(org.asam.ods.Column col, int startPoint, int count) throws org.asam.ods.AoException;
	java.lang.String[] listColumns(java.lang.String colPattern) throws org.asam.ods.AoException;
	java.lang.String[] listIndependentColumns(java.lang.String colPattern) throws org.asam.ods.AoException;
	void removeValueMeaPoint(java.lang.String[] columnNames, int meaPoint, int count) throws org.asam.ods.AoException;
	void removeValueVector(org.asam.ods.Column col, int startPoint, int count) throws org.asam.ods.AoException;
	void setValueMeaPoint(org.asam.ods.SetType set, int meaPoint, org.asam.ods.NameValue[] value) throws org.asam.ods.AoException;
	void setValueVector(org.asam.ods.Column col, org.asam.ods.SetType set, int startPoint, org.asam.ods.TS_ValueSeq value) throws org.asam.ods.AoException;
	void setValue(org.asam.ods.SetType set, int startPoint, org.asam.ods.NameValueSeqUnit[] value) throws org.asam.ods.AoException;
	org.asam.ods.Column addColumn(org.asam.ods.NameUnit newColumn) throws org.asam.ods.AoException;
	java.lang.String[] listScalingColumns(java.lang.String colPattern) throws org.asam.ods.AoException;
	org.asam.ods.Column[] getScalingColumns(java.lang.String colPattern) throws org.asam.ods.AoException;
	java.lang.String[] listColumnsScaledBy(org.asam.ods.Column scalingColumn) throws org.asam.ods.AoException;
	org.asam.ods.Column[] getColumnsScaledBy(org.asam.ods.Column scalingColumn) throws org.asam.ods.AoException;
	org.asam.ods.Column addColumnScaledBy(org.asam.ods.NameUnit newColumn, org.asam.ods.Column scalingColumn) throws org.asam.ods.AoException;
	void destroy() throws org.asam.ods.AoException;
	org.asam.ods.NameValueSeqUnit[] getValue(org.asam.ods.Column[] columns, int startPoint, int count) throws org.asam.ods.AoException;
	org.asam.ods.ValueMatrixMode getMode() throws org.asam.ods.AoException;
}
