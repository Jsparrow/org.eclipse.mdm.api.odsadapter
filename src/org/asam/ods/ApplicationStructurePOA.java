package org.asam.ods;


/**
 * Generated from IDL interface "ApplicationStructure".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public abstract class ApplicationStructurePOA
	extends org.omg.PortableServer.Servant
	implements org.omg.CORBA.portable.InvokeHandler, org.asam.ods.ApplicationStructureOperations
{
	static private final java.util.Hashtable m_opsHash = new java.util.Hashtable();
	static
	{
		m_opsHash.put ( "removeEnumerationDefinition", new java.lang.Integer(0));
		m_opsHash.put ( "listEnumerations", new java.lang.Integer(1));
		m_opsHash.put ( "removeRelation", new java.lang.Integer(2));
		m_opsHash.put ( "listTopLevelElements", new java.lang.Integer(3));
		m_opsHash.put ( "getSession", new java.lang.Integer(4));
		m_opsHash.put ( "createElement", new java.lang.Integer(5));
		m_opsHash.put ( "getInstanceByAsamPath", new java.lang.Integer(6));
		m_opsHash.put ( "getRelations", new java.lang.Integer(7));
		m_opsHash.put ( "getTopLevelElements", new java.lang.Integer(8));
		m_opsHash.put ( "listElements", new java.lang.Integer(9));
		m_opsHash.put ( "getElements", new java.lang.Integer(10));
		m_opsHash.put ( "createInstanceRelations", new java.lang.Integer(11));
		m_opsHash.put ( "listElementsByBaseType", new java.lang.Integer(12));
		m_opsHash.put ( "getElementById", new java.lang.Integer(13));
		m_opsHash.put ( "createRelation", new java.lang.Integer(14));
		m_opsHash.put ( "getEnumerationDefinition", new java.lang.Integer(15));
		m_opsHash.put ( "createEnumerationDefinition", new java.lang.Integer(16));
		m_opsHash.put ( "check", new java.lang.Integer(17));
		m_opsHash.put ( "removeElement", new java.lang.Integer(18));
		m_opsHash.put ( "getElementByName", new java.lang.Integer(19));
		m_opsHash.put ( "getInstancesById", new java.lang.Integer(20));
		m_opsHash.put ( "getElementsByBaseType", new java.lang.Integer(21));
	}
	private String[] ids = {"IDL:org/asam/ods/ApplicationStructure:1.0"};
	public org.asam.ods.ApplicationStructure _this()
	{
		return org.asam.ods.ApplicationStructureHelper.narrow(_this_object());
	}
	public org.asam.ods.ApplicationStructure _this(org.omg.CORBA.ORB orb)
	{
		return org.asam.ods.ApplicationStructureHelper.narrow(_this_object(orb));
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
			case 0: // removeEnumerationDefinition
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				_out = handler.createReply();
				removeEnumerationDefinition(_arg0);
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 1: // listEnumerations
			{
			try
			{
				_out = handler.createReply();
				org.asam.ods.NameSequenceHelper.write(_out,listEnumerations());
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 2: // removeRelation
			{
			try
			{
				org.asam.ods.ApplicationRelation _arg0=org.asam.ods.ApplicationRelationHelper.read(_input);
				_out = handler.createReply();
				removeRelation(_arg0);
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 3: // listTopLevelElements
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				_out = handler.createReply();
				org.asam.ods.NameSequenceHelper.write(_out,listTopLevelElements(_arg0));
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 4: // getSession
			{
			try
			{
				_out = handler.createReply();
				org.asam.ods.AoSessionHelper.write(_out,getSession());
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 5: // createElement
			{
			try
			{
				org.asam.ods.BaseElement _arg0=org.asam.ods.BaseElementHelper.read(_input);
				_out = handler.createReply();
				org.asam.ods.ApplicationElementHelper.write(_out,createElement(_arg0));
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 6: // getInstanceByAsamPath
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				_out = handler.createReply();
				org.asam.ods.InstanceElementHelper.write(_out,getInstanceByAsamPath(_arg0));
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 7: // getRelations
			{
			try
			{
				org.asam.ods.ApplicationElement _arg0=org.asam.ods.ApplicationElementHelper.read(_input);
				org.asam.ods.ApplicationElement _arg1=org.asam.ods.ApplicationElementHelper.read(_input);
				_out = handler.createReply();
				org.asam.ods.ApplicationRelationSequenceHelper.write(_out,getRelations(_arg0,_arg1));
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 8: // getTopLevelElements
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				_out = handler.createReply();
				org.asam.ods.ApplicationElementSequenceHelper.write(_out,getTopLevelElements(_arg0));
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 9: // listElements
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				_out = handler.createReply();
				org.asam.ods.NameSequenceHelper.write(_out,listElements(_arg0));
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 10: // getElements
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				_out = handler.createReply();
				org.asam.ods.ApplicationElementSequenceHelper.write(_out,getElements(_arg0));
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 11: // createInstanceRelations
			{
			try
			{
				org.asam.ods.ApplicationRelation _arg0=org.asam.ods.ApplicationRelationHelper.read(_input);
				org.asam.ods.InstanceElement[] _arg1=org.asam.ods.InstanceElementSequenceHelper.read(_input);
				org.asam.ods.InstanceElement[] _arg2=org.asam.ods.InstanceElementSequenceHelper.read(_input);
				_out = handler.createReply();
				createInstanceRelations(_arg0,_arg1,_arg2);
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 12: // listElementsByBaseType
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				_out = handler.createReply();
				org.asam.ods.NameSequenceHelper.write(_out,listElementsByBaseType(_arg0));
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 13: // getElementById
			{
			try
			{
				org.asam.ods.T_LONGLONG _arg0=org.asam.ods.T_LONGLONGHelper.read(_input);
				_out = handler.createReply();
				org.asam.ods.ApplicationElementHelper.write(_out,getElementById(_arg0));
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 14: // createRelation
			{
			try
			{
				_out = handler.createReply();
				org.asam.ods.ApplicationRelationHelper.write(_out,createRelation());
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 15: // getEnumerationDefinition
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				_out = handler.createReply();
				org.asam.ods.EnumerationDefinitionHelper.write(_out,getEnumerationDefinition(_arg0));
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 16: // createEnumerationDefinition
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				_out = handler.createReply();
				org.asam.ods.EnumerationDefinitionHelper.write(_out,createEnumerationDefinition(_arg0));
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 17: // check
			{
			try
			{
				_out = handler.createReply();
				check();
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 18: // removeElement
			{
			try
			{
				org.asam.ods.ApplicationElement _arg0=org.asam.ods.ApplicationElementHelper.read(_input);
				_out = handler.createReply();
				removeElement(_arg0);
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 19: // getElementByName
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				_out = handler.createReply();
				org.asam.ods.ApplicationElementHelper.write(_out,getElementByName(_arg0));
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 20: // getInstancesById
			{
			try
			{
				org.asam.ods.ElemId[] _arg0=org.asam.ods.ElemIdSequenceHelper.read(_input);
				_out = handler.createReply();
				org.asam.ods.InstanceElementSequenceHelper.write(_out,getInstancesById(_arg0));
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 21: // getElementsByBaseType
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				_out = handler.createReply();
				org.asam.ods.ApplicationElementSequenceHelper.write(_out,getElementsByBaseType(_arg0));
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
