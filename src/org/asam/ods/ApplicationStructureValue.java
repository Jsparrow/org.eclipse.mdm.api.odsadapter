package org.asam.ods;

/**
 * Generated from IDL struct "ApplicationStructureValue".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class ApplicationStructureValue
	implements org.omg.CORBA.portable.IDLEntity
{
	public ApplicationStructureValue(){}
	public org.asam.ods.ApplElem[] applElems;
	public org.asam.ods.ApplRel[] applRels;
	public ApplicationStructureValue(org.asam.ods.ApplElem[] applElems, org.asam.ods.ApplRel[] applRels)
	{
		this.applElems = applElems;
		this.applRels = applRels;
	}
}
