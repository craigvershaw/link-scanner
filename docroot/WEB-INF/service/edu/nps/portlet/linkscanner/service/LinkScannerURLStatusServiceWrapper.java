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

package edu.nps.portlet.linkscanner.service;

import com.liferay.portal.service.ServiceWrapper;

/**
 * <p>
 * This class is a wrapper for {@link LinkScannerURLStatusService}.
 * </p>
 *
 * @author    Craig Vershaw
 * @see       LinkScannerURLStatusService
 * @generated
 */
public class LinkScannerURLStatusServiceWrapper
	implements LinkScannerURLStatusService,
		ServiceWrapper<LinkScannerURLStatusService> {
	public LinkScannerURLStatusServiceWrapper(
		LinkScannerURLStatusService linkScannerURLStatusService) {
		_linkScannerURLStatusService = linkScannerURLStatusService;
	}

	/**
	* Returns the Spring bean ID for this bean.
	*
	* @return the Spring bean ID for this bean
	*/
	public java.lang.String getBeanIdentifier() {
		return _linkScannerURLStatusService.getBeanIdentifier();
	}

	/**
	* Sets the Spring bean ID for this bean.
	*
	* @param beanIdentifier the Spring bean ID for this bean
	*/
	public void setBeanIdentifier(java.lang.String beanIdentifier) {
		_linkScannerURLStatusService.setBeanIdentifier(beanIdentifier);
	}

	public java.lang.Object invokeMethod(java.lang.String name,
		java.lang.String[] parameterTypes, java.lang.Object[] arguments)
		throws java.lang.Throwable {
		return _linkScannerURLStatusService.invokeMethod(name, parameterTypes,
			arguments);
	}

	public java.lang.String[] getResponse(java.lang.String url)
		throws com.liferay.portal.kernel.exception.PortalException,
			com.liferay.portal.kernel.exception.SystemException {
		return _linkScannerURLStatusService.getResponse(url);
	}

	public java.lang.String[] getResponse(java.lang.String url,
		java.lang.String userAgent)
		throws com.liferay.portal.kernel.exception.PortalException,
			com.liferay.portal.kernel.exception.SystemException {
		return _linkScannerURLStatusService.getResponse(url, userAgent);
	}

	public java.lang.String getResponseCode(java.lang.String url)
		throws com.liferay.portal.kernel.exception.PortalException,
			com.liferay.portal.kernel.exception.SystemException {
		return _linkScannerURLStatusService.getResponseCode(url);
	}

	public java.lang.String getResponseCode(java.lang.String url,
		java.lang.String userAgent)
		throws com.liferay.portal.kernel.exception.PortalException,
			com.liferay.portal.kernel.exception.SystemException {
		return _linkScannerURLStatusService.getResponseCode(url, userAgent);
	}

	public java.lang.String getResponseString(java.lang.String url)
		throws com.liferay.portal.kernel.exception.PortalException,
			com.liferay.portal.kernel.exception.SystemException {
		return _linkScannerURLStatusService.getResponseString(url);
	}

	public java.lang.String getResponseString(java.lang.String url,
		java.lang.String userAgent)
		throws com.liferay.portal.kernel.exception.PortalException,
			com.liferay.portal.kernel.exception.SystemException {
		return _linkScannerURLStatusService.getResponseString(url, userAgent);
	}

	/**
	 * @deprecated Renamed to {@link #getWrappedService}
	 */
	public LinkScannerURLStatusService getWrappedLinkScannerURLStatusService() {
		return _linkScannerURLStatusService;
	}

	/**
	 * @deprecated Renamed to {@link #setWrappedService}
	 */
	public void setWrappedLinkScannerURLStatusService(
		LinkScannerURLStatusService linkScannerURLStatusService) {
		_linkScannerURLStatusService = linkScannerURLStatusService;
	}

	public LinkScannerURLStatusService getWrappedService() {
		return _linkScannerURLStatusService;
	}

	public void setWrappedService(
		LinkScannerURLStatusService linkScannerURLStatusService) {
		_linkScannerURLStatusService = linkScannerURLStatusService;
	}

	private LinkScannerURLStatusService _linkScannerURLStatusService;
}