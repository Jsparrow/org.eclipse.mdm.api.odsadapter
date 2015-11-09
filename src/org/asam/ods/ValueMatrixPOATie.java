package org.asam.ods;

import org.omg.PortableServer.POA;

/**
 * Generated from IDL interface "ValueMatrix".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public class ValueMatrixPOATie
	extends ValueMatrixPOA
{
	private ValueMatrixOperations _delegate;

	private POA _poa;
	public ValueMatrixPOATie(ValueMatrixOperations delegate)
	{
		_delegate = delegate;
	}
	public ValueMatrixPOATie(ValueMatrixOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.asam.ods.ValueMatrix _this()
	{
		return org.asam.ods.ValueMatrixHelper.narrow(_this_object());
	}
	public org.asam.ods.ValueMatrix _this(org.omg.CORBA.ORB orb)
	{
		return org.asam.ods.ValueMatrixHelper.narrow(_this_object(orb));
	}
	public ValueMatrixOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(ValueMatrixOperations delegate)
	{
		_delegate = delegate;
	}
	public POA _default_POA()
	{
		if (_poa != null)
		{
			return _poa;
		}
		return super._default_POA();
	}
	public java.lang.String[] listIndependentColumns(java.lang.String colPattern) throws org.asam.ods.AoException
	{
		return _delegate.listIndependentColumns(colPattern);
	}

	public void removeValueVector(org.asam.ods.Column col, int startPoint, int count) throws org.asam.ods.AoException
	{
_delegate.removeValueVector(col,startPoint,count);
	}

	public org.asam.ods.Column addColumn(org.asam.ods.NameUnit newColumn) throws org.asam.ods.AoException
	{
		return _delegate.addColumn(newColumn);
	}

	public org.asam.ods.Column addColumnScaledBy(org.asam.ods.NameUnit newColumn, org.asam.ods.Column scalingColumn) throws org.asam.ods.AoException
	{
		return _delegate.addColumnScaledBy(newColumn,scalingColumn);
	}

	public org.asam.ods.ValueMatrixMode getMode() throws org.asam.ods.AoException
	{
		return _delegate.getMode();
	}

	public void removeValueMeaPoint(java.lang.String[] columnNames, int meaPoint, int count) throws org.asam.ods.AoException
	{
_delegate.removeValueMeaPoint(columnNames,meaPoint,count);
	}

	public java.lang.String[] listScalingColumns(java.lang.String colPattern) throws org.asam.ods.AoException
	{
		return _delegate.listScalingColumns(colPattern);
	}

	public org.asam.ods.Column[] getColumnsScaledBy(org.asam.ods.Column scalingColumn) throws org.asam.ods.AoException
	{
		return _delegate.getColumnsScaledBy(scalingColumn);
	}

	public org.asam.ods.Column[] getIndependentColumns(java.lang.String colPattern) throws org.asam.ods.AoException
	{
		return _delegate.getIndependentColumns(colPattern);
	}

	public void setValueVector(org.asam.ods.Column col, org.asam.ods.SetType set, int startPoint, org.asam.ods.TS_ValueSeq value) throws org.asam.ods.AoException
	{
_delegate.setValueVector(col,set,startPoint,value);
	}

	public java.lang.String[] listColumnsScaledBy(org.asam.ods.Column scalingColumn) throws org.asam.ods.AoException
	{
		return _delegate.listColumnsScaledBy(scalingColumn);
	}

	public int getRowCount() throws org.asam.ods.AoException
	{
		return _delegate.getRowCount();
	}

	public void setValueMeaPoint(org.asam.ods.SetType set, int meaPoint, org.asam.ods.NameValue[] value) throws org.asam.ods.AoException
	{
_delegate.setValueMeaPoint(set,meaPoint,value);
	}

	public org.asam.ods.TS_ValueSeq getValueVector(org.asam.ods.Column col, int startPoint, int count) throws org.asam.ods.AoException
	{
		return _delegate.getValueVector(col,startPoint,count);
	}

	public java.lang.String[] listColumns(java.lang.String colPattern) throws org.asam.ods.AoException
	{
		return _delegate.listColumns(colPattern);
	}

	public org.asam.ods.NameValueSeqUnit[] getValue(org.asam.ods.Column[] columns, int startPoint, int count) throws org.asam.ods.AoException
	{
		return _delegate.getValue(columns,startPoint,count);
	}

	public int getColumnCount() throws org.asam.ods.AoException
	{
		return _delegate.getColumnCount();
	}

	public void setValue(org.asam.ods.SetType set, int startPoint, org.asam.ods.NameValueSeqUnit[] value) throws org.asam.ods.AoException
	{
_delegate.setValue(set,startPoint,value);
	}

	public org.asam.ods.Column[] getScalingColumns(java.lang.String colPattern) throws org.asam.ods.AoException
	{
		return _delegate.getScalingColumns(colPattern);
	}

	public org.asam.ods.Column[] getColumns(java.lang.String colPattern) throws org.asam.ods.AoException
	{
		return _delegate.getColumns(colPattern);
	}

	public void destroy() throws org.asam.ods.AoException
	{
_delegate.destroy();
	}

	public org.asam.ods.NameValueUnitIterator getValueMeaPoint(int meaPoint) throws org.asam.ods.AoException
	{
		return _delegate.getValueMeaPoint(meaPoint);
	}

}
