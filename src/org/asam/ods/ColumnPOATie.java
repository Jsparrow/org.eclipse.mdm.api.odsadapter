package org.asam.ods;

import org.omg.PortableServer.POA;

/**
 * Generated from IDL interface "Column".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public class ColumnPOATie
	extends ColumnPOA
{
	private ColumnOperations _delegate;

	private POA _poa;
	public ColumnPOATie(ColumnOperations delegate)
	{
		_delegate = delegate;
	}
	public ColumnPOATie(ColumnOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.asam.ods.Column _this()
	{
		return org.asam.ods.ColumnHelper.narrow(_this_object());
	}
	public org.asam.ods.Column _this(org.omg.CORBA.ORB orb)
	{
		return org.asam.ods.ColumnHelper.narrow(_this_object(orb));
	}
	public ColumnOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(ColumnOperations delegate)
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
	public org.asam.ods.InstanceElement getSourceMQ() throws org.asam.ods.AoException
	{
		return _delegate.getSourceMQ();
	}

	public boolean isIndependent() throws org.asam.ods.AoException
	{
		return _delegate.isIndependent();
	}

	public void setScaling(boolean scaling) throws org.asam.ods.AoException
	{
_delegate.setScaling(scaling);
	}

	public void setSequenceRepresentation(int sequenceRepresentation) throws org.asam.ods.AoException
	{
_delegate.setSequenceRepresentation(sequenceRepresentation);
	}

	public boolean isScaling() throws org.asam.ods.AoException
	{
		return _delegate.isScaling();
	}

	public org.asam.ods.TS_Union getGenerationParameters() throws org.asam.ods.AoException
	{
		return _delegate.getGenerationParameters();
	}

	public void setUnit(java.lang.String unit) throws org.asam.ods.AoException
	{
_delegate.setUnit(unit);
	}

	public void setFormula(java.lang.String formula) throws org.asam.ods.AoException
	{
_delegate.setFormula(formula);
	}

	public org.asam.ods.DataType getDataType() throws org.asam.ods.AoException
	{
		return _delegate.getDataType();
	}

	public org.asam.ods.DataType getRawDataType() throws org.asam.ods.AoException
	{
		return _delegate.getRawDataType();
	}

	public java.lang.String getName() throws org.asam.ods.AoException
	{
		return _delegate.getName();
	}

	public void destroy() throws org.asam.ods.AoException
	{
_delegate.destroy();
	}

	public int getSequenceRepresentation() throws org.asam.ods.AoException
	{
		return _delegate.getSequenceRepresentation();
	}

	public void setGenerationParameters(org.asam.ods.TS_Union generationParameters) throws org.asam.ods.AoException
	{
_delegate.setGenerationParameters(generationParameters);
	}

	public void setIndependent(boolean independent) throws org.asam.ods.AoException
	{
_delegate.setIndependent(independent);
	}

	public java.lang.String getFormula() throws org.asam.ods.AoException
	{
		return _delegate.getFormula();
	}

	public java.lang.String getUnit() throws org.asam.ods.AoException
	{
		return _delegate.getUnit();
	}

}
