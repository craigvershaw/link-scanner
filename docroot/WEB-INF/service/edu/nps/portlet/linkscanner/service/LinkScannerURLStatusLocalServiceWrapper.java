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
 * This class is a wrapper for {@link LinkScannerURLStatusLocalService}.
 * </p>
 *
 * @author    Craig Vershaw
 * @see       LinkScannerURLStatusLocalService
 * @generated
 */
public class LinkScannerURLStatusLocalServiceWrapper
	implements LinkScannerURLStatusLocalService,
		ServiceWrapper<LinkScannerURLStatusLocalService> {
	public LinkScannerURLStatusLocalServiceWrapper(
		LinkScannerURLStatusLocalService linkScannerURLStatusLocalService) {
		_linkScannerURLStatusLocalService = linkScannerURLStatusLocalService;
	}

	/**
	* Returns the Spring bean ID for this bean.
	*
	* @return the Spring bean ID for this bean
	*/
	public java.lang.String getBeanIdentifier() {
		return _linkScannerURLStatusLocalService.getBeanIdentifier();
	}

	/**
	* Sets the Spring bean ID for this bean.
	*
	* @param beanIdentifier the Spring bean ID for this bean
	*/
	public void setBeanIdentifier(java.lang.String beanIdentifier) {
		_linkScannerURLStatusLocalService.setBeanIdentifier(beanIdentifier);
	}

	public java.lang.Object invokeMethod(java.lang.String name,
		java.lang.String[] parameterTypes, java.lang.Object[] arguments)
		throws java.lang.Throwable {
		return _linkScannerURLStatusLocalService.invokeMethod(name,
			parameterTypes, arguments);
	}

	public java.lang.String[] getResponse(java.lang.String url)
		throws com.liferay.portal.kernel.exception.PortalException,
			com.liferay.portal.kernel.exception.SystemException {
		return _linkScannerURLStatusLocalService.getResponse(url);
	}

	public java.lang.String[] getResponse(java.lang.String url,
		java.lang.String userAgent)
		throws com.liferay.portal.kernel.exception.PortalException,
			com.liferay.portal.kernel.exception.SystemException {
		return _linkScannerURLStatusLocalService.getResponse(url, userAgent);
	}

	public java.lang.String getResponseCode(java.lang.String url)
		throws com.liferay.portal.kernel.exception.PortalException,
			com.liferay.portal.kernel.exception.SystemException {
		return _linkScannerURLStatusLocalService.getResponseCode(url);
	}

	public java.lang.String getResponseCode(java.lang.String url,
		java.lang.String userAgent)
		throws com.liferay.portal.kernel.exception.PortalException,
			com.liferay.portal.kernel.exception.SystemException {
		return _linkScannerURLStatusLocalService.getResponseCode(url, userAgent);
	}

	public java.lang.String getResponseString(java.lang.String url)
		throws com.liferay.portal.kernel.exception.PortalException,
			com.liferay.portal.kernel.exception.SystemException {
		return _linkScannerURLStatusLocalService.getResponseString(url);
	}

	public java.lang.String getResponseString(java.lang.String url,
		java.lang.String userAgent)
		throws com.liferay.portal.kernel.exception.PortalException,
			com.liferay.portal.kernel.exception.SystemException {
		return _linkScannerURLStatusLocalService.getResponseString(url,
			userAgent);
	}

	/**
	 * @deprecated Renamed to {@link #getWrappedService}
	 */
	public LinkScannerURLStatusLocalService getWrappedLinkScannerURLStatusLocalService() {
		return _linkScannerURLStatusLocalService;
	}

	/**
	 * @deprecated Renamed to {@link #setWrappedService}
	 */
	public void setWrappedLinkScannerURLStatusLocalService(
		LinkScannerURLStatusLocalService linkScannerURLStatusLocalService) {
		_linkScannerURLStatusLocalService = linkScannerURLStatusLocalService;
	}

	public LinkScannerURLStatusLocalService getWrappedService() {
		return _linkScannerURLStatusLocalService;
	}

	public void setWrappedService(
		LinkScannerURLStatusLocalService linkScannerURLStatusLocalService) {
		_linkScannerURLStatusLocalService = linkScannerURLStatusLocalService;
	}

	private LinkScannerURLStatusLocalService _linkScannerURLStatusLocalService;
}