package org.asam.ods;


/**
 * Generated from IDL interface "Query".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public abstract class QueryPOA
	extends org.omg.PortableServer.Servant
	implements org.omg.CORBA.portable.InvokeHandler, org.asam.ods.QueryOperations
{
	static private final java.util.Hashtable m_opsHash = new java.util.Hashtable();
	static
	{
		m_opsHash.put ( "getTable", new java.lang.Integer(0));
		m_opsHash.put ( "getInstances", new java.lang.Integer(1));
		m_opsHash.put ( "executeQuery", new java.lang.Integer(2));
		m_opsHash.put ( "getQueryEvaluator", new java.lang.Integer(3));
		m_opsHash.put ( "getStatus", new java.lang.Integer(4));
		m_opsHash.put ( "prepareQuery", new java.lang.Integer(5));
		m_opsHash.put ( "getTableRows", new java.lang.Integer(6));
	}
	private String[] ids = {"IDL:org/asam/ods/Query:1.0"};
	public org.asam.ods.Query _this()
	{
		return org.asam.ods.QueryHelper.narrow(_this_object());
	}
	public org.asam.ods.Query _this(org.omg.CORBA.ORB orb)
	{
		return org.asam.ods.QueryHelper.narrow(_this_object(orb));
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
			case 0: // getTable
			{
			try
			{
				_out = handler.createReply();
				org.asam.ods.NameValueSeqUnitSequenceHelper.write(_out,getTable());
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 1: // getInstances
			{
			try
			{
				_out = handler.createReply();
				org.asam.ods.InstanceElementIteratorHelper.write(_out,getInstances());
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 2: // executeQuery
			{
			try
			{
				org.asam.ods.NameValue[] _arg0=org.asam.ods.NameValueSequenceHelper.read(_input);
				_out = handler.createReply();
				executeQuery(_arg0);
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 3: // getQueryEvaluator
			{
			try
			{
				_out = handler.createReply();
				org.asam.ods.QueryEvaluatorHelper.write(_out,getQueryEvaluator());
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 4: // getStatus
			{
			try
			{
				_out = handler.createReply();
				org.asam.ods.QueryStatusHelper.write(_out,getStatus());
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 5: // prepareQuery
			{
			try
			{
				org.asam.ods.NameValue[] _arg0=org.asam.ods.NameValueSequenceHelper.read(_input);
				_out = handler.createReply();
				prepareQuery(_arg0);
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 6: // getTableRows
			{
			try
			{
				_out = handler.createReply();
				org.asam.ods.NameValueUnitSequenceIteratorHelper.write(_out,getTableRows());
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
