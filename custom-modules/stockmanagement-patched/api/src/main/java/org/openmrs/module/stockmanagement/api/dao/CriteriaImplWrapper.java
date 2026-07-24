/*
 * The contents of this file are subject to the OpenMRS Public License
 * Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See
 * the License for the specific language governing rights and
 * limitations under the License.
 *
 * Copyright (C) OpenHMIS.  All Rights Reserved.
 */
package org.openmrs.module.stockmanagement.api.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Version;
import org.hibernate.criterion.Projection;
import org.hibernate.transform.ResultTransformer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Wrapper class to handle differences between Hibernate 3 and 4 for the CriteriaImpl class.
 */
public class CriteriaImplWrapper {
	
	public static final long serialVersionUID = 0L;
	
	private static final Log LOG = LogFactory.getLog(CriteriaImplWrapper.class);
	
	private static final String VERSION_3_CRITERIA_CLASS = "org.hibernate.impl.CriteriaImpl";
	
	private static final String VERSION_4_CRITERIA_CLASS = "org.hibernate.internal.CriteriaImpl";
	
	private static final String PROJECTION_METHOD_NAME = "getProjection";
	
	private static final String RESULT_TRANSFORMER_METHOD_NAME = "getResultTransformer";
	
	private static Method getProjectionMethod;
	
	private static Method getResultTransformerMethod;
	
	private Object impl;
	
	static {
		Class<?> cls;
		
		try {
			LOG.debug("Hibernate version: " + Version.getVersionString());
			if (org.hibernate.Version.getVersionString().startsWith("3.")) {
				cls = Class.forName(VERSION_3_CRITERIA_CLASS);
			} else {
				cls = Class.forName(VERSION_4_CRITERIA_CLASS);
			}
			
			getProjectionMethod = cls.getMethod(PROJECTION_METHOD_NAME);
			getResultTransformerMethod = cls.getMethod(RESULT_TRANSFORMER_METHOD_NAME);
		}
		catch (ClassNotFoundException ex) {
			cls = null;
			getProjectionMethod = null;
			getResultTransformerMethod = null;
			
			LOG.error("Could not determine hibernate version.", ex);
		}
		catch (NoSuchMethodException e) {
			cls = null;
			getProjectionMethod = null;
			getResultTransformerMethod = null;
			
			LOG.error("Could not get CriteriaImpl method.", e);
		}
	}
	
	public CriteriaImplWrapper(Criteria criteria) {
		impl = criteria;
	}
	
	public Projection getProjection() {
		if (getProjectionMethod != null) {
			try {
				return (Projection) getProjectionMethod.invoke(impl);
			}
			catch (IllegalAccessException e) {
				LOG.error("Could not get CriteriaImpl Projection", e);
			}
			catch (InvocationTargetException e) {
				LOG.error("Could not get CriteriaImpl Projection", e);
			}
			
		}
		
		return null;
	}
	
	public ResultTransformer getResultTransformer() {
		if (getResultTransformerMethod != null) {
			try {
				return (ResultTransformer) getResultTransformerMethod.invoke(impl);
			}
			catch (IllegalAccessException e) {
				LOG.error("Could not get CriteriaImpl ResultTransformer", e);
			}
			catch (InvocationTargetException e) {
				LOG.error("Could not get CriteriaImpl ResultTransformer", e);
			}
		}
		
		return null;
	}
}
