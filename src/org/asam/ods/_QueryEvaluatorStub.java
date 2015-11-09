package org.asam.ods;


/**
 * Generated from IDL interface "QueryEvaluator".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public class _QueryEvaluatorStub
	extends org.omg.CORBA.portable.ObjectImpl
	implements org.asam.ods.QueryEvaluator
{
	private String[] ids = {"IDL:org/asam/ods/QueryEvaluator:1.0"};
	public String[] _ids()
	{
		return ids;
	}

	public final static java.lang.Class _opsClass = org.asam.ods.QueryEvaluatorOperations.class;
	public org.asam.ods.Query createQuery(java.lang.String queryStr, org.asam.ods.NameValue[] params) throws org.asam.ods.AoException
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "createQuery", true);
				_os.write_string(queryStr);
				org.asam.ods.NameValueSequenceHelper.write(_os,params);
				_is = _invoke(_os);
				org.asam.ods.Query _result = org.asam.ods.QueryHelper.read(_is);
				return _result;
			}
			catch( org.omg.CORBA.portable.RemarshalException _rx ){}
			catch( org.omg.CORBA.portable.ApplicationException _ax )
			{
				String _id = _ax.getId();
				if( _id.equals("IDL:org/asam/ods/AoException:1.0"))
				{
					throw org.asam.ods.AoExceptionHelper.read(_ax.getInputStream());
				}
				throw new RuntimeException("Unexpected exception " + _id );
			}
			finally
			{
				this._releaseReply(_is);
			}
		}
		else
		{
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "createQuery", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			QueryEvaluatorOperations _localServant = (QueryEvaluatorOperations)_so.servant;
			org.asam.ods.Query _result;
			try
			{
				_result = _localServant.createQuery(queryStr,params);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public org.asam.ods.NameValueSeqUnit[] getTable(java.lang.String queryStr, org.asam.ods.NameValue[] params) throws org.asam.ods.AoException
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "getTable", true);
				_os.write_string(queryStr);
				org.asam.ods.NameValueSequenceHelper.write(_os,params);
				_is = _invoke(_os);
				org.asam.ods.NameValueSeqUnit[] _result = org.asam.ods.NameValueSeqUnitSequenceHelper.read(_is);
				return _result;
			}
			catch( org.omg.CORBA.portable.RemarshalException _rx ){}
			catch( org.omg.CORBA.portable.ApplicationException _ax )
			{
				String _id = _ax.getId();
				if( _id.equals("IDL:org/asam/ods/AoException:1.0"))
				{
					throw org.asam.ods.AoExceptionHelper.read(_ax.getInputStream());
				}
				throw new RuntimeException("Unexpected exception " + _id );
			}
			finally
			{
				this._releaseReply(_is);
			}
		}
		else
		{
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "getTable", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			QueryEvaluatorOperations _localServant = (QueryEvaluatorOperations)_so.servant;
			org.asam.ods.NameValueSeqUnit[] _result;
			try
			{
				_result = _localServant.getTable(queryStr,params);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public org.asam.ods.NameValueUnitSequenceIterator getTableRows(java.lang.String queryStr, org.asam.ods.NameValue[] params) throws org.asam.ods.AoException
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "getTableRows", true);
				_os.write_string(queryStr);
				org.asam.ods.NameValueSequenceHelper.write(_os,params);
				_is = _invoke(_os);
				org.asam.ods.NameValueUnitSequenceIterator _result = org.asam.ods.NameValueUnitSequenceIteratorHelper.read(_is);
				return _result;
			}
			catch( org.omg.CORBA.portable.RemarshalException _rx ){}
			catch( org.omg.CORBA.portable.ApplicationException _ax )
			{
				String _id = _ax.getId();
				if( _id.equals("IDL:org/asam/ods/AoException:1.0"))
				{
					throw org.asam.ods.AoExceptionHelper.read(_ax.getInputStream());
				}
				throw new RuntimeException("Unexpected exception " + _id );
			}
			finally
			{
				this._releaseReply(_is);
			}
		}
		else
		{
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "getTableRows", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			QueryEvaluatorOperations _localServant = (QueryEvaluatorOperations)_so.servant;
			org.asam.ods.NameValueUnitSequenceIterator _result;
			try
			{
				_result = _localServant.getTableRows(queryStr,params);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public org.asam.ods.InstanceElementIterator getInstances(java.lang.String queryStr, org.asam.ods.NameValue[] params) throws org.asam.ods.AoException
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "getInstances", true);
				_os.write_string(queryStr);
				org.asam.ods.NameValueSequenceHelper.write(_os,params);
				_is = _invoke(_os);
				org.asam.ods.InstanceElementIterator _result = org.asam.ods.InstanceElementIteratorHelper.read(_is);
				return _result;
			}
			catch( org.omg.CORBA.portable.RemarshalException _rx ){}
			catch( org.omg.CORBA.portable.ApplicationException _ax )
			{
				String _id = _ax.getId();
				if( _id.equals("IDL:org/asam/ods/AoException:1.0"))
				{
					throw org.asam.ods.AoExceptionHelper.read(_ax.getInputStream());
				}
				throw new RuntimeException("Unexpected exception " + _id );
			}
			finally
			{
				this._releaseReply(_is);
			}
		}
		else
		{
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "getInstances", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			QueryEvaluatorOperations _localServant = (QueryEvaluatorOperations)_so.servant;
			org.asam.ods.InstanceElementIterator _result;
			try
			{
				_result = _localServant.getInstances(queryStr,params);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

}
