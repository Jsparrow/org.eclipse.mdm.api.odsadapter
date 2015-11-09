package org.asam.ods;


/**
 * Generated from IDL interface "BaseAttribute".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public class _BaseAttributeStub
	extends org.omg.CORBA.portable.ObjectImpl
	implements org.asam.ods.BaseAttribute
{
	private String[] ids = {"IDL:org/asam/ods/BaseAttribute:1.0"};
	public String[] _ids()
	{
		return ids;
	}

	public final static java.lang.Class _opsClass = org.asam.ods.BaseAttributeOperations.class;
	public boolean isUnique() throws org.asam.ods.AoException
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "isUnique", true);
				_is = _invoke(_os);
				boolean _result = _is.read_boolean();
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "isUnique", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			BaseAttributeOperations _localServant = (BaseAttributeOperations)_so.servant;
			boolean _result;
			try
			{
				_result = _localServant.isUnique();
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public org.asam.ods.EnumerationDefinition getEnumerationDefinition() throws org.asam.ods.AoException
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "getEnumerationDefinition", true);
				_is = _invoke(_os);
				org.asam.ods.EnumerationDefinition _result = org.asam.ods.EnumerationDefinitionHelper.read(_is);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "getEnumerationDefinition", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			BaseAttributeOperations _localServant = (BaseAttributeOperations)_so.servant;
			org.asam.ods.EnumerationDefinition _result;
			try
			{
				_result = _localServant.getEnumerationDefinition();
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public boolean isObligatory() throws org.asam.ods.AoException
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "isObligatory", true);
				_is = _invoke(_os);
				boolean _result = _is.read_boolean();
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "isObligatory", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			BaseAttributeOperations _localServant = (BaseAttributeOperations)_so.servant;
			boolean _result;
			try
			{
				_result = _localServant.isObligatory();
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public org.asam.ods.DataType getDataType() throws org.asam.ods.AoException
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "getDataType", true);
				_is = _invoke(_os);
				org.asam.ods.DataType _result = org.asam.ods.DataTypeHelper.read(_is);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "getDataType", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			BaseAttributeOperations _localServant = (BaseAttributeOperations)_so.servant;
			org.asam.ods.DataType _result;
			try
			{
				_result = _localServant.getDataType();
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public org.asam.ods.BaseElement getBaseElement() throws org.asam.ods.AoException
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "getBaseElement", true);
				_is = _invoke(_os);
				org.asam.ods.BaseElement _result = org.asam.ods.BaseElementHelper.read(_is);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "getBaseElement", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			BaseAttributeOperations _localServant = (BaseAttributeOperations)_so.servant;
			org.asam.ods.BaseElement _result;
			try
			{
				_result = _localServant.getBaseElement();
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public java.lang.String getName() throws org.asam.ods.AoException
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "getName", true);
				_is = _invoke(_os);
				java.lang.String _result = _is.read_string();
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "getName", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			BaseAttributeOperations _localServant = (BaseAttributeOperations)_so.servant;
			java.lang.String _result;
			try
			{
				_result = _localServant.getName();
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
