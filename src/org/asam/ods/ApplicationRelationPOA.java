package org.asam.ods;


/**
 * Generated from IDL interface "ApplicationRelation".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public abstract class ApplicationRelationPOA
	extends org.omg.PortableServer.Servant
	implements org.omg.CORBA.portable.InvokeHandler, org.asam.ods.ApplicationRelationOperations
{
	static private final java.util.Hashtable m_opsHash = new java.util.Hashtable();
	static
	{
		m_opsHash.put ( "setRelationRange", new java.lang.Integer(0));
		m_opsHash.put ( "getBaseRelation", new java.lang.Integer(1));
		m_opsHash.put ( "setRelationType", new java.lang.Integer(2));
		m_opsHash.put ( "setBaseRelation", new java.lang.Integer(3));
		m_opsHash.put ( "getElem1", new java.lang.Integer(4));
		m_opsHash.put ( "getInverseRelationRange", new java.lang.Integer(5));
		m_opsHash.put ( "setInverseRelationRange", new java.lang.Integer(6));
		m_opsHash.put ( "getInverseRelationship", new java.lang.Integer(7));
		m_opsHash.put ( "setElem2", new java.lang.Integer(8));
		m_opsHash.put ( "getRelationship", new java.lang.Integer(9));
		m_opsHash.put ( "getRelationRange", new java.lang.Integer(10));
		m_opsHash.put ( "setInverseRelationName", new java.lang.Integer(11));
		m_opsHash.put ( "setElem1", new java.lang.Integer(12));
		m_opsHash.put ( "getInverseRelationName", new java.lang.Integer(13));
		m_opsHash.put ( "getRelationName", new java.lang.Integer(14));
		m_opsHash.put ( "getRelationType", new java.lang.Integer(15));
		m_opsHash.put ( "getElem2", new java.lang.Integer(16));
		m_opsHash.put ( "setRelationName", new java.lang.Integer(17));
	}
	private String[] ids = {"IDL:org/asam/ods/ApplicationRelation:1.0"};
	public org.asam.ods.ApplicationRelation _this()
	{
		return org.asam.ods.ApplicationRelationHelper.narrow(_this_object());
	}
	public org.asam.ods.ApplicationRelation _this(org.omg.CORBA.ORB orb)
	{
		return org.asam.ods.ApplicationRelationHelper.narrow(_this_object(orb));
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
			case 0: // setRelationRange
			{
			try
			{
				org.asam.ods.RelationRange _arg0=org.asam.ods.RelationRangeHelper.read(_input);
				_out = handler.createReply();
				setRelationRange(_arg0);
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 1: // getBaseRelation
			{
			try
			{
				_out = handler.createReply();
				org.asam.ods.BaseRelationHelper.write(_out,getBaseRelation());
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 2: // setRelationType
			{
			try
			{
				org.asam.ods.RelationType _arg0=org.asam.ods.RelationTypeHelper.read(_input);
				_out = handler.createReply();
				setRelationType(_arg0);
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 3: // setBaseRelation
			{
			try
			{
				org.asam.ods.BaseRelation _arg0=org.asam.ods.BaseRelationHelper.read(_input);
				_out = handler.createReply();
				setBaseRelation(_arg0);
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 4: // getElem1
			{
			try
			{
				_out = handler.createReply();
				org.asam.ods.ApplicationElementHelper.write(_out,getElem1());
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 5: // getInverseRelationRange
			{
			try
			{
				_out = handler.createReply();
				org.asam.ods.RelationRangeHelper.write(_out,getInverseRelationRange());
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 6: // setInverseRelationRange
			{
			try
			{
				org.asam.ods.RelationRange _arg0=org.asam.ods.RelationRangeHelper.read(_input);
				_out = handler.createReply();
				setInverseRelationRange(_arg0);
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 7: // getInverseRelationship
			{
			try
			{
				_out = handler.createReply();
				org.asam.ods.RelationshipHelper.write(_out,getInverseRelationship());
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 8: // setElem2
			{
			try
			{
				org.asam.ods.ApplicationElement _arg0=org.asam.ods.ApplicationElementHelper.read(_input);
				_out = handler.createReply();
				setElem2(_arg0);
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 9: // getRelationship
			{
			try
			{
				_out = handler.createReply();
				org.asam.ods.RelationshipHelper.write(_out,getRelationship());
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 10: // getRelationRange
			{
			try
			{
				_out = handler.createReply();
				org.asam.ods.RelationRangeHelper.write(_out,getRelationRange());
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 11: // setInverseRelationName
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				_out = handler.createReply();
				setInverseRelationName(_arg0);
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 12: // setElem1
			{
			try
			{
				org.asam.ods.ApplicationElement _arg0=org.asam.ods.ApplicationElementHelper.read(_input);
				_out = handler.createReply();
				setElem1(_arg0);
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 13: // getInverseRelationName
			{
			try
			{
				_out = handler.createReply();
				_out.write_string(getInverseRelationName());
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 14: // getRelationName
			{
			try
			{
				_out = handler.createReply();
				_out.write_string(getRelationName());
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 15: // getRelationType
			{
			try
			{
				_out = handler.createReply();
				org.asam.ods.RelationTypeHelper.write(_out,getRelationType());
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 16: // getElem2
			{
			try
			{
				_out = handler.createReply();
				org.asam.ods.ApplicationElementHelper.write(_out,getElem2());
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 17: // setRelationName
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				_out = handler.createReply();
				setRelationName(_arg0);
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
