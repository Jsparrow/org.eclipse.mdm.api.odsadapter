package org.asam.ods;


/**
 * Generated from IDL interface "ApplElemAccess".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public abstract class ApplElemAccessPOA
	extends org.omg.PortableServer.Servant
	implements org.omg.CORBA.portable.InvokeHandler, org.asam.ods.ApplElemAccessOperations
{
	static private final java.util.Hashtable m_opsHash = new java.util.Hashtable();
	static
	{
		m_opsHash.put ( "updateInstances", new java.lang.Integer(0));
		m_opsHash.put ( "insertInstances", new java.lang.Integer(1));
		m_opsHash.put ( "setElementRights", new java.lang.Integer(2));
		m_opsHash.put ( "getAttributeRights", new java.lang.Integer(3));
		m_opsHash.put ( "getInitialRightReference", new java.lang.Integer(4));
		m_opsHash.put ( "setInitialRightReference", new java.lang.Integer(5));
		m_opsHash.put ( "setInstanceInitialRights", new java.lang.Integer(6));
		m_opsHash.put ( "getElementInitialRights", new java.lang.Integer(7));
		m_opsHash.put ( "getInstances", new java.lang.Integer(8));
		m_opsHash.put ( "setElementInitialRights", new java.lang.Integer(9));
		m_opsHash.put ( "getElementRights", new java.lang.Integer(10));
		m_opsHash.put ( "setInstanceRights", new java.lang.Integer(11));
		m_opsHash.put ( "getInstanceInitialRights", new java.lang.Integer(12));
		m_opsHash.put ( "getValueMatrix", new java.lang.Integer(13));
		m_opsHash.put ( "setAttributeRights", new java.lang.Integer(14));
		m_opsHash.put ( "getInstanceRights", new java.lang.Integer(15));
		m_opsHash.put ( "getInstancesExt", new java.lang.Integer(16));
		m_opsHash.put ( "deleteInstances", new java.lang.Integer(17));
		m_opsHash.put ( "getValueMatrixInMode", new java.lang.Integer(18));
		m_opsHash.put ( "getODSFile", new java.lang.Integer(19));
		m_opsHash.put ( "setRelInst", new java.lang.Integer(20));
		m_opsHash.put ( "getRelInst", new java.lang.Integer(21));
	}
	private String[] ids = {"IDL:org/asam/ods/ApplElemAccess:1.0"};
	public org.asam.ods.ApplElemAccess _this()
	{
		return org.asam.ods.ApplElemAccessHelper.narrow(_this_object());
	}
	public org.asam.ods.ApplElemAccess _this(org.omg.CORBA.ORB orb)
	{
		return org.asam.ods.ApplElemAccessHelper.narrow(_this_object(orb));
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
			case 0: // updateInstances
			{
			try
			{
				org.asam.ods.AIDNameValueSeqUnitId[] _arg0=org.asam.ods.AIDNameValueSeqUnitIdSequenceHelper.read(_input);
				_out = handler.createReply();
				updateInstances(_arg0);
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 1: // insertInstances
			{
			try
			{
				org.asam.ods.AIDNameValueSeqUnitId[] _arg0=org.asam.ods.AIDNameValueSeqUnitIdSequenceHelper.read(_input);
				_out = handler.createReply();
				org.asam.ods.ElemIdSequenceHelper.write(_out,insertInstances(_arg0));
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 2: // setElementRights
			{
			try
			{
				org.asam.ods.T_LONGLONG _arg0=org.asam.ods.T_LONGLONGHelper.read(_input);
				org.asam.ods.T_LONGLONG _arg1=org.asam.ods.T_LONGLONGHelper.read(_input);
				int _arg2=_input.read_long();
				org.asam.ods.RightsSet _arg3=org.asam.ods.RightsSetHelper.read(_input);
				_out = handler.createReply();
				setElementRights(_arg0,_arg1,_arg2,_arg3);
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 3: // getAttributeRights
			{
			try
			{
				org.asam.ods.T_LONGLONG _arg0=org.asam.ods.T_LONGLONGHelper.read(_input);
				java.lang.String _arg1=_input.read_string();
				_out = handler.createReply();
				org.asam.ods.ACLSequenceHelper.write(_out,getAttributeRights(_arg0,_arg1));
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 4: // getInitialRightReference
			{
			try
			{
				org.asam.ods.T_LONGLONG _arg0=org.asam.ods.T_LONGLONGHelper.read(_input);
				_out = handler.createReply();
				org.asam.ods.NameSequenceHelper.write(_out,getInitialRightReference(_arg0));
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 5: // setInitialRightReference
			{
			try
			{
				org.asam.ods.T_LONGLONG _arg0=org.asam.ods.T_LONGLONGHelper.read(_input);
				java.lang.String _arg1=_input.read_string();
				org.asam.ods.RightsSet _arg2=org.asam.ods.RightsSetHelper.read(_input);
				_out = handler.createReply();
				setInitialRightReference(_arg0,_arg1,_arg2);
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 6: // setInstanceInitialRights
			{
			try
			{
				org.asam.ods.T_LONGLONG _arg0=org.asam.ods.T_LONGLONGHelper.read(_input);
				org.asam.ods.T_LONGLONG[] _arg1=org.asam.ods.S_LONGLONGHelper.read(_input);
				org.asam.ods.T_LONGLONG _arg2=org.asam.ods.T_LONGLONGHelper.read(_input);
				int _arg3=_input.read_long();
				org.asam.ods.T_LONGLONG _arg4=org.asam.ods.T_LONGLONGHelper.read(_input);
				org.asam.ods.RightsSet _arg5=org.asam.ods.RightsSetHelper.read(_input);
				_out = handler.createReply();
				setInstanceInitialRights(_arg0,_arg1,_arg2,_arg3,_arg4,_arg5);
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 7: // getElementInitialRights
			{
			try
			{
				org.asam.ods.T_LONGLONG _arg0=org.asam.ods.T_LONGLONGHelper.read(_input);
				_out = handler.createReply();
				org.asam.ods.InitialRightSequenceHelper.write(_out,getElementInitialRights(_arg0));
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 8: // getInstances
			{
			try
			{
				org.asam.ods.QueryStructure _arg0=org.asam.ods.QueryStructureHelper.read(_input);
				int _arg1=_input.read_long();
				_out = handler.createReply();
				org.asam.ods.ElemResultSetSequenceHelper.write(_out,getInstances(_arg0,_arg1));
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 9: // setElementInitialRights
			{
			try
			{
				org.asam.ods.T_LONGLONG _arg0=org.asam.ods.T_LONGLONGHelper.read(_input);
				org.asam.ods.T_LONGLONG _arg1=org.asam.ods.T_LONGLONGHelper.read(_input);
				int _arg2=_input.read_long();
				org.asam.ods.T_LONGLONG _arg3=org.asam.ods.T_LONGLONGHelper.read(_input);
				org.asam.ods.RightsSet _arg4=org.asam.ods.RightsSetHelper.read(_input);
				_out = handler.createReply();
				setElementInitialRights(_arg0,_arg1,_arg2,_arg3,_arg4);
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 10: // getElementRights
			{
			try
			{
				org.asam.ods.T_LONGLONG _arg0=org.asam.ods.T_LONGLONGHelper.read(_input);
				_out = handler.createReply();
				org.asam.ods.ACLSequenceHelper.write(_out,getElementRights(_arg0));
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 11: // setInstanceRights
			{
			try
			{
				org.asam.ods.T_LONGLONG _arg0=org.asam.ods.T_LONGLONGHelper.read(_input);
				org.asam.ods.T_LONGLONG[] _arg1=org.asam.ods.S_LONGLONGHelper.read(_input);
				org.asam.ods.T_LONGLONG _arg2=org.asam.ods.T_LONGLONGHelper.read(_input);
				int _arg3=_input.read_long();
				org.asam.ods.RightsSet _arg4=org.asam.ods.RightsSetHelper.read(_input);
				_out = handler.createReply();
				setInstanceRights(_arg0,_arg1,_arg2,_arg3,_arg4);
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 12: // getInstanceInitialRights
			{
			try
			{
				org.asam.ods.T_LONGLONG _arg0=org.asam.ods.T_LONGLONGHelper.read(_input);
				org.asam.ods.T_LONGLONG _arg1=org.asam.ods.T_LONGLONGHelper.read(_input);
				_out = handler.createReply();
				org.asam.ods.InitialRightSequenceHelper.write(_out,getInstanceInitialRights(_arg0,_arg1));
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 13: // getValueMatrix
			{
			try
			{
				org.asam.ods.ElemId _arg0=org.asam.ods.ElemIdHelper.read(_input);
				_out = handler.createReply();
				org.asam.ods.ValueMatrixHelper.write(_out,getValueMatrix(_arg0));
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 14: // setAttributeRights
			{
			try
			{
				org.asam.ods.T_LONGLONG _arg0=org.asam.ods.T_LONGLONGHelper.read(_input);
				java.lang.String _arg1=_input.read_string();
				org.asam.ods.T_LONGLONG _arg2=org.asam.ods.T_LONGLONGHelper.read(_input);
				int _arg3=_input.read_long();
				org.asam.ods.RightsSet _arg4=org.asam.ods.RightsSetHelper.read(_input);
				_out = handler.createReply();
				setAttributeRights(_arg0,_arg1,_arg2,_arg3,_arg4);
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 15: // getInstanceRights
			{
			try
			{
				org.asam.ods.T_LONGLONG _arg0=org.asam.ods.T_LONGLONGHelper.read(_input);
				org.asam.ods.T_LONGLONG _arg1=org.asam.ods.T_LONGLONGHelper.read(_input);
				_out = handler.createReply();
				org.asam.ods.ACLSequenceHelper.write(_out,getInstanceRights(_arg0,_arg1));
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 16: // getInstancesExt
			{
			try
			{
				org.asam.ods.QueryStructureExt _arg0=org.asam.ods.QueryStructureExtHelper.read(_input);
				int _arg1=_input.read_long();
				_out = handler.createReply();
				org.asam.ods.ResultSetExtSequenceHelper.write(_out,getInstancesExt(_arg0,_arg1));
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 17: // deleteInstances
			{
			try
			{
				org.asam.ods.T_LONGLONG _arg0=org.asam.ods.T_LONGLONGHelper.read(_input);
				org.asam.ods.T_LONGLONG[] _arg1=org.asam.ods.S_LONGLONGHelper.read(_input);
				_out = handler.createReply();
				deleteInstances(_arg0,_arg1);
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 18: // getValueMatrixInMode
			{
			try
			{
				org.asam.ods.ElemId _arg0=org.asam.ods.ElemIdHelper.read(_input);
				org.asam.ods.ValueMatrixMode _arg1=org.asam.ods.ValueMatrixModeHelper.read(_input);
				_out = handler.createReply();
				org.asam.ods.ValueMatrixHelper.write(_out,getValueMatrixInMode(_arg0,_arg1));
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 19: // getODSFile
			{
			try
			{
				org.asam.ods.ElemId _arg0=org.asam.ods.ElemIdHelper.read(_input);
				_out = handler.createReply();
				org.asam.ods.ODSFileHelper.write(_out,getODSFile(_arg0));
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 20: // setRelInst
			{
			try
			{
				org.asam.ods.ElemId _arg0=org.asam.ods.ElemIdHelper.read(_input);
				java.lang.String _arg1=_input.read_string();
				org.asam.ods.T_LONGLONG[] _arg2=org.asam.ods.S_LONGLONGHelper.read(_input);
				org.asam.ods.SetType _arg3=org.asam.ods.SetTypeHelper.read(_input);
				_out = handler.createReply();
				setRelInst(_arg0,_arg1,_arg2,_arg3);
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 21: // getRelInst
			{
			try
			{
				org.asam.ods.ElemId _arg0=org.asam.ods.ElemIdHelper.read(_input);
				java.lang.String _arg1=_input.read_string();
				_out = handler.createReply();
				org.asam.ods.S_LONGLONGHelper.write(_out,getRelInst(_arg0,_arg1));
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
