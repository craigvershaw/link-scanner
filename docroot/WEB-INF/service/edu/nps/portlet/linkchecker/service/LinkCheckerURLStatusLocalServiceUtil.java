/**
 * Copyright (c) 2000-2012 Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package edu.nps.portlet.linkchecker.service;

import com.liferay.portal.kernel.bean.PortletBeanLocatorUtil;
import com.liferay.portal.kernel.util.ReferenceRegistry;
import com.liferay.portal.service.InvokableLocalService;

/**
 * The utility for the link checker u r l status local service. This utility wraps {@link edu.nps.portlet.linkchecker.service.impl.LinkCheckerURLStatusLocalServiceImpl} and is the primary access point for service operations in application layer code running on the local server.
 *
 * <p>
 * This is a local service. Methods of this service will not have security checks based on the propagated JAAS credentials because this service can only be accessed from within the same VM.
 * </p>
 *
 * @author Craig Vershaw
 * @see LinkCheckerURLStatusLocalService
 * @see edu.nps.portlet.linkchecker.service.base.LinkCheckerURLStatusLocalServiceBaseImpl
 * @see edu.nps.portlet.linkchecker.service.impl.LinkCheckerURLStatusLocalServiceImpl
 * @generated
 */
public class LinkCheckerURLStatusLocalServiceUtil {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to {@link edu.nps.portlet.linkchecker.service.impl.LinkCheckerURLStatusLocalServiceImpl} and rerun ServiceBuilder to regenerate this class.
	 */

	/**
	* Returns the Spring bean ID for this bean.
	*
	* @return the Spring bean ID for this bean
	*/
	public static java.lang.String getBeanIdentifier() {
		return getService().getBeanIdentifier();
	}

	/**
	* Sets the Spring bean ID for this bean.
	*
	* @param beanIdentifier the Spring bean ID for this bean
	*/
	public static void setBeanIdentifier(java.lang.String beanIdentifier) {
		getService().setBeanIdentifier(beanIdentifier);
	}

	public static java.lang.Object invokeMethod(java.lang.String name,
		java.lang.String[] parameterTypes, java.lang.Object[] arguments)
		throws java.lang.Throwable {
		return getService().invokeMethod(name, parameterTypes, arguments);
	}

	public static java.lang.String[] getResponse(java.lang.String url)
		throws com.liferay.portal.kernel.exception.PortalException,
			com.liferay.portal.kernel.exception.SystemException {
		return getService().getResponse(url);
	}

	public static java.lang.String getResponseCode(java.lang.String url)
		throws com.liferay.portal.kernel.exception.PortalException,
			com.liferay.portal.kernel.exception.SystemException {
		return getService().getResponseCode(url);
	}

	public static java.lang.String getResponseString(java.lang.String url)
		throws com.liferay.portal.kernel.exception.PortalException,
			com.liferay.portal.kernel.exception.SystemException {
		return getService().getResponseString(url);
	}

	public static void clearService() {
		_service = null;
	}

	public static LinkCheckerURLStatusLocalService getService() {
		if (_service == null) {
			InvokableLocalService invokableLocalService = (InvokableLocalService)PortletBeanLocatorUtil.locate(ClpSerializer.getServletContextName(),
					LinkCheckerURLStatusLocalService.class.getName());

			if (invokableLocalService instanceof LinkCheckerURLStatusLocalService) {
				_service = (LinkCheckerURLStatusLocalService)invokableLocalService;
			}
			else {
				_service = new LinkCheckerURLStatusLocalServiceClp(invokableLocalService);
			}

			ReferenceRegistry.registerReference(LinkCheckerURLStatusLocalServiceUtil.class,
				"_service");
		}

		return _service;
	}

	/**
	 * @deprecated
	 */
	public void setService(LinkCheckerURLStatusLocalService service) {
	}

	private static LinkCheckerURLStatusLocalService _service;
}