package org.asam.ods;


/**
 * Generated from IDL interface "ODSWriteTransfer".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public abstract class ODSWriteTransferPOA
	extends org.omg.PortableServer.Servant
	implements org.omg.CORBA.portable.InvokeHandler, org.asam.ods.ODSWriteTransferOperations
{
	static private final java.util.Hashtable m_opsHash = new java.util.Hashtable();
	static
	{
		m_opsHash.put ( "getPosition", new java.lang.Integer(0));
		m_opsHash.put ( "putOctectSeq", new java.lang.Integer(1));
		m_opsHash.put ( "close", new java.lang.Integer(2));
	}
	private String[] ids = {"IDL:org/asam/ods/ODSWriteTransfer:1.0"};
	public org.asam.ods.ODSWriteTransfer _this()
	{
		return org.asam.ods.ODSWriteTransferHelper.narrow(_this_object());
	}
	public org.asam.ods.ODSWriteTransfer _this(org.omg.CORBA.ORB orb)
	{
		return org.asam.ods.ODSWriteTransferHelper.narrow(_this_object(orb));
	}
	public org.omg.CORBA.portable.OutputStream _invoke(String method, org.omg.CORBA.portable.InputStream _input, org.omg.CORBA.portable.ResponseHandler handler)
		throws org.omg.CORBA.SystemException
	{
		org.omg.CORBA.portable.OutputStream _out = null;
		// do something
		// quick lookup of operation
		java.lang.Integer opsIndex = (java.lang.Integer)m_opsHash.get ( method );
		if ( null == opsIndex )
			throw new org.omg.CORBA.BAD_OPERATION(method + " not found");
		switch ( opsIndex.intValue() )
		{
			case 0: // getPosition
			{
			try
			{
				_out = handler.createReply();
				org.asam.ods.T_LONGLONGHelper.write(_out,getPosition());
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 1: // putOctectSeq
			{
			try
			{
				byte[] _arg0=org.asam.ods.S_BYTEHelper.read(_input);
				_out = handler.createReply();
				putOctectSeq(_arg0);
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 2: // close
			{
			try
			{
				_out = handler.createReply();
				close();
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
		}
		return _out;
	}

	public String[] _all_interfaces(org.omg.PortableServer.POA poa, byte[] obj_id)
	{
		return ids;
	}
}
