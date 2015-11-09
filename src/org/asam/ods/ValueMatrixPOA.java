package org.asam.ods;


/**
 * Generated from IDL interface "ValueMatrix".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public abstract class ValueMatrixPOA
	extends org.omg.PortableServer.Servant
	implements org.omg.CORBA.portable.InvokeHandler, org.asam.ods.ValueMatrixOperations
{
	static private final java.util.Hashtable m_opsHash = new java.util.Hashtable();
	static
	{
		m_opsHash.put ( "listIndependentColumns", new java.lang.Integer(0));
		m_opsHash.put ( "removeValueVector", new java.lang.Integer(1));
		m_opsHash.put ( "addColumn", new java.lang.Integer(2));
		m_opsHash.put ( "addColumnScaledBy", new java.lang.Integer(3));
		m_opsHash.put ( "getMode", new java.lang.Integer(4));
		m_opsHash.put ( "removeValueMeaPoint", new java.lang.Integer(5));
		m_opsHash.put ( "listScalingColumns", new java.lang.Integer(6));
		m_opsHash.put ( "getColumnsScaledBy", new java.lang.Integer(7));
		m_opsHash.put ( "getIndependentColumns", new java.lang.Integer(8));
		m_opsHash.put ( "setValueVector", new java.lang.Integer(9));
		m_opsHash.put ( "listColumnsScaledBy", new java.lang.Integer(10));
		m_opsHash.put ( "getRowCount", new java.lang.Integer(11));
		m_opsHash.put ( "setValueMeaPoint", new java.lang.Integer(12));
		m_opsHash.put ( "getValueVector", new java.lang.Integer(13));
		m_opsHash.put ( "listColumns", new java.lang.Integer(14));
		m_opsHash.put ( "getValue", new java.lang.Integer(15));
		m_opsHash.put ( "getColumnCount", new java.lang.Integer(16));
		m_opsHash.put ( "setValue", new java.lang.Integer(17));
		m_opsHash.put ( "getScalingColumns", new java.lang.Integer(18));
		m_opsHash.put ( "getColumns", new java.lang.Integer(19));
		m_opsHash.put ( "destroy", new java.lang.Integer(20));
		m_opsHash.put ( "getValueMeaPoint", new java.lang.Integer(21));
	}
	private String[] ids = {"IDL:org/asam/ods/ValueMatrix:1.0"};
	public org.asam.ods.ValueMatrix _this()
	{
		return org.asam.ods.ValueMatrixHelper.narrow(_this_object());
	}
	public org.asam.ods.ValueMatrix _this(org.omg.CORBA.ORB orb)
	{
		return org.asam.ods.ValueMatrixHelper.narrow(_this_object(orb));
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
			case 0: // listIndependentColumns
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				_out = handler.createReply();
				org.asam.ods.NameSequenceHelper.write(_out,listIndependentColumns(_arg0));
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 1: // removeValueVector
			{
			try
			{
				org.asam.ods.Column _arg0=org.asam.ods.ColumnHelper.read(_input);
				int _arg1=_input.read_long();
				int _arg2=_input.read_long();
				_out = handler.createReply();
				removeValueVector(_arg0,_arg1,_arg2);
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 2: // addColumn
			{
			try
			{
				org.asam.ods.NameUnit _arg0=org.asam.ods.NameUnitHelper.read(_input);
				_out = handler.createReply();
				org.asam.ods.ColumnHelper.write(_out,addColumn(_arg0));
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 3: // addColumnScaledBy
			{
			try
			{
				org.asam.ods.NameUnit _arg0=org.asam.ods.NameUnitHelper.read(_input);
				org.asam.ods.Column _arg1=org.asam.ods.ColumnHelper.read(_input);
				_out = handler.createReply();
				org.asam.ods.ColumnHelper.write(_out,addColumnScaledBy(_arg0,_arg1));
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 4: // getMode
			{
			try
			{
				_out = handler.createReply();
				org.asam.ods.ValueMatrixModeHelper.write(_out,getMode());
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 5: // removeValueMeaPoint
			{
			try
			{
				java.lang.String[] _arg0=org.asam.ods.NameSequenceHelper.read(_input);
				int _arg1=_input.read_long();
				int _arg2=_input.read_long();
				_out = handler.createReply();
				removeValueMeaPoint(_arg0,_arg1,_arg2);
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 6: // listScalingColumns
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				_out = handler.createReply();
				org.asam.ods.NameSequenceHelper.write(_out,listScalingColumns(_arg0));
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 7: // getColumnsScaledBy
			{
			try
			{
				org.asam.ods.Column _arg0=org.asam.ods.ColumnHelper.read(_input);
				_out = handler.createReply();
				org.asam.ods.ColumnSequenceHelper.write(_out,getColumnsScaledBy(_arg0));
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 8: // getIndependentColumns
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				_out = handler.createReply();
				org.asam.ods.ColumnSequenceHelper.write(_out,getIndependentColumns(_arg0));
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 9: // setValueVector
			{
			try
			{
				org.asam.ods.Column _arg0=org.asam.ods.ColumnHelper.read(_input);
				org.asam.ods.SetType _arg1=org.asam.ods.SetTypeHelper.read(_input);
				int _arg2=_input.read_long();
				org.asam.ods.TS_ValueSeq _arg3=org.asam.ods.TS_ValueSeqHelper.read(_input);
				_out = handler.createReply();
				setValueVector(_arg0,_arg1,_arg2,_arg3);
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 10: // listColumnsScaledBy
			{
			try
			{
				org.asam.ods.Column _arg0=org.asam.ods.ColumnHelper.read(_input);
				_out = handler.createReply();
				org.asam.ods.NameSequenceHelper.write(_out,listColumnsScaledBy(_arg0));
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 11: // getRowCount
			{
			try
			{
				_out = handler.createReply();
				_out.write_long(getRowCount());
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 12: // setValueMeaPoint
			{
			try
			{
				org.asam.ods.SetType _arg0=org.asam.ods.SetTypeHelper.read(_input);
				int _arg1=_input.read_long();
				org.asam.ods.NameValue[] _arg2=org.asam.ods.NameValueSequenceHelper.read(_input);
				_out = handler.createReply();
				setValueMeaPoint(_arg0,_arg1,_arg2);
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 13: // getValueVector
			{
			try
			{
				org.asam.ods.Column _arg0=org.asam.ods.ColumnHelper.read(_input);
				int _arg1=_input.read_long();
				int _arg2=_input.read_long();
				_out = handler.createReply();
				org.asam.ods.TS_ValueSeqHelper.write(_out,getValueVector(_arg0,_arg1,_arg2));
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 14: // listColumns
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				_out = handler.createReply();
				org.asam.ods.NameSequenceHelper.write(_out,listColumns(_arg0));
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 15: // getValue
			{
			try
			{
				org.asam.ods.Column[] _arg0=org.asam.ods.ColumnSequenceHelper.read(_input);
				int _arg1=_input.read_long();
				int _arg2=_input.read_long();
				_out = handler.createReply();
				org.asam.ods.NameValueSeqUnitSequenceHelper.write(_out,getValue(_arg0,_arg1,_arg2));
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 16: // getColumnCount
			{
			try
			{
				_out = handler.createReply();
				_out.write_long(getColumnCount());
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 17: // setValue
			{
			try
			{
				org.asam.ods.SetType _arg0=org.asam.ods.SetTypeHelper.read(_input);
				int _arg1=_input.read_long();
				org.asam.ods.NameValueSeqUnit[] _arg2=org.asam.ods.NameValueSeqUnitSequenceHelper.read(_input);
				_out = handler.createReply();
				setValue(_arg0,_arg1,_arg2);
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 18: // getScalingColumns
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				_out = handler.createReply();
				org.asam.ods.ColumnSequenceHelper.write(_out,getScalingColumns(_arg0));
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 19: // getColumns
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				_out = handler.createReply();
				org.asam.ods.ColumnSequenceHelper.write(_out,getColumns(_arg0));
			}
			catch(org.asam.ods.AoException _ex0)
			{
				_out = handler.createExceptionReply();
				org.asam.ods.AoExceptionHelper.write(_out, _ex0);
			}
				break;
			}
			case 20: // destroy
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
			case 21: // getValueMeaPoint
			{
			try
			{
				int _arg0=_input.read_long();
				_out = handler.createReply();
				org.asam.ods.NameValueUnitIteratorHelper.write(_out,getValueMeaPoint(_arg0));
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
