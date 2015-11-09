package org.asam.ods;

import org.omg.PortableServer.POA;

/**
 * Generated from IDL interface "SMatLink".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public class SMatLinkPOATie
	extends SMatLinkPOA
{
	private SMatLinkOperations _delegate;

	private POA _poa;
	public SMatLinkPOATie(SMatLinkOperations delegate)
	{
		_delegate = delegate;
	}
	public SMatLinkPOATie(SMatLinkOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.asam.ods.SMatLink _this()
	{
		return org.asam.ods.SMatLinkHelper.narrow(_this_object());
	}
	public org.asam.ods.SMatLink _this(org.omg.CORBA.ORB orb)
	{
		return org.asam.ods.SMatLinkHelper.narrow(_this_object(orb));
	}
	public SMatLinkOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(SMatLinkOperations delegate)
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
	public org.asam.ods.Column[] getSMat1Columns() throws org.asam.ods.AoException
	{
		return _delegate.getSMat1Columns();
	}

	public void setSMat1Columns(org.asam.ods.Column[] columns) throws org.asam.ods.AoException
	{
_delegate.setSMat1Columns(columns);
	}

	public org.asam.ods.SubMatrix getSMat1() throws org.asam.ods.AoException
	{
		return _delegate.getSMat1();
	}

	public void setOrdinalNumber(int ordinalNumber) throws org.asam.ods.AoException
	{
_delegate.setOrdinalNumber(ordinalNumber);
	}

	public void setSMat1(org.asam.ods.SubMatrix subMat1) throws org.asam.ods.AoException
	{
_delegate.setSMat1(subMat1);
	}

	public org.asam.ods.SubMatrix getSMat2() throws org.asam.ods.AoException
	{
		return _delegate.getSMat2();
	}

	public org.asam.ods.Column[] getSMat2Columns() throws org.asam.ods.AoException
	{
		return _delegate.getSMat2Columns();
	}

	public int getOrdinalNumber() throws org.asam.ods.AoException
	{
		return _delegate.getOrdinalNumber();
	}

	public void setSMat2(org.asam.ods.SubMatrix subMat2) throws org.asam.ods.AoException
	{
_delegate.setSMat2(subMat2);
	}

	public void setLinkType(org.asam.ods.BuildUpFunction linkType) throws org.asam.ods.AoException
	{
_delegate.setLinkType(linkType);
	}

	public void setSMat2Columns(org.asam.ods.Column[] columns) throws org.asam.ods.AoException
	{
_delegate.setSMat2Columns(columns);
	}

	public org.asam.ods.BuildUpFunction getLinkType() throws org.asam.ods.AoException
	{
		return _delegate.getLinkType();
	}

}
