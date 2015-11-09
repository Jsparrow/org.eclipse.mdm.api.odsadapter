package org.asam.ods;


/**
 * Generated from IDL interface "ApplicationElement".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public abstract class ApplicationElementPOA
	extends org.omg.PortableServer.Servant
	implements org.omg.CORBA.portable.InvokeHandler, org.asam.ods.ApplicationElementOperations
{
	static private final java.util.Hashtable m_opsHash = new java.util.Hashtable();
	static
	{
		m_opsHash.put ( "listAttributes", new java.lang.Integer(0));
		m_opsHash.put ( "setRights", new java.lang.Integer(1));
		m_opsHash.put ( "getName", new java.lang.Integer(2));
		m_opsHash.put ( "getInitialRights", new java.lang.Integer(3));
		m_opsHash.put ( "removeAttribute", new java.lang.Integer(4));
		m_opsHash.put ( "getSecurityLevel", new java.lang.Integer(5));
		m_opsHash.put ( "createAttribute", new java.lang.Integer(6));
		m_opsHash.put ( "getId", new java.lang.Integer(7));
		m_opsHash.put ( "getRelationsByBaseName", new java.lang.Integer(8));
		m_opsHash.put ( "createInstances", new java.lang.Integer(9));
		m_opsHash.put ( "setSecurityLevel", new java.lang.Integer(10));
		m_opsHash.put ( "getInitialRightRelations", new java.lang.Integer(11));
		m_opsHash.put ( "listRelatedElementsByRelationship", new java.lang.Integer(12));
		m_opsHash.put ( "setBaseElement", new java.lang.Integer(13));
		m_opsHash.put ( "getAttributeByBaseName", new java.lang.Integer(14));
		m_opsHash.put ( "getInstanceById", new java.lang.Integer(15));
		m_opsHash.put ( "listInstances", new java.lang.Integer(16));
		m_opsHash.put ( "getAllRelatedElements", new java.lang.Integer(17));
		m_opsHash.put ( "listAllRelatedElements", new java.lang.Integer(18));
		m_opsHash.put ( "getRelatedElementsByRelationship", new java.lang.Integer(19));
		m_opsHash.put ( "getBaseElement", new java.lang.Integer(20));
		m_opsHash.put ( "getRights", new java.lang.Integer(21));
		m_opsHash.put ( "setName", new java.lang.Integer(22));
		m_opsHash.put ( "getApplicationStructure", new java.lang.Integer(23));
		m_opsHash.put ( "setInitialRights", new java.lang.Integer(24));
		m_opsHash.put ( "getAttributes", new java.lang.Integer(25));
		m_opsHash.put ( "getAllRelations", new java.lang.Integer(26));
		m_opsHash.put ( "getInstances", new java.lang.Integer(27));
		m_opsHash.put ( "getInstanceByName", new java.lang.Integer(28));
		m_opsHash.put ( "createInstance", new java.lang.Integer(29));
		m_opsHash.put ( "setInitialRightRelation", new java.lang.Integer(30));
		m_opsHash.put ( "getRelationsByType", new java.lang.Integer(31));
		m_opsHash.put ( "getAttributeByName", new java.lang.Integer(32));
		m_opsHash.put ( "removeInstance", new java.lang.Integer(33));
	}
	private String[] ids = {"IDL:org/asam/ods/ApplicationElement:1.0"};
	public org.asam.ods.ApplicationElement _this()
	{
		return org.asam.ods.ApplicationElementHelper.narrow(_this_object());
	}
	public org.asam.ods.ApplicationElement _this(org.omg.CORBA.ORB orb)
	{
		return org.asam.ods.ApplicationElementHelper.narrow(_this_object(orb));
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
			case 0: // listAttributes
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				_out = handler.createReply();
				org.asam.ods.NameSequenceHelper.write(_out,listAttributes(_arg0));
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 1: // setRights
			{
			try
			{
				org.asam.ods.InstanceElement _arg0=org.asam.ods.InstanceElementHelper.read(_input);
				int _arg1=_input.read_long();
				org.asam.ods.RightsSet _arg2=org.asam.ods.RightsSetHelper.read(_input);
				_out = handler.createReply();
				setRights(_arg0,_arg1,_arg2);
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 2: // getName
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
			case 3: // getInitialRights
			{
			try
			{
				_out = handler.createReply();
				org.asam.ods.InitialRightSequenceHelper.write(_out,getInitialRights());
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 4: // removeAttribute
			{
			try
			{
				org.asam.ods.ApplicationAttribute _arg0=org.asam.ods.ApplicationAttributeHelper.read(_input);
				_out = handler.createReply();
				removeAttribute(_arg0);
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 5: // getSecurityLevel
			{
			try
			{
				_out = handler.createReply();
				_out.write_long(getSecurityLevel());
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 6: // createAttribute
			{
			try
			{
				_out = handler.createReply();
				org.asam.ods.ApplicationAttributeHelper.write(_out,createAttribute());
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 7: // getId
			{
			try
			{
				_out = handler.createReply();
				org.asam.ods.T_LONGLONGHelper.write(_out,getId());
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 8: // getRelationsByBaseName
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				_out = handler.createReply();
				org.asam.ods.ApplicationRelationSequenceHelper.write(_out,getRelationsByBaseName(_arg0));
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 9: // createInstances
			{
			try
			{
				org.asam.ods.NameValueSeqUnit[] _arg0=org.asam.ods.NameValueSeqUnitSequenceHelper.read(_input);
				org.asam.ods.ApplicationRelationInstanceElementSeq[] _arg1=org.asam.ods.ApplicationRelationInstanceElementSeqSequenceHelper.read(_input);
				_out = handler.createReply();
				org.asam.ods.InstanceElementSequenceHelper.write(_out,createInstances(_arg0,_arg1));
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 10: // setSecurityLevel
			{
			try
			{
				int _arg0=_input.read_long();
				org.asam.ods.RightsSet _arg1=org.asam.ods.RightsSetHelper.read(_input);
				_out = handler.createReply();
				setSecurityLevel(_arg0,_arg1);
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 11: // getInitialRightRelations
			{
			try
			{
				_out = handler.createReply();
				org.asam.ods.ApplicationRelationSequenceHelper.write(_out,getInitialRightRelations());
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 12: // listRelatedElementsByRelationship
			{
			try
			{
				org.asam.ods.Relationship _arg0=org.asam.ods.RelationshipHelper.read(_input);
				_out = handler.createReply();
				org.asam.ods.NameSequenceHelper.write(_out,listRelatedElementsByRelationship(_arg0));
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 13: // setBaseElement
			{
			try
			{
				org.asam.ods.BaseElement _arg0=org.asam.ods.BaseElementHelper.read(_input);
				_out = handler.createReply();
				setBaseElement(_arg0);
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 14: // getAttributeByBaseName
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				_out = handler.createReply();
				org.asam.ods.ApplicationAttributeHelper.write(_out,getAttributeByBaseName(_arg0));
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 15: // getInstanceById
			{
			try
			{
				org.asam.ods.T_LONGLONG _arg0=org.asam.ods.T_LONGLONGHelper.read(_input);
				_out = handler.createReply();
				org.asam.ods.InstanceElementHelper.write(_out,getInstanceById(_arg0));
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 16: // listInstances
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				_out = handler.createReply();
				org.asam.ods.NameIteratorHelper.write(_out,listInstances(_arg0));
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 17: // getAllRelatedElements
			{
			try
			{
				_out = handler.createReply();
				org.asam.ods.ApplicationElementSequenceHelper.write(_out,getAllRelatedElements());
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 18: // listAllRelatedElements
			{
			try
			{
				_out = handler.createReply();
				org.asam.ods.NameSequenceHelper.write(_out,listAllRelatedElements());
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 19: // getRelatedElementsByRelationship
			{
			try
			{
				org.asam.ods.Relationship _arg0=org.asam.ods.RelationshipHelper.read(_input);
				_out = handler.createReply();
				org.asam.ods.ApplicationElementSequenceHelper.write(_out,getRelatedElementsByRelationship(_arg0));
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 20: // getBaseElement
			{
			try
			{
				_out = handler.createReply();
				org.asam.ods.BaseElementHelper.write(_out,getBaseElement());
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 21: // getRights
			{
			try
			{
				_out = handler.createReply();
				org.asam.ods.ACLSequenceHelper.write(_out,getRights());
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 22: // setName
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				_out = handler.createReply();
				setName(_arg0);
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 23: // getApplicationStructure
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
			case 24: // setInitialRights
			{
			try
			{
				org.asam.ods.InstanceElement _arg0=org.asam.ods.InstanceElementHelper.read(_input);
				int _arg1=_input.read_long();
				org.asam.ods.T_LONGLONG _arg2=org.asam.ods.T_LONGLONGHelper.read(_input);
				org.asam.ods.RightsSet _arg3=org.asam.ods.RightsSetHelper.read(_input);
				_out = handler.createReply();
				setInitialRights(_arg0,_arg1,_arg2,_arg3);
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 25: // getAttributes
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				_out = handler.createReply();
				org.asam.ods.ApplicationAttributeSequenceHelper.write(_out,getAttributes(_arg0));
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 26: // getAllRelations
			{
			try
			{
				_out = handler.createReply();
				org.asam.ods.ApplicationRelationSequenceHelper.write(_out,getAllRelations());
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 27: // getInstances
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				_out = handler.createReply();
				org.asam.ods.InstanceElementIteratorHelper.write(_out,getInstances(_arg0));
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 28: // getInstanceByName
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				_out = handler.createReply();
				org.asam.ods.InstanceElementHelper.write(_out,getInstanceByName(_arg0));
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 29: // createInstance
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				_out = handler.createReply();
				org.asam.ods.InstanceElementHelper.write(_out,createInstance(_arg0));
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 30: // setInitialRightRelation
			{
			try
			{
				org.asam.ods.ApplicationRelation _arg0=org.asam.ods.ApplicationRelationHelper.read(_input);
				boolean _arg1=_input.read_boolean();
				_out = handler.createReply();
				setInitialRightRelation(_arg0,_arg1);
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 31: // getRelationsByType
			{
			try
			{
				org.asam.ods.RelationType _arg0=org.asam.ods.RelationTypeHelper.read(_input);
				_out = handler.createReply();
				org.asam.ods.ApplicationRelationSequenceHelper.write(_out,getRelationsByType(_arg0));
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 32: // getAttributeByName
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				_out = handler.createReply();
				org.asam.ods.ApplicationAttributeHelper.write(_out,getAttributeByName(_arg0));
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 33: // removeInstance
			{
			try
			{
				org.asam.ods.T_LONGLONG _arg0=org.asam.ods.T_LONGLONGHelper.read(_input);
				boolean _arg1=_input.read_boolean();
				_out = handler.createReply();
				removeInstance(_arg0,_arg1);
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
