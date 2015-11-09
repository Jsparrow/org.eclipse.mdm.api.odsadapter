package org.asam.ods;


/**
 * Generated from IDL interface "SMatLink".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public abstract class SMatLinkPOA
	extends org.omg.PortableServer.Servant
	implements org.omg.CORBA.portable.InvokeHandler, org.asam.ods.SMatLinkOperations
{
	static private final java.util.Hashtable m_opsHash = new java.util.Hashtable();
	static
	{
		m_opsHash.put ( "getSMat1Columns", new java.lang.Integer(0));
		m_opsHash.put ( "setSMat1Columns", new java.lang.Integer(1));
		m_opsHash.put ( "getSMat1", new java.lang.Integer(2));
		m_opsHash.put ( "setOrdinalNumber", new java.lang.Integer(3));
		m_opsHash.put ( "setSMat1", new java.lang.Integer(4));
		m_opsHash.put ( "getSMat2", new java.lang.Integer(5));
		m_opsHash.put ( "getSMat2Columns", new java.lang.Integer(6));
		m_opsHash.put ( "getOrdinalNumber", new java.lang.Integer(7));
		m_opsHash.put ( "setSMat2", new java.lang.Integer(8));
		m_opsHash.put ( "setLinkType", new java.lang.Integer(9));
		m_opsHash.put ( "setSMat2Columns", new java.lang.Integer(10));
		m_opsHash.put ( "getLinkType", new java.lang.Integer(11));
	}
	private String[] ids = {"IDL:org/asam/ods/SMatLink:1.0"};
	public org.asam.ods.SMatLink _this()
	{
		return org.asam.ods.SMatLinkHelper.narrow(_this_object());
	}
	public org.asam.ods.SMatLink _this(org.omg.CORBA.ORB orb)
	{
		return org.asam.ods.SMatLinkHelper.narrow(_this_object(orb));
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
			case 0: // getSMat1Columns
			{
			try
			{
				_out = handler.createReply();
				org.asam.ods.ColumnSequenceHelper.write(_out,getSMat1Columns());
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 1: // setSMat1Columns
			{
			try
			{
				org.asam.ods.Column[] _arg0=org.asam.ods.ColumnSequenceHelper.read(_input);
				_out = handler.createReply();
				setSMat1Columns(_arg0);
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 2: // getSMat1
			{
			try
			{
				_out = handler.createReply();
				org.asam.ods.SubMatrixHelper.write(_out,getSMat1());
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 3: // setOrdinalNumber
			{
			try
			{
				int _arg0=_input.read_long();
				_out = handler.createReply();
				setOrdinalNumber(_arg0);
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 4: // setSMat1
			{
			try
			{
				org.asam.ods.SubMatrix _arg0=org.asam.ods.SubMatrixHelper.read(_input);
				_out = handler.createReply();
				setSMat1(_arg0);
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 5: // getSMat2
			{
			try
			{
				_out = handler.createReply();
				org.asam.ods.SubMatrixHelper.write(_out,getSMat2());
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 6: // getSMat2Columns
			{
			try
			{
				_out = handler.createReply();
				org.asam.ods.ColumnSequenceHelper.write(_out,getSMat2Columns());
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 7: // getOrdinalNumber
			{
			try
			{
				_out = handler.createReply();
				_out.write_long(getOrdinalNumber());
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 8: // setSMat2
			{
			try
			{
				org.asam.ods.SubMatrix _arg0=org.asam.ods.SubMatrixHelper.read(_input);
				_out = handler.createReply();
				setSMat2(_arg0);
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 9: // setLinkType
			{
			try
			{
				org.asam.ods.BuildUpFunction _arg0=org.asam.ods.BuildUpFunctionHelper.read(_input);
				_out = handler.createReply();
				setLinkType(_arg0);
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 10: // setSMat2Columns
			{
			try
			{
				org.asam.ods.Column[] _arg0=org.asam.ods.ColumnSequenceHelper.read(_input);
				_out = handler.createReply();
				setSMat2Columns(_arg0);
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 11: // getLinkType
			{
			try
			{
				_out = handler.createReply();
				org.asam.ods.BuildUpFunctionHelper.write(_out,getLinkType());
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
