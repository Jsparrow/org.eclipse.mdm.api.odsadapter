/*
 * Copyright (c) 2015 OpenMDM(r) Working Group
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */



import java.util.ArrayList;
import java.util.List;

import org.eclipse.mdm.api.base.BaseDataProvider;
import org.eclipse.mdm.api.base.DataProviderException;
import org.eclipse.mdm.api.base.model.Channel;
import org.eclipse.mdm.api.base.model.ChannelGroup;
import org.eclipse.mdm.api.base.model.ContextRoot;
import org.eclipse.mdm.api.base.model.Measurement;
import org.eclipse.mdm.api.base.model.Test;
import org.eclipse.mdm.api.base.model.TestStep;
import org.eclipse.mdm.api.base.model.Value;
import org.eclipse.mdm.api.base.model.ValueType;
import org.eclipse.mdm.api.base.query.DataAccessException;
import org.eclipse.mdm.api.odsadapter.ODSDataProviderFactory;

public class Main {

	private final static String ARG_ODS_NAMESERVICE = "ods_nameservice";
	private final static String ARG_ODS_SERVICENAME = "ods_servicename";
	private final static String ARG_ODS_USER 		= "ods_user";
	private final static String ARG_ODS_PASSWORD    = "ods_password";
	
	public static void main(String[] args) throws DataProviderException, DataAccessException {
		
		List<Value> parameters = new ArrayList<Value>();
		/*
		 * TODO: Replace with connection details of your MDM system
		 */
		parameters.add(ValueType.STRING.newValue(ARG_ODS_NAMESERVICE, "corbaloc::1.2@in-dbserv1:2809/NameService"));
		parameters.add(ValueType.STRING.newValue(ARG_ODS_SERVICENAME, "MDMTEST01.ASAM-ODS"));
		parameters.add(ValueType.STRING.newValue(ARG_ODS_USER, "sa"));
		parameters.add(ValueType.STRING.newValue(ARG_ODS_PASSWORD, "sa"));
		
		ODSDataProviderFactory baseModelManagerFactory = new ODSDataProviderFactory();
		BaseDataProvider baseDataProvider = baseModelManagerFactory.connect(parameters);
		
		Test test = baseDataProvider.findByID(Test.class, 4439l); // TODO: replace with a valid Test.Id in your MDM system
		List<TestStep> testSteps = baseDataProvider.listChildren(test, TestStep.class);
		for(TestStep testStep : testSteps) {
			System.out.println(testStep);		
			List<Measurement> meaResults = baseDataProvider.listChildren(testStep, Measurement.class);
			for(Measurement meaResult : meaResults) {
				System.out.println("\t" + meaResult);
				
				List<Channel> channels = baseDataProvider.listChildren(meaResult, Channel.class);
				for(Channel channel : channels) {
					System.out.println("\t\t" + channel);
					System.out.println("\t\t\t" + channel.getChannelDataInfo());
					System.out.println("\t\t\t" + channel.getUnit());
					System.out.println("\t\t\t" + channel.getQuantity());
					System.out.println("\t\t\t\t" + channel.getQuantity().getDefaultUnit());
				}
				
				
				List<ChannelGroup> channelGroups = baseDataProvider.listChildren(meaResult, Measurement.CHILD_TYPE_CHANNELGROUP);
				for (ChannelGroup channelGroup : channelGroups) {
					System.out.println(channelGroup);
					
					List<Channel> channels2 = baseDataProvider.listChildren(channelGroup, ChannelGroup.CHILD_TYPE_CHANNEL);
					for (Channel channel : channels2) {
						System.out.println("\t\t" + channel);
						System.out.println("\t\t\t" + channel.getChannelDataInfo());
						System.out.println("\t\t\t" + channel.getUnit());
						System.out.println("\t\t\t" + channel.getQuantity());
						System.out.println("\t\t\t\t" + channel.getQuantity().getDefaultUnit());
					}
				}
			}
		}
		
		TestStep testStep = baseDataProvider.findByID(TestStep.class, 30389l); // TODO: replace with a valid TestStep.Id in your MDM system
		for(ContextRoot contextRoot : baseDataProvider.getContextData(testStep).values()) {
			System.out.println(contextRoot);
		}
		
		System.out.println(baseDataProvider.getEnvironment());		
		System.out.println(baseDataProvider.getLoggedOnUser());
		
		createCustomQuery(baseDataProvider);

		
		baseModelManagerFactory.disconnect(baseDataProvider);
	}
	
	public static void createCustomQuery(BaseDataProvider mdmDataProvider) throws DataAccessException {
		
		
//		QueryService qs = mdmDataProvider.getQueryService();
//		
//		Entity testEntity = qs.getEntity(Test.class);
//		Entity testStepEntity = qs.getEntity(TestStep.class);
//		Entity measurementEntity = qs.getEntity(Measurement.class);
//		
//		Query query = qs.createQuery();
//		query.select(testEntity, Test.ATTR_NAME, Test.ATTR_ID);
//		query.selectAll(testStepEntity);
//		query.select(measurementEntity, Measurement.ATTR_NAME, Measurement.ATTR_ID);
//		query.selectID(qs.getEntity(Measurement.class));
//		
//		query.join(qs.getRelation(testEntity, testStepEntity));
//		query.join(qs.getRelation(testStepEntity, measurementEntity));
//		
//		ConditionJoiner joiner = ConditionJoiner.andJoiner();
//		joiner.add(Operation.NEQ.create(measurementEntity.getIDAttribute(), 498426L));
//		joiner.add(Operation.EQ.create(testEntity.getIDAttribute(), 4418L));
//		
//		List<Result> results = query.fetch(joiner.getFilter());
//		for(Result result : results) {
//			for(Record record : result) {
//				System.out.println(record.getEntity() + " " + record.getValues().values());
//			}
//		}
		
	}

}
