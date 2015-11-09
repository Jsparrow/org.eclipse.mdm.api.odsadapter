package org.asam.ods;


/**
 * Generated from IDL interface "AoSession".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public abstract class AoSessionPOA
	extends org.omg.PortableServer.Servant
	implements org.omg.CORBA.portable.InvokeHandler, org.asam.ods.AoSessionOperations
{
	static private final java.util.Hashtable m_opsHash = new java.util.Hashtable();
	static
	{
		m_opsHash.put ( "commitTransaction", new java.lang.Integer(0));
		m_opsHash.put ( "getEnumerationStructure", new java.lang.Integer(1));
		m_opsHash.put ( "getEnumerationAttributes", new java.lang.Integer(2));
		m_opsHash.put ( "getUser", new java.lang.Integer(3));
		m_opsHash.put ( "getName", new java.lang.Integer(4));
		m_opsHash.put ( "getType", new java.lang.Integer(5));
		m_opsHash.put ( "getContext", new java.lang.Integer(6));
		m_opsHash.put ( "setContext", new java.lang.Integer(7));
		m_opsHash.put ( "getLockMode", new java.lang.Integer(8));
		m_opsHash.put ( "setLockMode", new java.lang.Integer(9));
		m_opsHash.put ( "getId", new java.lang.Integer(10));
		m_opsHash.put ( "startTransaction", new java.lang.Integer(11));
		m_opsHash.put ( "flush", new java.lang.Integer(12));
		m_opsHash.put ( "getDescription", new java.lang.Integer(13));
		m_opsHash.put ( "setPassword", new java.lang.Integer(14));
		m_opsHash.put ( "createCoSession", new java.lang.Integer(15));
		m_opsHash.put ( "close", new java.lang.Integer(16));
		m_opsHash.put ( "createQueryEvaluator", new java.lang.Integer(17));
		m_opsHash.put ( "getApplElemAccess", new java.lang.Integer(18));
		m_opsHash.put ( "getBaseStructure", new java.lang.Integer(19));
		m_opsHash.put ( "setContextString", new java.lang.Integer(20));
		m_opsHash.put ( "getContextByName", new java.lang.Integer(21));
		m_opsHash.put ( "getApplicationStructure", new java.lang.Integer(22));
		m_opsHash.put ( "createBlob", new java.lang.Integer(23));
		m_opsHash.put ( "getApplicationStructureValue", new java.lang.Integer(24));
		m_opsHash.put ( "setCurrentInitialRights", new java.lang.Integer(25));
		m_opsHash.put ( "listContext", new java.lang.Integer(26));
		m_opsHash.put ( "removeContext", new java.lang.Integer(27));
		m_opsHash.put ( "abortTransaction", new java.lang.Integer(28));
	}
	private String[] ids = {"IDL:org/asam/ods/AoSession:1.0"};
	public org.asam.ods.AoSession _this()
	{
		return org.asam.ods.AoSessionHelper.narrow(_this_object());
	}
	public org.asam.ods.AoSession _this(org.omg.CORBA.ORB orb)
	{
		return org.asam.ods.AoSessionHelper.narrow(_this_object(orb));
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
			case 0: // commitTransaction
			{
			try
			{
				_out = handler.createReply();
				commitTransaction();
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 1: // getEnumerationStructure
			{
			try
			{
				_out = handler.createReply();
				org.asam.ods.EnumerationStructureSequenceHelper.write(_out,getEnumerationStructure());
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 2: // getEnumerationAttributes
			{
			try
			{
				_out = handler.createReply();
				org.asam.ods.EnumerationAttributeStructureSequenceHelper.write(_out,getEnumerationAttributes());
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 3: // getUser
			{
			try
			{
				_out = handler.createReply();
				org.asam.ods.InstanceElementHelper.write(_out,getUser());
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 4: // getName
			{
			try
			{
				_out = handler.createReply();
				_out.write_string(getName());
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 5: // getType
			{
			try
			{
				_out = handler.createReply();
				_out.write_string(getType());
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 6: // getContext
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				_out = handler.createReply();
				org.asam.ods.NameValueIteratorHelper.write(_out,getContext(_arg0));
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 7: // setContext
			{
			try
			{
				org.asam.ods.NameValue _arg0=org.asam.ods.NameValueHelper.read(_input);
				_out = handler.createReply();
				setContext(_arg0);
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 8: // getLockMode
			{
			try
			{
				_out = handler.createReply();
				_out.write_short(getLockMode());
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 9: // setLockMode
			{
			try
			{
				short _arg0=_input.read_short();
				_out = handler.createReply();
				setLockMode(_arg0);
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 10: // getId
			{
			try
			{
				_out = handler.createReply();
				_out.write_long(getId());
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 11: // startTransaction
			{
			try
			{
				_out = handler.createReply();
				startTransaction();
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 12: // flush
			{
			try
			{
				_out = handler.createReply();
				flush();
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 13: // getDescription
			{
			try
			{
				_out = handler.createReply();
				_out.write_string(getDescription());
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 14: // setPassword
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				java.lang.String _arg1=_input.read_string();
				java.lang.String _arg2=_input.read_string();
				_out = handler.createReply();
				setPassword(_arg0,_arg1,_arg2);
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 15: // createCoSession
			{
			try
			{
				_out = handler.createReply();
				org.asam.ods.AoSessionHelper.write(_out,createCoSession());
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 16: // close
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
			case 17: // createQueryEvaluator
			{
			try
			{
				_out = handler.createReply();
				org.asam.ods.QueryEvaluatorHelper.write(_out,createQueryEvaluator());
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 18: // getApplElemAccess
			{
			try
			{
				_out = handler.createReply();
				org.asam.ods.ApplElemAccessHelper.write(_out,getApplElemAccess());
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 19: // getBaseStructure
			{
			try
			{
				_out = handler.createReply();
				org.asam.ods.BaseStructureHelper.write(_out,getBaseStructure());
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 20: // setContextString
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				java.lang.String _arg1=_input.read_string();
				_out = handler.createReply();
				setContextString(_arg0,_arg1);
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 21: // getContextByName
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				_out = handler.createReply();
				org.asam.ods.NameValueHelper.write(_out,getContextByName(_arg0));
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 22: // getApplicationStructure
			{
			try
			{
				_out = handler.createReply();
				org.asam.ods.ApplicationStructureHelper.write(_out,getApplicationStructure());
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 23: // createBlob
			{
			try
			{
				_out = handler.createReply();
				org.asam.ods.BlobHelper.write(_out,createBlob());
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 24: // getApplicationStructureValue
			{
			try
			{
				_out = handler.createReply();
				org.asam.ods.ApplicationStructureValueHelper.write(_out,getApplicationStructureValue());
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 25: // setCurrentInitialRights
			{
			try
			{
				org.asam.ods.InitialRight[] _arg0=org.asam.ods.InitialRightSequenceHelper.read(_input);
				boolean _arg1=_input.read_boolean();
				_out = handler.createReply();
				setCurrentInitialRights(_arg0,_arg1);
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 26: // listContext
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				_out = handler.createReply();
				org.asam.ods.NameIteratorHelper.write(_out,listContext(_arg0));
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 27: // removeContext
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				_out = handler.createReply();
				removeContext(_arg0);
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 28: // abortTransaction
			{
			try
			{
				_out = handler.createReply();
				abortTransaction();
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
