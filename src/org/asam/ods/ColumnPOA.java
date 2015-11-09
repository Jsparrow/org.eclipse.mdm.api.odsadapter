package org.asam.ods;


/**
 * Generated from IDL interface "Column".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public abstract class ColumnPOA
	extends org.omg.PortableServer.Servant
	implements org.omg.CORBA.portable.InvokeHandler, org.asam.ods.ColumnOperations
{
	static private final java.util.Hashtable m_opsHash = new java.util.Hashtable();
	static
	{
		m_opsHash.put ( "getSourceMQ", new java.lang.Integer(0));
		m_opsHash.put ( "isIndependent", new java.lang.Integer(1));
		m_opsHash.put ( "setScaling", new java.lang.Integer(2));
		m_opsHash.put ( "setSequenceRepresentation", new java.lang.Integer(3));
		m_opsHash.put ( "isScaling", new java.lang.Integer(4));
		m_opsHash.put ( "getGenerationParameters", new java.lang.Integer(5));
		m_opsHash.put ( "setUnit", new java.lang.Integer(6));
		m_opsHash.put ( "setFormula", new java.lang.Integer(7));
		m_opsHash.put ( "getDataType", new java.lang.Integer(8));
		m_opsHash.put ( "getRawDataType", new java.lang.Integer(9));
		m_opsHash.put ( "getName", new java.lang.Integer(10));
		m_opsHash.put ( "destroy", new java.lang.Integer(11));
		m_opsHash.put ( "getSequenceRepresentation", new java.lang.Integer(12));
		m_opsHash.put ( "setGenerationParameters", new java.lang.Integer(13));
		m_opsHash.put ( "setIndependent", new java.lang.Integer(14));
		m_opsHash.put ( "getFormula", new java.lang.Integer(15));
		m_opsHash.put ( "getUnit", new java.lang.Integer(16));
	}
	private String[] ids = {"IDL:org/asam/ods/Column:1.0"};
	public org.asam.ods.Column _this()
	{
		return org.asam.ods.ColumnHelper.narrow(_this_object());
	}
	public org.asam.ods.Column _this(org.omg.CORBA.ORB orb)
	{
		return org.asam.ods.ColumnHelper.narrow(_this_object(orb));
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
			case 0: // getSourceMQ
			{
			try
			{
				_out = handler.createReply();
				org.asam.ods.InstanceElementHelper.write(_out,getSourceMQ());
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 1: // isIndependent
			{
			try
			{
				_out = handler.createReply();
				_out.write_boolean(isIndependent());
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 2: // setScaling
			{
			try
			{
				boolean _arg0=_input.read_boolean();
				_out = handler.createReply();
				setScaling(_arg0);
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 3: // setSequenceRepresentation
			{
			try
			{
				int _arg0=_input.read_long();
				_out = handler.createReply();
				setSequenceRepresentation(_arg0);
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 4: // isScaling
			{
			try
			{
				_out = handler.createReply();
				_out.write_boolean(isScaling());
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 5: // getGenerationParameters
			{
			try
			{
				_out = handler.createReply();
				org.asam.ods.TS_UnionHelper.write(_out,getGenerationParameters());
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 6: // setUnit
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				_out = handler.createReply();
				setUnit(_arg0);
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 7: // setFormula
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				_out = handler.createReply();
				setFormula(_arg0);
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 8: // getDataType
			{
			try
			{
				_out = handler.createReply();
				org.asam.ods.DataTypeHelper.write(_out,getDataType());
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 9: // getRawDataType
			{
			try
			{
				_out = handler.createReply();
				org.asam.ods.DataTypeHelper.write(_out,getRawDataType());
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 10: // getName
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
			case 11: // destroy
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
			case 12: // getSequenceRepresentation
			{
			try
			{
				_out = handler.createReply();
				_out.write_long(getSequenceRepresentation());
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 13: // setGenerationParameters
			{
			try
			{
				org.asam.ods.TS_Union _arg0=org.asam.ods.TS_UnionHelper.read(_input);
				_out = handler.createReply();
				setGenerationParameters(_arg0);
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 14: // setIndependent
			{
			try
			{
				boolean _arg0=_input.read_boolean();
				_out = handler.createReply();
				setIndependent(_arg0);
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 15: // getFormula
			{
			try
			{
				_out = handler.createReply();
				_out.write_string(getFormula());
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 16: // getUnit
			{
			try
			{
				_out = handler.createReply();
				_out.write_string(getUnit());
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
