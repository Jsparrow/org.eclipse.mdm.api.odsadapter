package org.asam.ods;


/**
 * Generated from IDL interface "NameValueIterator".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public class _NameValueIteratorStub
	extends org.omg.CORBA.portable.ObjectImpl
	implements org.asam.ods.NameValueIterator
{
	private String[] ids = {"IDL:org/asam/ods/NameValueIterator:1.0"};
	public String[] _ids()
	{
		return ids;
	}

	public final static java.lang.Class _opsClass = org.asam.ods.NameValueIteratorOperations.class;
	public org.asam.ods.NameValue nextOne() throws org.asam.ods.AoException
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "nextOne", true);
				_is = _invoke(_os);
				org.asam.ods.NameValue _result = org.asam.ods.NameValueHelper.read(_is);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "nextOne", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			NameValueIteratorOperations _localServant = (NameValueIteratorOperations)_so.servant;
			org.asam.ods.NameValue _result;
			try
			{
				_result = _localServant.nextOne();
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public org.asam.ods.NameValue[] nextN(int how_many) throws org.asam.ods.AoException
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "nextN", true);
				_os.write_long(how_many);
				_is = _invoke(_os);
				org.asam.ods.NameValue[] _result = org.asam.ods.NameValueSequenceHelper.read(_is);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "nextN", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			NameValueIteratorOperations _localServant = (NameValueIteratorOperations)_so.servant;
			org.asam.ods.NameValue[] _result;
			try
			{
				_result = _localServant.nextN(how_many);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public void reset() throws org.asam.ods.AoException
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "reset", true);
				_is = _invoke(_os);
				return;
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "reset", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			NameValueIteratorOperations _localServant = (NameValueIteratorOperations)_so.servant;
			try
			{
				_localServant.reset();
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public int getCount() throws org.asam.ods.AoException
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "getCount", true);
				_is = _invoke(_os);
				int _result = _is.read_long();
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "getCount", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			NameValueIteratorOperations _localServant = (NameValueIteratorOperations)_so.servant;
			int _result;
			try
			{
				_result = _localServant.getCount();
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public void destroy() throws org.asam.ods.AoException
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "destroy", true);
				_is = _invoke(_os);
				return;
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "destroy", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			NameValueIteratorOperations _localServant = (NameValueIteratorOperations)_so.servant;
			try
			{
				_localServant.destroy();
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

}
