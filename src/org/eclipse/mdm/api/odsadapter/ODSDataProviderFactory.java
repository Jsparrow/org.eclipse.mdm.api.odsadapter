/*
 * Copyright (c) 2015 OpenMDM(r) Working Group
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.eclipse.mdm.api.odsadapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.asam.ods.AoException;
import org.asam.ods.AoFactory;
import org.asam.ods.AoFactoryHelper;
import org.asam.ods.AoSession;
import org.eclipse.mdm.api.base.BaseDataProvider;
import org.eclipse.mdm.api.base.DataProviderException;
import org.eclipse.mdm.api.base.DataProviderFactory;
import org.eclipse.mdm.api.base.model.Value;
import org.eclipse.mdm.api.base.query.DataAccessException;
import org.eclipse.mdm.api.odsadapter.odscache.ODSCache;
import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.InvalidName;
import org.omg.CosNaming.NamingContextPackage.NotFound;

public class ODSDataProviderFactory implements DataProviderFactory<BaseDataProvider> {

	private final static String ARG_ODS_NAMESERVICE = "ods_nameservice";
	private final static String ARG_ODS_SERVICENAME = "ods_servicename";
	private final static String ARG_ODS_USER 		= "ods_user";
	private final static String ARG_ODS_PASSWORD    = "ods_password";
	
	private ORB orb = null;
	
	@Override
	public BaseDataProvider connect(List<Value> parameters) throws DataProviderException {
		try {
			String odsNameService = getConnectionParameter(parameters, ARG_ODS_NAMESERVICE);
			String odsServiceName = getConnectionParameter(parameters, ARG_ODS_SERVICENAME);
			String odsUser = getConnectionParameter(parameters, ARG_ODS_USER);
			String odsPassword = getConnectionParameter(parameters, ARG_ODS_PASSWORD);
		
			AoSession aoSession = connect2ODS(odsNameService, odsServiceName, odsUser, odsPassword);
			
			return new ODSDataProvider(new ODSCache(aoSession));
			
		} catch(DataAccessException e) {
			throw new DataProviderException(e.getMessage(), e);
		}		
	}
	
	@Override
	public void disconnect(BaseDataProvider mdmDataProvider) throws DataProviderException {
		ODSDataProvider impl = (ODSDataProvider)mdmDataProvider;
		impl.close();		
	}
	
	private AoSession connect2ODS(String odsNameService, String odsServiceName, String odsUser, 
			String odsPassword) throws DataProviderException {
			try {
				NamingContextExt nameService = resolveNameService(odsNameService);
				AoFactory aoFactory = resolveAoFactorysService(odsServiceName, nameService);
				
				System.out.println("connecting to ODS Server ...");
				
				System.out.println("AoFactory name: " + aoFactory.getName());			
				System.out.println("AoFactory description: " + aoFactory.getDescription());
				System.out.println("AoFactory interface version: " + aoFactory.getInterfaceVersion());
				System.out.println("AoFactory type: " + aoFactory.getType());
				
				String connectionString = "USER=" + odsUser + ",PASSWORD=" + odsPassword  + ",CREATE_COSESSION_ALLOWED=TRUE";
				AoSession aoSession = aoFactory.newSession(connectionString);
				
				System.out.println("connecting to ODS Server ... done!");
				return aoSession;
			} catch(AoException aoe) {
				System.out.println(aoe.reason);		
				throw new DataProviderException(aoe.reason, aoe);
			}
	}
	
	private NamingContextExt resolveNameService(String nameServiceString) {	
		 org.omg.CORBA.Object nsObject = getORB().string_to_object(nameServiceString);
		 NamingContextExt nsReference = NamingContextExtHelper.narrow(nsObject);

		 return nsReference;
	}
	
	private AoFactory resolveAoFactorysService(String odsServiceName, NamingContextExt nameService) 
			throws DataProviderException {
		try {
			if(odsServiceName.contains("/")) {
				String[] ncsStrings = odsServiceName.split("/");
				List<NameComponent> ncList = new ArrayList<NameComponent>();
				for(String ncString : ncsStrings) {
					NameComponent nc = new NameComponent(ncString, "");
					ncList.add(nc);
				}
				NameComponent[] ncs = ncList.toArray(new NameComponent[ncList.size()]);
				org.omg.CORBA.Object o = nameService.resolve(ncs);
				return AoFactoryHelper.narrow(o);
			} 
			
			if(odsServiceName.toUpperCase().endsWith(".ASAM-ODS")) {
				String serviceName = odsServiceName.replace(".ASAM-ODS", "");
				NameComponent nc = new NameComponent(serviceName, "ASAM-ODS");	
				org.omg.CORBA.Object o = nameService.resolve(new NameComponent[]{nc});
				return AoFactoryHelper.narrow(o);
			}
			
			NameComponent nc = new NameComponent(odsServiceName, "");	
			org.omg.CORBA.Object o = nameService.resolve(new NameComponent[]{nc});

			return AoFactoryHelper.narrow(o);		
		} catch (NotFound e) {
			System.out.println(e.getMessage());
			throw new DataProviderException(e.getMessage(), e);
		} catch (CannotProceed e) {
			System.out.println(e.getMessage());
			throw new DataProviderException(e.getMessage(), e);
		} catch (InvalidName e) {
			System.out.println(e.getMessage());
			throw new DataProviderException(e.getMessage(), e);
		}		
	}
	
	private String getConnectionParameter(List<Value> parameters, String parameterName) throws DataProviderException {
		for(Value parameter : parameters) {
			if(parameter.getName().equalsIgnoreCase(parameterName)) {
				return parameter.getValue();
			}
		}
		throw new DataProviderException("mandatory connection parameter with name '" + parameterName + "' is missing");
	}
	
	private ORB getORB() {	
		if(this.orb == null) {
			Properties props = new Properties(System.getProperties());
//	        props.put("org.omg.CORBA.ORBClass", "org.jacorb.orb.ORB");
//	        props.put("org.omg.CORBA.ORBSingletonClass", "org.jacorb.orb.ORBSingleton");  
        
	        this.orb = ORB.init(new String[]{}, props);
		}
		
		return this.orb;
	}

}
