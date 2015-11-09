package org.asam.ods;

/**
 * Generated from IDL exception "AoException".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class AoException
	extends org.omg.CORBA.UserException
{
	public AoException()
	{
		super(org.asam.ods.AoExceptionHelper.id());
	}

	public org.asam.ods.ErrorCode errCode;
	public org.asam.ods.SeverityFlag sevFlag;
	public int minorCode;
	public java.lang.String reason;
	public AoException(java.lang.String _reason,org.asam.ods.ErrorCode errCode, org.asam.ods.SeverityFlag sevFlag, int minorCode, java.lang.String reason)
	{
		super(org.asam.ods.AoExceptionHelper.id()+ " " + _reason);
		this.errCode = errCode;
		this.sevFlag = sevFlag;
		this.minorCode = minorCode;
		this.reason = reason;
	}
	public AoException(org.asam.ods.ErrorCode errCode, org.asam.ods.SeverityFlag sevFlag, int minorCode, java.lang.String reason)
	{
		super(org.asam.ods.AoExceptionHelper.id());
		this.errCode = errCode;
		this.sevFlag = sevFlag;
		this.minorCode = minorCode;
		this.reason = reason;
	}
}
