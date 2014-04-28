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

import com.liferay.portal.service.ServiceWrapper;

/**
 * <p>
 * This class is a wrapper for {@link LinkCheckerURLStatusService}.
 * </p>
 *
 * @author    Craig Vershaw
 * @see       LinkCheckerURLStatusService
 * @generated
 */
public class LinkCheckerURLStatusServiceWrapper
	implements LinkCheckerURLStatusService,
		ServiceWrapper<LinkCheckerURLStatusService> {
	public LinkCheckerURLStatusServiceWrapper(
		LinkCheckerURLStatusService linkCheckerURLStatusService) {
		_linkCheckerURLStatusService = linkCheckerURLStatusService;
	}

	/**
	* Returns the Spring bean ID for this bean.
	*
	* @return the Spring bean ID for this bean
	*/
	public java.lang.String getBeanIdentifier() {
		return _linkCheckerURLStatusService.getBeanIdentifier();
	}

	/**
	* Sets the Spring bean ID for this bean.
	*
	* @param beanIdentifier the Spring bean ID for this bean
	*/
	public void setBeanIdentifier(java.lang.String beanIdentifier) {
		_linkCheckerURLStatusService.setBeanIdentifier(beanIdentifier);
	}

	public java.lang.Object invokeMethod(java.lang.String name,
		java.lang.String[] parameterTypes, java.lang.Object[] arguments)
		throws java.lang.Throwable {
		return _linkCheckerURLStatusService.invokeMethod(name, parameterTypes,
			arguments);
	}

	public java.lang.String[] getResponse(java.lang.String url)
		throws com.liferay.portal.kernel.exception.PortalException,
			com.liferay.portal.kernel.exception.SystemException {
		return _linkCheckerURLStatusService.getResponse(url);
	}

	public java.lang.String getResponseCode(java.lang.String url)
		throws com.liferay.portal.kernel.exception.PortalException,
			com.liferay.portal.kernel.exception.SystemException {
		return _linkCheckerURLStatusService.getResponseCode(url);
	}

	public java.lang.String getResponseString(java.lang.String url)
		throws com.liferay.portal.kernel.exception.PortalException,
			com.liferay.portal.kernel.exception.SystemException {
		return _linkCheckerURLStatusService.getResponseString(url);
	}

	/**
	 * @deprecated Renamed to {@link #getWrappedService}
	 */
	public LinkCheckerURLStatusService getWrappedLinkCheckerURLStatusService() {
		return _linkCheckerURLStatusService;
	}

	/**
	 * @deprecated Renamed to {@link #setWrappedService}
	 */
	public void setWrappedLinkCheckerURLStatusService(
		LinkCheckerURLStatusService linkCheckerURLStatusService) {
		_linkCheckerURLStatusService = linkCheckerURLStatusService;
	}

	public LinkCheckerURLStatusService getWrappedService() {
		return _linkCheckerURLStatusService;
	}

	public void setWrappedService(
		LinkCheckerURLStatusService linkCheckerURLStatusService) {
		_linkCheckerURLStatusService = linkCheckerURLStatusService;
	}

	private LinkCheckerURLStatusService _linkCheckerURLStatusService;
}