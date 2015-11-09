package org.asam.ods;

/**
 * Generated from IDL struct "T_ExternalReference".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class T_ExternalReference
	implements org.omg.CORBA.portable.IDLEntity
{
	public T_ExternalReference(){}
	public java.lang.String description;
	public java.lang.String mimeType;
	public java.lang.String location;
	public T_ExternalReference(java.lang.String description, java.lang.String mimeType, java.lang.String location)
	{
		this.description = description;
		this.mimeType = mimeType;
		this.location = location;
	}
}
