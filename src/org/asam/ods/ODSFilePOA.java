package org.asam.ods;


/**
 * Generated from IDL interface "ODSFile".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public abstract class ODSFilePOA
	extends org.omg.PortableServer.Servant
	implements org.omg.CORBA.portable.InvokeHandler, org.asam.ods.ODSFileOperations
{
	static private final java.util.Hashtable m_opsHash = new java.util.Hashtable();
	static
	{
		m_opsHash.put ( "setValue", new java.lang.Integer(0));
		m_opsHash.put ( "create", new java.lang.Integer(1));
		m_opsHash.put ( "getDate", new java.lang.Integer(2));
		m_opsHash.put ( "getSize", new java.lang.Integer(3));
		m_opsHash.put ( "append", new java.lang.Integer(4));
		m_opsHash.put ( "getApplicationElement", new java.lang.Integer(5));
		m_opsHash.put ( "removeRelation", new java.lang.Integer(6));
		m_opsHash.put ( "setRights", new java.lang.Integer(7));
		m_opsHash.put ( "upcastSubMatrix", new java.lang.Integer(8));
		m_opsHash.put ( "getValueByBaseName", new java.lang.Integer(9));
		m_opsHash.put ( "upcastMeasurement", new java.lang.Integer(10));
		m_opsHash.put ( "canWrite", new java.lang.Integer(11));
		m_opsHash.put ( "setValueSeq", new java.lang.Integer(12));
		m_opsHash.put ( "renameInstanceAttribute", new java.lang.Integer(13));
		m_opsHash.put ( "getRelatedInstances", new java.lang.Integer(14));
		m_opsHash.put ( "createRelatedInstances", new java.lang.Integer(15));
		m_opsHash.put ( "remove", new java.lang.Integer(16));
		m_opsHash.put ( "getName", new java.lang.Integer(17));
		m_opsHash.put ( "getInitialRights", new java.lang.Integer(18));
		m_opsHash.put ( "deepCopy", new java.lang.Integer(19));
		m_opsHash.put ( "addInstanceAttribute", new java.lang.Integer(20));
		m_opsHash.put ( "canRead", new java.lang.Integer(21));
		m_opsHash.put ( "getAsamPath", new java.lang.Integer(22));
		m_opsHash.put ( "getRelatedInstancesByRelationship", new java.lang.Integer(23));
		m_opsHash.put ( "removeFromControl", new java.lang.Integer(24));
		m_opsHash.put ( "destroy", new java.lang.Integer(25));
		m_opsHash.put ( "getId", new java.lang.Integer(26));
		m_opsHash.put ( "removeInstanceAttribute", new java.lang.Integer(27));
		m_opsHash.put ( "getRights", new java.lang.Integer(28));
		m_opsHash.put ( "shallowCopy", new java.lang.Integer(29));
		m_opsHash.put ( "listAttributes", new java.lang.Integer(30));
		m_opsHash.put ( "getValueSeq", new java.lang.Integer(31));
		m_opsHash.put ( "compare", new java.lang.Integer(32));
		m_opsHash.put ( "exists", new java.lang.Integer(33));
		m_opsHash.put ( "getValueInUnit", new java.lang.Integer(34));
		m_opsHash.put ( "createRelation", new java.lang.Integer(35));
		m_opsHash.put ( "takeUnderControl", new java.lang.Integer(36));
		m_opsHash.put ( "listRelatedInstancesByRelationship", new java.lang.Integer(37));
		m_opsHash.put ( "listRelatedInstances", new java.lang.Integer(38));
		m_opsHash.put ( "getValue", new java.lang.Integer(39));
		m_opsHash.put ( "setInitialRights", new java.lang.Integer(40));
		m_opsHash.put ( "read", new java.lang.Integer(41));
		m_opsHash.put ( "upcastODSFile", new java.lang.Integer(42));
		m_opsHash.put ( "setName", new java.lang.Integer(43));
	}
	private String[] ids = {"IDL:org/asam/ods/ODSFile:1.0","IDL:org/asam/ods/InstanceElement:1.0"};
	public org.asam.ods.ODSFile _this()
	{
		return org.asam.ods.ODSFileHelper.narrow(_this_object());
	}
	public org.asam.ods.ODSFile _this(org.omg.CORBA.ORB orb)
	{
		return org.asam.ods.ODSFileHelper.narrow(_this_object(orb));
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
			case 0: // setValue
			{
			try
			{
				org.asam.ods.NameValueUnit _arg0=org.asam.ods.NameValueUnitHelper.read(_input);
				_out = handler.createReply();
				setValue(_arg0);
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 1: // create
			{
			try
			{
				_out = handler.createReply();
				org.asam.ods.ODSWriteTransferHelper.write(_out,create());
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 2: // getDate
			{
			try
			{
				_out = handler.createReply();
				_out.write_string(getDate());
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 3: // getSize
			{
			try
			{
				_out = handler.createReply();
				org.asam.ods.T_LONGLONGHelper.write(_out,getSize());
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 4: // append
			{
			try
			{
				_out = handler.createReply();
				org.asam.ods.ODSWriteTransferHelper.write(_out,append());
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 5: // getApplicationElement
			{
			try
			{
				_out = handler.createReply();
				org.asam.ods.ApplicationElementHelper.write(_out,getApplicationElement());
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 6: // removeRelation
			{
			try
			{
				org.asam.ods.ApplicationRelation _arg0=org.asam.ods.ApplicationRelationHelper.read(_input);
				org.asam.ods.InstanceElement _arg1=org.asam.ods.InstanceElementHelper.read(_input);
				_out = handler.createReply();
				removeRelation(_arg0,_arg1);
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 7: // setRights
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
			case 8: // upcastSubMatrix
			{
			try
			{
				_out = handler.createReply();
				org.asam.ods.SubMatrixHelper.write(_out,upcastSubMatrix());
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 9: // getValueByBaseName
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				_out = handler.createReply();
				org.asam.ods.NameValueUnitHelper.write(_out,getValueByBaseName(_arg0));
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 10: // upcastMeasurement
			{
			try
			{
				_out = handler.createReply();
				org.asam.ods.MeasurementHelper.write(_out,upcastMeasurement());
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 11: // canWrite
			{
			try
			{
				_out = handler.createReply();
				_out.write_boolean(canWrite());
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 12: // setValueSeq
			{
			try
			{
				org.asam.ods.NameValueUnit[] _arg0=org.asam.ods.NameValueUnitSequenceHelper.read(_input);
				_out = handler.createReply();
				setValueSeq(_arg0);
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 13: // renameInstanceAttribute
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				java.lang.String _arg1=_input.read_string();
				_out = handler.createReply();
				renameInstanceAttribute(_arg0,_arg1);
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 14: // getRelatedInstances
			{
			try
			{
				org.asam.ods.ApplicationRelation _arg0=org.asam.ods.ApplicationRelationHelper.read(_input);
				java.lang.String _arg1=_input.read_string();
				_out = handler.createReply();
				org.asam.ods.InstanceElementIteratorHelper.write(_out,getRelatedInstances(_arg0,_arg1));
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 15: // createRelatedInstances
			{
			try
			{
				org.asam.ods.ApplicationRelation _arg0=org.asam.ods.ApplicationRelationHelper.read(_input);
				org.asam.ods.NameValueSeqUnit[] _arg1=org.asam.ods.NameValueSeqUnitSequenceHelper.read(_input);
				org.asam.ods.ApplicationRelationInstanceElementSeq[] _arg2=org.asam.ods.ApplicationRelationInstanceElementSeqSequenceHelper.read(_input);
				_out = handler.createReply();
				org.asam.ods.InstanceElementSequenceHelper.write(_out,createRelatedInstances(_arg0,_arg1,_arg2));
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 16: // remove
			{
			try
			{
				_out = handler.createReply();
				remove();
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 17: // getName
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
			case 18: // getInitialRights
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
			case 19: // deepCopy
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				java.lang.String _arg1=_input.read_string();
				_out = handler.createReply();
				org.asam.ods.InstanceElementHelper.write(_out,deepCopy(_arg0,_arg1));
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 20: // addInstanceAttribute
			{
			try
			{
				org.asam.ods.NameValueUnit _arg0=org.asam.ods.NameValueUnitHelper.read(_input);
				_out = handler.createReply();
				addInstanceAttribute(_arg0);
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 21: // canRead
			{
			try
			{
				_out = handler.createReply();
				_out.write_boolean(canRead());
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 22: // getAsamPath
			{
			try
			{
				_out = handler.createReply();
				_out.write_string(getAsamPath());
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 23: // getRelatedInstancesByRelationship
			{
			try
			{
				org.asam.ods.Relationship _arg0=org.asam.ods.RelationshipHelper.read(_input);
				java.lang.String _arg1=_input.read_string();
				_out = handler.createReply();
				org.asam.ods.InstanceElementIteratorHelper.write(_out,getRelatedInstancesByRelationship(_arg0,_arg1));
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 24: // removeFromControl
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				_out = handler.createReply();
				removeFromControl(_arg0);
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 25: // destroy
			{
			try
			{
				_out = handler.createReply();
				destroy();
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 26: // getId
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
			case 27: // removeInstanceAttribute
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				_out = handler.createReply();
				removeInstanceAttribute(_arg0);
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 28: // getRights
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
			case 29: // shallowCopy
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				java.lang.String _arg1=_input.read_string();
				_out = handler.createReply();
				org.asam.ods.InstanceElementHelper.write(_out,shallowCopy(_arg0,_arg1));
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 30: // listAttributes
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				org.asam.ods.AttrType _arg1=org.asam.ods.AttrTypeHelper.read(_input);
				_out = handler.createReply();
				org.asam.ods.NameSequenceHelper.write(_out,listAttributes(_arg0,_arg1));
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 31: // getValueSeq
			{
			try
			{
				java.lang.String[] _arg0=org.asam.ods.NameSequenceHelper.read(_input);
				_out = handler.createReply();
				org.asam.ods.NameValueUnitSequenceHelper.write(_out,getValueSeq(_arg0));
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 32: // compare
			{
			try
			{
				org.asam.ods.InstanceElement _arg0=org.asam.ods.InstanceElementHelper.read(_input);
				_out = handler.createReply();
				org.asam.ods.T_LONGLONGHelper.write(_out,compare(_arg0));
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 33: // exists
			{
			try
			{
				_out = handler.createReply();
				_out.write_boolean(exists());
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 34: // getValueInUnit
			{
			try
			{
				org.asam.ods.NameUnit _arg0=org.asam.ods.NameUnitHelper.read(_input);
				_out = handler.createReply();
				org.asam.ods.NameValueUnitHelper.write(_out,getValueInUnit(_arg0));
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 35: // createRelation
			{
			try
			{
				org.asam.ods.ApplicationRelation _arg0=org.asam.ods.ApplicationRelationHelper.read(_input);
				org.asam.ods.InstanceElement _arg1=org.asam.ods.InstanceElementHelper.read(_input);
				_out = handler.createReply();
				createRelation(_arg0,_arg1);
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 36: // takeUnderControl
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				_out = handler.createReply();
				takeUnderControl(_arg0);
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 37: // listRelatedInstancesByRelationship
			{
			try
			{
				org.asam.ods.Relationship _arg0=org.asam.ods.RelationshipHelper.read(_input);
				java.lang.String _arg1=_input.read_string();
				_out = handler.createReply();
				org.asam.ods.NameIteratorHelper.write(_out,listRelatedInstancesByRelationship(_arg0,_arg1));
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 38: // listRelatedInstances
			{
			try
			{
				org.asam.ods.ApplicationRelation _arg0=org.asam.ods.ApplicationRelationHelper.read(_input);
				java.lang.String _arg1=_input.read_string();
				_out = handler.createReply();
				org.asam.ods.NameIteratorHelper.write(_out,listRelatedInstances(_arg0,_arg1));
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 39: // getValue
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				_out = handler.createReply();
				org.asam.ods.NameValueUnitHelper.write(_out,getValue(_arg0));
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 40: // setInitialRights
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
			case 41: // read
			{
			try
			{
				_out = handler.createReply();
				org.asam.ods.ODSReadTransferHelper.write(_out,read());
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 42: // upcastODSFile
			{
			try
			{
				_out = handler.createReply();
				org.asam.ods.ODSFileHelper.write(_out,upcastODSFile());
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 43: // setName
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
		}
		return _out;
	}

	public String[] _all_interfaces(org.omg.PortableServer.POA poa, byte[] obj_id)
	{
		return ids;
	}
}
