package org.asam.ods;

/**
 * Generated from IDL struct "ApplicationRelationInstanceElementSeq".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class ApplicationRelationInstanceElementSeq
	implements org.omg.CORBA.portable.IDLEntity
{
	public ApplicationRelationInstanceElementSeq(){}
	public org.asam.ods.ApplicationRelation applRel;
	public org.asam.ods.InstanceElement[] instances;
	public ApplicationRelationInstanceElementSeq(org.asam.ods.ApplicationRelation applRel, org.asam.ods.InstanceElement[] instances)
	{
		this.applRel = applRel;
		this.instances = instances;
	}
}
