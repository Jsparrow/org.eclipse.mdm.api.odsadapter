package org.asam.ods;

/**
 * Generated from IDL union "TS_UnionSeq".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 28.09.2015 13:19:28
 */

public final class TS_UnionSeqHelper
{
	private static org.omg.CORBA.TypeCode _type;
	public static void insert (final org.omg.CORBA.Any any, final org.asam.ods.TS_UnionSeq s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static org.asam.ods.TS_UnionSeq extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:org/asam/ods/TS_UnionSeq:1.0";
	}
	public static TS_UnionSeq read (org.omg.CORBA.portable.InputStream in)
	{
		TS_UnionSeq result = new TS_UnionSeq ();
		org.asam.ods.DataType disc = org.asam.ods.DataType.from_int(in.read_long());
		switch (disc.value ())
		{
			case org.asam.ods.DataType._DT_STRING:
			{
				java.lang.String[] _var;
				_var = org.asam.ods.S_STRINGHelper.read(in);
				result.stringVal (_var);
				break;
			}
			case org.asam.ods.DataType._DT_SHORT:
			{
				short[] _var;
				_var = org.asam.ods.S_SHORTHelper.read(in);
				result.shortVal (_var);
				break;
			}
			case org.asam.ods.DataType._DT_FLOAT:
			{
				float[] _var;
				_var = org.asam.ods.S_FLOATHelper.read(in);
				result.floatVal (_var);
				break;
			}
			case org.asam.ods.DataType._DT_BYTE:
			{
				byte[] _var;
				_var = org.asam.ods.S_BYTEHelper.read(in);
				result.byteVal (_var);
				break;
			}
			case org.asam.ods.DataType._DT_BOOLEAN:
			{
				boolean[] _var;
				_var = org.asam.ods.S_BOOLEANHelper.read(in);
				result.booleanVal (_var);
				break;
			}
			case org.asam.ods.DataType._DT_LONG:
			{
				int[] _var;
				_var = org.asam.ods.S_LONGHelper.read(in);
				result.longVal (_var);
				break;
			}
			case org.asam.ods.DataType._DT_DOUBLE:
			{
				double[] _var;
				_var = org.asam.ods.S_DOUBLEHelper.read(in);
				result.doubleVal (_var);
				break;
			}
			case org.asam.ods.DataType._DT_LONGLONG:
			{
				org.asam.ods.T_LONGLONG[] _var;
				_var = org.asam.ods.S_LONGLONGHelper.read(in);
				result.longlongVal (_var);
				break;
			}
			case org.asam.ods.DataType._DT_COMPLEX:
			{
				org.asam.ods.T_COMPLEX[] _var;
				_var = org.asam.ods.S_COMPLEXHelper.read(in);
				result.complexVal (_var);
				break;
			}
			case org.asam.ods.DataType._DT_DCOMPLEX:
			{
				org.asam.ods.T_DCOMPLEX[] _var;
				_var = org.asam.ods.S_DCOMPLEXHelper.read(in);
				result.dcomplexVal (_var);
				break;
			}
			case org.asam.ods.DataType._DT_DATE:
			{
				java.lang.String[] _var;
				_var = org.asam.ods.S_DATEHelper.read(in);
				result.dateVal (_var);
				break;
			}
			case org.asam.ods.DataType._DT_BYTESTR:
			{
				byte[][] _var;
				_var = org.asam.ods.S_BYTESTRHelper.read(in);
				result.bytestrVal (_var);
				break;
			}
			case org.asam.ods.DataType._DT_BLOB:
			{
				org.asam.ods.Blob[] _var;
				_var = org.asam.ods.S_BLOBHelper.read(in);
				result.blobVal (_var);
				break;
			}
			case org.asam.ods.DataType._DS_STRING:
			{
				java.lang.String[][] _var;
				_var = org.asam.ods.SS_STRINGHelper.read(in);
				result.stringSeq (_var);
				break;
			}
			case org.asam.ods.DataType._DS_SHORT:
			{
				short[][] _var;
				_var = org.asam.ods.SS_SHORTHelper.read(in);
				result.shortSeq (_var);
				break;
			}
			case org.asam.ods.DataType._DS_FLOAT:
			{
				float[][] _var;
				_var = org.asam.ods.SS_FLOATHelper.read(in);
				result.floatSeq (_var);
				break;
			}
			case org.asam.ods.DataType._DS_BYTE:
			{
				byte[][] _var;
				_var = org.asam.ods.SS_BYTEHelper.read(in);
				result.byteSeq (_var);
				break;
			}
			case org.asam.ods.DataType._DS_BOOLEAN:
			{
				boolean[][] _var;
				_var = org.asam.ods.SS_BOOLEANHelper.read(in);
				result.booleanSeq (_var);
				break;
			}
			case org.asam.ods.DataType._DS_LONG:
			{
				int[][] _var;
				_var = org.asam.ods.SS_LONGHelper.read(in);
				result.longSeq (_var);
				break;
			}
			case org.asam.ods.DataType._DS_DOUBLE:
			{
				double[][] _var;
				_var = org.asam.ods.SS_DOUBLEHelper.read(in);
				result.doubleSeq (_var);
				break;
			}
			case org.asam.ods.DataType._DS_LONGLONG:
			{
				org.asam.ods.T_LONGLONG[][] _var;
				_var = org.asam.ods.SS_LONGLONGHelper.read(in);
				result.longlongSeq (_var);
				break;
			}
			case org.asam.ods.DataType._DS_COMPLEX:
			{
				org.asam.ods.T_COMPLEX[][] _var;
				_var = org.asam.ods.SS_COMPLEXHelper.read(in);
				result.complexSeq (_var);
				break;
			}
			case org.asam.ods.DataType._DS_DCOMPLEX:
			{
				org.asam.ods.T_DCOMPLEX[][] _var;
				_var = org.asam.ods.SS_DCOMPLEXHelper.read(in);
				result.dcomplexSeq (_var);
				break;
			}
			case org.asam.ods.DataType._DS_DATE:
			{
				java.lang.String[][] _var;
				_var = org.asam.ods.SS_DATEHelper.read(in);
				result.dateSeq (_var);
				break;
			}
			case org.asam.ods.DataType._DS_BYTESTR:
			{
				byte[][][] _var;
				_var = org.asam.ods.SS_BYTESTRHelper.read(in);
				result.bytestrSeq (_var);
				break;
			}
			case org.asam.ods.DataType._DT_EXTERNALREFERENCE:
			{
				org.asam.ods.T_ExternalReference[] _var;
				_var = org.asam.ods.S_ExternalReferenceHelper.read(in);
				result.extRefVal (_var);
				break;
			}
			case org.asam.ods.DataType._DS_EXTERNALREFERENCE:
			{
				org.asam.ods.T_ExternalReference[][] _var;
				_var = org.asam.ods.SS_ExternalReferenceHelper.read(in);
				result.extRefSeq (_var);
				break;
			}
			case org.asam.ods.DataType._DT_ENUM:
			{
				int[] _var;
				_var = org.asam.ods.S_LONGHelper.read(in);
				result.enumVal (_var);
				break;
			}
			case org.asam.ods.DataType._DS_ENUM:
			{
				int[][] _var;
				_var = org.asam.ods.SS_LONGHelper.read(in);
				result.enumSeq (_var);
				break;
			}
			default: result.__default (disc);
		}
		return result;
	}
	public static void write (org.omg.CORBA.portable.OutputStream out, TS_UnionSeq s)
	{
		out.write_long (s.discriminator().value ());
		switch (s.discriminator().value ())
		{
			case org.asam.ods.DataType._DT_STRING:
			{
				org.asam.ods.S_STRINGHelper.write(out,s.stringVal ());
				break;
			}
			case org.asam.ods.DataType._DT_SHORT:
			{
				org.asam.ods.S_SHORTHelper.write(out,s.shortVal ());
				break;
			}
			case org.asam.ods.DataType._DT_FLOAT:
			{
				org.asam.ods.S_FLOATHelper.write(out,s.floatVal ());
				break;
			}
			case org.asam.ods.DataType._DT_BYTE:
			{
				org.asam.ods.S_BYTEHelper.write(out,s.byteVal ());
				break;
			}
			case org.asam.ods.DataType._DT_BOOLEAN:
			{
				org.asam.ods.S_BOOLEANHelper.write(out,s.booleanVal ());
				break;
			}
			case org.asam.ods.DataType._DT_LONG:
			{
				org.asam.ods.S_LONGHelper.write(out,s.longVal ());
				break;
			}
			case org.asam.ods.DataType._DT_DOUBLE:
			{
				org.asam.ods.S_DOUBLEHelper.write(out,s.doubleVal ());
				break;
			}
			case org.asam.ods.DataType._DT_LONGLONG:
			{
				org.asam.ods.S_LONGLONGHelper.write(out,s.longlongVal ());
				break;
			}
			case org.asam.ods.DataType._DT_COMPLEX:
			{
				org.asam.ods.S_COMPLEXHelper.write(out,s.complexVal ());
				break;
			}
			case org.asam.ods.DataType._DT_DCOMPLEX:
			{
				org.asam.ods.S_DCOMPLEXHelper.write(out,s.dcomplexVal ());
				break;
			}
			case org.asam.ods.DataType._DT_DATE:
			{
				org.asam.ods.S_DATEHelper.write(out,s.dateVal ());
				break;
			}
			case org.asam.ods.DataType._DT_BYTESTR:
			{
				org.asam.ods.S_BYTESTRHelper.write(out,s.bytestrVal ());
				break;
			}
			case org.asam.ods.DataType._DT_BLOB:
			{
				org.asam.ods.S_BLOBHelper.write(out,s.blobVal ());
				break;
			}
			case org.asam.ods.DataType._DS_STRING:
			{
				org.asam.ods.SS_STRINGHelper.write(out,s.stringSeq ());
				break;
			}
			case org.asam.ods.DataType._DS_SHORT:
			{
				org.asam.ods.SS_SHORTHelper.write(out,s.shortSeq ());
				break;
			}
			case org.asam.ods.DataType._DS_FLOAT:
			{
				org.asam.ods.SS_FLOATHelper.write(out,s.floatSeq ());
				break;
			}
			case org.asam.ods.DataType._DS_BYTE:
			{
				org.asam.ods.SS_BYTEHelper.write(out,s.byteSeq ());
				break;
			}
			case org.asam.ods.DataType._DS_BOOLEAN:
			{
				org.asam.ods.SS_BOOLEANHelper.write(out,s.booleanSeq ());
				break;
			}
			case org.asam.ods.DataType._DS_LONG:
			{
				org.asam.ods.SS_LONGHelper.write(out,s.longSeq ());
				break;
			}
			case org.asam.ods.DataType._DS_DOUBLE:
			{
				org.asam.ods.SS_DOUBLEHelper.write(out,s.doubleSeq ());
				break;
			}
			case org.asam.ods.DataType._DS_LONGLONG:
			{
				org.asam.ods.SS_LONGLONGHelper.write(out,s.longlongSeq ());
				break;
			}
			case org.asam.ods.DataType._DS_COMPLEX:
			{
				org.asam.ods.SS_COMPLEXHelper.write(out,s.complexSeq ());
				break;
			}
			case org.asam.ods.DataType._DS_DCOMPLEX:
			{
				org.asam.ods.SS_DCOMPLEXHelper.write(out,s.dcomplexSeq ());
				break;
			}
			case org.asam.ods.DataType._DS_DATE:
			{
				org.asam.ods.SS_DATEHelper.write(out,s.dateSeq ());
				break;
			}
			case org.asam.ods.DataType._DS_BYTESTR:
			{
				org.asam.ods.SS_BYTESTRHelper.write(out,s.bytestrSeq ());
				break;
			}
			case org.asam.ods.DataType._DT_EXTERNALREFERENCE:
			{
				org.asam.ods.S_ExternalReferenceHelper.write(out,s.extRefVal ());
				break;
			}
			case org.asam.ods.DataType._DS_EXTERNALREFERENCE:
			{
				org.asam.ods.SS_ExternalReferenceHelper.write(out,s.extRefSeq ());
				break;
			}
			case org.asam.ods.DataType._DT_ENUM:
			{
				org.asam.ods.S_LONGHelper.write(out,s.enumVal ());
				break;
			}
			case org.asam.ods.DataType._DS_ENUM:
			{
				org.asam.ods.SS_LONGHelper.write(out,s.enumSeq ());
				break;
			}
		}
	}
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			org.omg.CORBA.UnionMember[] members = new org.omg.CORBA.UnionMember[29];
			org.omg.CORBA.Any label_any;
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.asam.ods.DataTypeHelper.insert(label_any, org.asam.ods.DataType.DT_STRING);
			members[0] = new org.omg.CORBA.UnionMember ("stringVal", label_any, org.asam.ods.S_STRINGHelper.type(),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.asam.ods.DataTypeHelper.insert(label_any, org.asam.ods.DataType.DT_SHORT);
			members[1] = new org.omg.CORBA.UnionMember ("shortVal", label_any, org.asam.ods.S_SHORTHelper.type(),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.asam.ods.DataTypeHelper.insert(label_any, org.asam.ods.DataType.DT_FLOAT);
			members[2] = new org.omg.CORBA.UnionMember ("floatVal", label_any, org.asam.ods.S_FLOATHelper.type(),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.asam.ods.DataTypeHelper.insert(label_any, org.asam.ods.DataType.DT_BYTE);
			members[3] = new org.omg.CORBA.UnionMember ("byteVal", label_any, org.asam.ods.S_BYTEHelper.type(),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.asam.ods.DataTypeHelper.insert(label_any, org.asam.ods.DataType.DT_BOOLEAN);
			members[4] = new org.omg.CORBA.UnionMember ("booleanVal", label_any, org.asam.ods.S_BOOLEANHelper.type(),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.asam.ods.DataTypeHelper.insert(label_any, org.asam.ods.DataType.DT_LONG);
			members[5] = new org.omg.CORBA.UnionMember ("longVal", label_any, org.asam.ods.S_LONGHelper.type(),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.asam.ods.DataTypeHelper.insert(label_any, org.asam.ods.DataType.DT_DOUBLE);
			members[6] = new org.omg.CORBA.UnionMember ("doubleVal", label_any, org.asam.ods.S_DOUBLEHelper.type(),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.asam.ods.DataTypeHelper.insert(label_any, org.asam.ods.DataType.DT_LONGLONG);
			members[7] = new org.omg.CORBA.UnionMember ("longlongVal", label_any, org.asam.ods.S_LONGLONGHelper.type(),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.asam.ods.DataTypeHelper.insert(label_any, org.asam.ods.DataType.DT_COMPLEX);
			members[8] = new org.omg.CORBA.UnionMember ("complexVal", label_any, org.asam.ods.S_COMPLEXHelper.type(),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.asam.ods.DataTypeHelper.insert(label_any, org.asam.ods.DataType.DT_DCOMPLEX);
			members[9] = new org.omg.CORBA.UnionMember ("dcomplexVal", label_any, org.asam.ods.S_DCOMPLEXHelper.type(),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.asam.ods.DataTypeHelper.insert(label_any, org.asam.ods.DataType.DT_DATE);
			members[10] = new org.omg.CORBA.UnionMember ("dateVal", label_any, org.asam.ods.S_DATEHelper.type(),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.asam.ods.DataTypeHelper.insert(label_any, org.asam.ods.DataType.DT_BYTESTR);
			members[11] = new org.omg.CORBA.UnionMember ("bytestrVal", label_any, org.asam.ods.S_BYTESTRHelper.type(),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.asam.ods.DataTypeHelper.insert(label_any, org.asam.ods.DataType.DT_BLOB);
			members[12] = new org.omg.CORBA.UnionMember ("blobVal", label_any, org.asam.ods.S_BLOBHelper.type(),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.asam.ods.DataTypeHelper.insert(label_any, org.asam.ods.DataType.DS_STRING);
			members[13] = new org.omg.CORBA.UnionMember ("stringSeq", label_any, org.asam.ods.SS_STRINGHelper.type(),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.asam.ods.DataTypeHelper.insert(label_any, org.asam.ods.DataType.DS_SHORT);
			members[14] = new org.omg.CORBA.UnionMember ("shortSeq", label_any, org.asam.ods.SS_SHORTHelper.type(),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.asam.ods.DataTypeHelper.insert(label_any, org.asam.ods.DataType.DS_FLOAT);
			members[15] = new org.omg.CORBA.UnionMember ("floatSeq", label_any, org.asam.ods.SS_FLOATHelper.type(),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.asam.ods.DataTypeHelper.insert(label_any, org.asam.ods.DataType.DS_BYTE);
			members[16] = new org.omg.CORBA.UnionMember ("byteSeq", label_any, org.asam.ods.SS_BYTEHelper.type(),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.asam.ods.DataTypeHelper.insert(label_any, org.asam.ods.DataType.DS_BOOLEAN);
			members[17] = new org.omg.CORBA.UnionMember ("booleanSeq", label_any, org.asam.ods.SS_BOOLEANHelper.type(),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.asam.ods.DataTypeHelper.insert(label_any, org.asam.ods.DataType.DS_LONG);
			members[18] = new org.omg.CORBA.UnionMember ("longSeq", label_any, org.asam.ods.SS_LONGHelper.type(),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.asam.ods.DataTypeHelper.insert(label_any, org.asam.ods.DataType.DS_DOUBLE);
			members[19] = new org.omg.CORBA.UnionMember ("doubleSeq", label_any, org.asam.ods.SS_DOUBLEHelper.type(),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.asam.ods.DataTypeHelper.insert(label_any, org.asam.ods.DataType.DS_LONGLONG);
			members[20] = new org.omg.CORBA.UnionMember ("longlongSeq", label_any, org.asam.ods.SS_LONGLONGHelper.type(),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.asam.ods.DataTypeHelper.insert(label_any, org.asam.ods.DataType.DS_COMPLEX);
			members[21] = new org.omg.CORBA.UnionMember ("complexSeq", label_any, org.asam.ods.SS_COMPLEXHelper.type(),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.asam.ods.DataTypeHelper.insert(label_any, org.asam.ods.DataType.DS_DCOMPLEX);
			members[22] = new org.omg.CORBA.UnionMember ("dcomplexSeq", label_any, org.asam.ods.SS_DCOMPLEXHelper.type(),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.asam.ods.DataTypeHelper.insert(label_any, org.asam.ods.DataType.DS_DATE);
			members[23] = new org.omg.CORBA.UnionMember ("dateSeq", label_any, org.asam.ods.SS_DATEHelper.type(),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.asam.ods.DataTypeHelper.insert(label_any, org.asam.ods.DataType.DS_BYTESTR);
			members[24] = new org.omg.CORBA.UnionMember ("bytestrSeq", label_any, org.asam.ods.SS_BYTESTRHelper.type(),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.asam.ods.DataTypeHelper.insert(label_any, org.asam.ods.DataType.DT_EXTERNALREFERENCE);
			members[25] = new org.omg.CORBA.UnionMember ("extRefVal", label_any, org.asam.ods.S_ExternalReferenceHelper.type(),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.asam.ods.DataTypeHelper.insert(label_any, org.asam.ods.DataType.DS_EXTERNALREFERENCE);
			members[26] = new org.omg.CORBA.UnionMember ("extRefSeq", label_any, org.asam.ods.SS_ExternalReferenceHelper.type(),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.asam.ods.DataTypeHelper.insert(label_any, org.asam.ods.DataType.DT_ENUM);
			members[27] = new org.omg.CORBA.UnionMember ("enumVal", label_any, org.asam.ods.S_LONGHelper.type(),null);
			label_any = org.omg.CORBA.ORB.init().create_any ();
			org.asam.ods.DataTypeHelper.insert(label_any, org.asam.ods.DataType.DS_ENUM);
			members[28] = new org.omg.CORBA.UnionMember ("enumSeq", label_any, org.asam.ods.SS_LONGHelper.type(),null);
			 _type = org.omg.CORBA.ORB.init().create_union_tc(id(),"TS_UnionSeq",org.asam.ods.DataTypeHelper.type(), members);
		}
		return _type;
	}
}
