package org.asam.ods;


/**
 * Generated from IDL interface "BaseStructure".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public abstract class BaseStructurePOA
	extends org.omg.PortableServer.Servant
	implements org.omg.CORBA.portable.InvokeHandler, org.asam.ods.BaseStructureOperations
{
	static private final java.util.Hashtable m_opsHash = new java.util.Hashtable();
	static
	{
		m_opsHash.put ( "listTopLevelElements", new java.lang.Integer(0));
		m_opsHash.put ( "getTopLevelElements", new java.lang.Integer(1));
		m_opsHash.put ( "getElements", new java.lang.Integer(2));
		m_opsHash.put ( "getElementByType", new java.lang.Integer(3));
		m_opsHash.put ( "getRelations", new java.lang.Integer(4));
		m_opsHash.put ( "listElements", new java.lang.Integer(5));
		m_opsHash.put ( "getVersion", new java.lang.Integer(6));
		m_opsHash.put ( "getRelation", new java.lang.Integer(7));
	}
	private String[] ids = {"IDL:org/asam/ods/BaseStructure:1.0"};
	public org.asam.ods.BaseStructure _this()
	{
		return org.asam.ods.BaseStructureHelper.narrow(_this_object());
	}
	public org.asam.ods.BaseStructure _this(org.omg.CORBA.ORB orb)
	{
		return org.asam.ods.BaseStructureHelper.narrow(_this_object(orb));
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
			case 0: // listTopLevelElements
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				_out = handler.createReply();
				org.asam.ods.BaseTypeSequenceHelper.write(_out,listTopLevelElements(_arg0));
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 1: // getTopLevelElements
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				_out = handler.createReply();
				org.asam.ods.BaseElementSequenceHelper.write(_out,getTopLevelElements(_arg0));
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 2: // getElements
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				_out = handler.createReply();
				org.asam.ods.BaseElementSequenceHelper.write(_out,getElements(_arg0));
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 3: // getElementByType
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				_out = handler.createReply();
				org.asam.ods.BaseElementHelper.write(_out,getElementByType(_arg0));
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 4: // getRelations
			{
			try
			{
				org.asam.ods.BaseElement _arg0=org.asam.ods.BaseElementHelper.read(_input);
				org.asam.ods.BaseElement _arg1=org.asam.ods.BaseElementHelper.read(_input);
				_out = handler.createReply();
				org.asam.ods.BaseRelationSequenceHelper.write(_out,getRelations(_arg0,_arg1));
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 5: // listElements
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				_out = handler.createReply();
				org.asam.ods.BaseTypeSequenceHelper.write(_out,listElements(_arg0));
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 6: // getVersion
			{
			try
			{
				_out = handler.createReply();
				_out.write_string(getVersion());
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 7: // getRelation
			{
			try
			{
				org.asam.ods.BaseElement _arg0=org.asam.ods.BaseElementHelper.read(_input);
				org.asam.ods.BaseElement _arg1=org.asam.ods.BaseElementHelper.read(_input);
				_out = handler.createReply();
				org.asam.ods.BaseRelationHelper.write(_out,getRelation(_arg0,_arg1));
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
