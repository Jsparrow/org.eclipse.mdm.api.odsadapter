/*
 * Copyright (c) 2016 Gigatronik Ingolstadt GmbH
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.mdm.api.odsadapter.utils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.mdm.api.base.model.AxisType;
import org.eclipse.mdm.api.base.model.Interpolation;
import org.eclipse.mdm.api.base.model.ScalarType;
import org.eclipse.mdm.api.base.model.SequenceRepresentation;
import org.eclipse.mdm.api.base.model.TypeSpecification;
import org.eclipse.mdm.api.base.model.VersionState;
import org.eclipse.mdm.api.base.query.DataAccessException;

public final class ODSEnumerations {

	private static final String SCALAR_TYPE_NAME = "datatype_enum";
	private static final String STATE_NAME = "valid_enum";
	private static final String INTERPOLATION_NAME = "interpolation_enum";
	private static final String AXIS_TYPE_NAME = "axistype";
	private static final String TYPE_SPECIFICATION_NAME = "typespec_enum";
	private static final String SEQUENCE_REPRESENTATION_NAME = "seq_rep_enum";

	private ODSEnumerations() {}

	/*
	 * TODO: for some enumeration types we have a direct mapping from ODS value
	 * to the enumeration constant and vice versa. For all of them the
	 * conversion is done by the ordinal number of the enumeration constant.
	 *
	 * So if enumeration constant ordering is changed this will break the
	 * validity -> we either need to ensure validity by providing tests or
	 * provide dedicated methods for individual mapping without the enumeration
	 * constant's ordinal number!
	 */

	@SuppressWarnings("unchecked")
	public static <E extends Enum<?>> Class<E> getEnumClass(String name) {
		if(SCALAR_TYPE_NAME.equals(name)) {
			return (Class<E>) ScalarType.class;
		} else if(STATE_NAME.equals(name)) {
			return (Class<E>) VersionState.class;
		} else if(INTERPOLATION_NAME.equals(name)) {
			return (Class<E>) Interpolation.class;
		} else if(AXIS_TYPE_NAME.equals(name)) {
			return (Class<E>) AxisType.class;
		} else if(TYPE_SPECIFICATION_NAME.equals(name)) {
			return (Class<E>) TypeSpecification.class;
		} else if(SEQUENCE_REPRESENTATION_NAME.equals(name)) {
			return (Class<E>) SequenceRepresentation.class;
		}

		throw new IllegalStateException("Enumeration mapping for name '" + name + "' does not exist.");
	}

	public static String getEnumName(Class<? extends Enum<?>> enumClass) {
		if(enumClass == null) {
			throw new IllegalArgumentException("Enumeration class is not allowed to be null.");
		} else if(ScalarType.class == enumClass) {
			return SCALAR_TYPE_NAME;
		} else if(VersionState.class == enumClass) {
			return STATE_NAME;
		} else if(Interpolation.class == enumClass) {
			return INTERPOLATION_NAME;
		} else if(AxisType.class == enumClass) {
			return AXIS_TYPE_NAME;
		} else if(TypeSpecification.class == enumClass) {
			return TYPE_SPECIFICATION_NAME;
		} else if(SequenceRepresentation.class == enumClass) {
			return SEQUENCE_REPRESENTATION_NAME;
		}

		throw new IllegalStateException("Enumeration mapping for enumeration class '" +
				enumClass.getSimpleName() + "' does not exist.");
	}

	@SuppressWarnings("unchecked")
	static <E extends Enum<?>> E fromODSEnum(Class<E> enumClass, int value)
			throws DataAccessException {
		if(enumClass == null) {
			throw new IllegalArgumentException("Enumeration class is not allowed to be null.");
		} else if(ScalarType.class == enumClass) {
			return (E) fromODSScalarType(value);
		} else if(VersionState.class == enumClass) {
			return (E) fromODSEnumByOrdinal(VersionState.class, value);
		} else if(Interpolation.class == enumClass) {
			return (E) fromODSEnumByOrdinal(Interpolation.class, value);
		} else if(AxisType.class == enumClass) {
			return (E) fromODSEnumByOrdinal(AxisType.class, value);
		} else if(TypeSpecification.class == enumClass) {
			return (E) fromODSEnumByOrdinal(TypeSpecification.class, value);
		} else if(SequenceRepresentation.class == enumClass) {
			return (E) fromODSSequenceRepresentation(value);
		}

		throw new IllegalStateException("Enumeration mapping for type '" + enumClass.getSimpleName() + "' does not exist.");
	}

	@SuppressWarnings("unchecked")
	static <E extends Enum<?>> E[] fromODSEnumSeq(Class<E> enumClass,
			int[] values) throws DataAccessException {
		if(enumClass == null) {
			throw new IllegalArgumentException("Enumeration class is not allowed to be null.");
		} else if(ScalarType.class == enumClass) {
			List<E> scalarTypes = new ArrayList<>(values.length);
			for(int value : values) {
				scalarTypes.add((E) fromODSScalarType(value));
			}
			return (E[]) scalarTypes.toArray(new ScalarType[values.length]);
		} else if(VersionState.class == enumClass) {
			return (E[]) fromODSEnumSeqByOrdinal(VersionState.class, values);
		} else if(Interpolation.class == enumClass) {
			return (E[]) fromODSEnumSeqByOrdinal(Interpolation.class, values);
		} else if(AxisType.class == enumClass) {
			return (E[]) fromODSEnumSeqByOrdinal(AxisType.class, values);
		} else if(TypeSpecification.class == enumClass) {
			return (E[]) fromODSEnumSeqByOrdinal(TypeSpecification.class, values);
		} else if(SequenceRepresentation.class == enumClass) {
			List<E> sequenceRepresentations = new ArrayList<>(values.length);
			for(int value : values) {
				sequenceRepresentations.add((E) fromODSSequenceRepresentation(value));
			}
			return (E[]) sequenceRepresentations.toArray(new SequenceRepresentation[values.length]);
		}

		throw new IllegalStateException("Enumeration mapping for type '" + enumClass.getSimpleName() + "' does not exist.");
	}

	static <E extends Enum<?>> int toODSEnum(E constant) throws DataAccessException {
		if(constant == null) {
			return 0;
		} else if(constant instanceof ScalarType) {
			return toODSScalarType((ScalarType) constant);
		} else if(constant instanceof VersionState || constant instanceof Interpolation
				|| constant instanceof AxisType || constant instanceof TypeSpecification) {
			//NOTE: Ordinal numbers map directly to the corresponding ODS enumeration constant value.
			return ((Enum<?>) constant).ordinal();
		} else if(constant instanceof SequenceRepresentation) {
			return toODSSequenceRepresentation((SequenceRepresentation) constant);
		}

		throw new IllegalStateException("Enumeration mapping for type '"
				+ constant.getClass().getSimpleName() + "' does not exist.");
	}

	static <E extends Enum<?>> int[] toODSEnumSeq(E[] constants)
			throws DataAccessException {
		if(constants == null) {
			return new int[0];
		}

		int[] values = new int[constants.length];
		if(constants instanceof ScalarType[]) {
			for(int i = 0; i < values.length; i++) {
				values[i] = toODSScalarType((ScalarType) constants[i]);
			}
		} else if(constants instanceof VersionState[] || constants instanceof Interpolation[]
				|| constants instanceof AxisType[] || constants instanceof TypeSpecification[]) {
			for(int i = 0; i < values.length; i++) {
				//NOTE: Ordinal numbers directly map to the corresponding ODS enumeration constant value.
				values[i] = ((Enum<?>) constants[i]).ordinal();
			}
		} else if(constants instanceof SequenceRepresentation[]) {
			for(int i = 0; i < values.length; i++) {
				values[i] = toODSSequenceRepresentation((SequenceRepresentation) constants[i]);
			}
		} else {
			throw new IllegalStateException("Enumeration mapping for type '"
					+ constants.getClass().getComponentType().getSimpleName() + "' does not exist.");
		}

		return values;
	}

	private static ScalarType fromODSScalarType(int value) throws DataAccessException {
		if(value == 0) {
			return ScalarType.UNKNOWN;
		} else if(value == 1) {
			return ScalarType.STRING;
		} else if(value == 2) {
			return ScalarType.SHORT;
		} else if(value == 3) {
			return ScalarType.FLOAT;
		} else if(value == 4) {
			return ScalarType.BOOLEAN;
		} else if(value == 5) {
			return ScalarType.BYTE;
		} else if(value == 6) {
			return ScalarType.INTEGER;
		} else if(value == 7) {
			return ScalarType.DOUBLE;
		} else if(value == 8) {
			return ScalarType.LONG;
		} else if(value == 10) {
			return ScalarType.DATE;
		} else if(value == 11) {
			return ScalarType.BYTE_STREAM;
		} else if(value == 12) {
			return ScalarType.BLOB;
		} else if(value == 13) {
			return ScalarType.FLOAT_COMPLEX;
		} else if(value == 14) {
			return ScalarType.DOUBLE_COMPLEX;
		} else if(value == 28) {
			return ScalarType.FILE_LINK;
		} else if(value == 30) {
			return ScalarType.ENUMERATION;
		}

		throw new DataAccessException("Unable to map ODS enumeration vaue '" + value + "' to constant of type '"
				+ ScalarType.class.getSimpleName() + "'.");
	}

	private static int toODSScalarType(ScalarType scalarType) throws DataAccessException {
		if(ScalarType.UNKNOWN == scalarType) {
			return 0;
		} else if(ScalarType.STRING == scalarType) {
			return 1;
		} else if(ScalarType.SHORT == scalarType) {
			return 2;
		} else if(ScalarType.FLOAT == scalarType) {
			return 3;
		} else if(ScalarType.BOOLEAN == scalarType) {
			return 4;
		} else if(ScalarType.BYTE == scalarType) {
			return 5;
		} else if(ScalarType.INTEGER == scalarType) {
			return 6;
		} else if(ScalarType.DOUBLE == scalarType) {
			return 7;
		} else if(ScalarType.LONG == scalarType) {
			return 8;
		} else if(ScalarType.DATE == scalarType) {
			return 10;
		} else if(ScalarType.BYTE_STREAM == scalarType) {
			return 11;
		} else if(ScalarType.BLOB == scalarType) {
			return 12;
		} else if(ScalarType.FLOAT_COMPLEX == scalarType) {
			return 13;
		} else if(ScalarType.DOUBLE_COMPLEX == scalarType) {
			return 14;
		} else if(ScalarType.FILE_LINK == scalarType) {
			return 28;
		} else if(ScalarType.ENUMERATION == scalarType) {
			return 30;
		}

		throw new DataAccessException("Unable to map enumeration constant '" + scalarType
				+ "' of type '" + ScalarType.class.getSimpleName() + "' to ODS enumeration value.");
	}

	private static SequenceRepresentation fromODSSequenceRepresentation(int value) throws DataAccessException {
		if(value == 0) {
			return SequenceRepresentation.EXPLICIT;
		} else if(value == 1) {
			return SequenceRepresentation.IMPLICIT_CONSTANT;
		} else if(value == 2) {
			return SequenceRepresentation.IMPLICIT_LINEAR;
		} else if(value == 3) {
			return SequenceRepresentation.IMPLICIT_SAW;
		} else if(value == 4) {
			return SequenceRepresentation.RAW_LINEAR;
		} else if(value == 5) {
			return SequenceRepresentation.RAW_POLYNOMIAL;
		} else if(value == 7) {
			return SequenceRepresentation.EXPLICIT_EXTERNAL;
		} else if(value == 8) {
			return SequenceRepresentation.RAW_LINEAR_EXTERNAL;
		} else if(value == 9) {
			return SequenceRepresentation.RAW_POLYNOMIAL_EXTERNAL;
		} else if(value == 10) {
			return SequenceRepresentation.RAW_LINEAR_CALIBRATED;
		} else if(value == 11) {
			return SequenceRepresentation.RAW_LINEAR_CALIBRATED_EXTERNAL;
		}

		throw new DataAccessException("Unable to map ODS enumeration vaue '" + value + "' to constant of type '"
				+ SequenceRepresentation.class.getSimpleName() + "'.");
	}

	private static int toODSSequenceRepresentation(SequenceRepresentation sequenceRepresentation)
			throws DataAccessException {
		if(SequenceRepresentation.EXPLICIT == sequenceRepresentation) {
			return 0;
		} else if(SequenceRepresentation.IMPLICIT_CONSTANT == sequenceRepresentation) {
			return 1;
		} else if(SequenceRepresentation.IMPLICIT_LINEAR == sequenceRepresentation) {
			return 2;
		} else if(SequenceRepresentation.IMPLICIT_SAW == sequenceRepresentation) {
			return 3;
		} else if(SequenceRepresentation.RAW_LINEAR == sequenceRepresentation) {
			return 4;
		} else if(SequenceRepresentation.RAW_POLYNOMIAL == sequenceRepresentation) {
			return 5;
		} else if(SequenceRepresentation.EXPLICIT_EXTERNAL == sequenceRepresentation) {
			return 7;
		} else if(SequenceRepresentation.RAW_LINEAR_EXTERNAL == sequenceRepresentation) {
			return 8;
		} else if(SequenceRepresentation.RAW_POLYNOMIAL_EXTERNAL == sequenceRepresentation) {
			return 9;
		} else if(SequenceRepresentation.RAW_LINEAR_CALIBRATED == sequenceRepresentation) {
			return 10;
		} else if(SequenceRepresentation.RAW_LINEAR_CALIBRATED_EXTERNAL == sequenceRepresentation) {
			return 11;
		}

		throw new DataAccessException("Unable to map enumeration constant '" + sequenceRepresentation
				+ "' of type '" + SequenceRepresentation.class.getSimpleName() + "' to ODS enumeration value.");
	}

	private static <E extends Enum<?>> E fromODSEnumByOrdinal(Class<E> enumClass,
			int value) throws DataAccessException {
		E[] constants = enumClass.getEnumConstants();
		if(value < 0 || value > constants.length) {
			throw new DataAccessException("Unable to map ODS enumeration vaue '" + value + "' to constant of type '"
					+ enumClass.getSimpleName() + "'.");
		}

		//NOTE: Ordinal numbers directly map to the corresponding ODS enumeration constant value.
		return constants[value];
	}

	@SuppressWarnings("unchecked")
	private static <E extends Enum<?>> E[] fromODSEnumSeqByOrdinal(Class<E> enumClass,
			int[] values) throws DataAccessException {
		List<E> enumValues = new ArrayList<>(values.length);
		E[] constants = enumClass.getEnumConstants();

		for(int value : values) {
			if(value < 0 || value > constants.length) {
				throw new DataAccessException("Unable to map ODS enumeration vaue '" + value + "' to constant of type '"
						+ enumClass.getSimpleName() + "'.");
			}

			//NOTE: Ordinal numbers directly map to the corresponding ODS enumeration constant value.
			enumValues.add(constants[value]);
		}

		return enumValues.toArray((E[]) Array.newInstance(enumClass, values.length));
	}

}
