package org.asam.ods;

/**
 * Generated from IDL union "SelItem".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class SelItem
	implements org.omg.CORBA.portable.IDLEntity
{
	private org.asam.ods.SelType discriminator;
	private org.asam.ods.SelValueExt value;
	private org.asam.ods.SelOperator operator;

	public SelItem ()
	{
	}

	public org.asam.ods.SelType discriminator ()
	{
		return discriminator;
	}

	public org.asam.ods.SelValueExt value ()
	{
		if (discriminator != org.asam.ods.SelType.SEL_VALUE_TYPE)
			throw new org.omg.CORBA.BAD_OPERATION();
		return value;
	}

	public void value (org.asam.ods.SelValueExt _x)
	{
		discriminator = org.asam.ods.SelType.SEL_VALUE_TYPE;
		value = _x;
	}

	public org.asam.ods.SelOperator operator ()
	{
		if (discriminator != org.asam.ods.SelType.SEL_OPERATOR_TYPE)
			throw new org.omg.CORBA.BAD_OPERATION();
		return operator;
	}

	public void operator (org.asam.ods.SelOperator _x)
	{
		discriminator = org.asam.ods.SelType.SEL_OPERATOR_TYPE;
		operator = _x;
	}

}
