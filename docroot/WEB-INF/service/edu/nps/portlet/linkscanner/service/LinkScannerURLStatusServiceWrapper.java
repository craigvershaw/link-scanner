/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package edu.nps.portlet.linkscanner.service;

import com.liferay.portal.service.ServiceWrapper;

/**
 * Provides a wrapper for {@link LinkScannerURLStatusService}.
 *
 * @author Craig Vershaw
 * @see LinkScannerURLStatusService
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
	@Override
	public java.lang.String getBeanIdentifier() {
		return _linkScannerURLStatusService.getBeanIdentifier();
	}

	/**
	* Sets the Spring bean ID for this bean.
	*
	* @param beanIdentifier the Spring bean ID for this bean
	*/
	@Override
	public void setBeanIdentifier(java.lang.String beanIdentifier) {
		_linkScannerURLStatusService.setBeanIdentifier(beanIdentifier);
	}

	@Override
	public java.lang.Object invokeMethod(java.lang.String name,
		java.lang.String[] parameterTypes, java.lang.Object[] arguments)
		throws java.lang.Throwable {
		return _linkScannerURLStatusService.invokeMethod(name, parameterTypes,
			arguments);
	}

	@Override
	public java.lang.String[] getResponse(java.lang.String url)
		throws com.liferay.portal.kernel.exception.PortalException,
			com.liferay.portal.kernel.exception.SystemException {
		return _linkScannerURLStatusService.getResponse(url);
	}

	@Override
	public java.lang.String[] getResponse(java.lang.String url,
		java.lang.String userAgent)
		throws com.liferay.portal.kernel.exception.PortalException,
			com.liferay.portal.kernel.exception.SystemException {
		return _linkScannerURLStatusService.getResponse(url, userAgent);
	}

	@Override
	public java.lang.String getResponseCode(java.lang.String url)
		throws com.liferay.portal.kernel.exception.PortalException,
			com.liferay.portal.kernel.exception.SystemException {
		return _linkScannerURLStatusService.getResponseCode(url);
	}

	@Override
	public java.lang.String getResponseCode(java.lang.String url,
		java.lang.String userAgent)
		throws com.liferay.portal.kernel.exception.PortalException,
			com.liferay.portal.kernel.exception.SystemException {
		return _linkScannerURLStatusService.getResponseCode(url, userAgent);
	}

	@Override
	public java.lang.String getResponseString(java.lang.String url)
		throws com.liferay.portal.kernel.exception.PortalException,
			com.liferay.portal.kernel.exception.SystemException {
		return _linkScannerURLStatusService.getResponseString(url);
	}

	@Override
	public java.lang.String getResponseString(java.lang.String url,
		java.lang.String userAgent)
		throws com.liferay.portal.kernel.exception.PortalException,
			com.liferay.portal.kernel.exception.SystemException {
		return _linkScannerURLStatusService.getResponseString(url, userAgent);
	}

	/**
	 * @deprecated As of 6.1.0, replaced by {@link #getWrappedService}
	 */
	public LinkScannerURLStatusService getWrappedLinkScannerURLStatusService() {
		return _linkScannerURLStatusService;
	}

	/**
	 * @deprecated As of 6.1.0, replaced by {@link #setWrappedService}
	 */
	public void setWrappedLinkScannerURLStatusService(
		LinkScannerURLStatusService linkScannerURLStatusService) {
		_linkScannerURLStatusService = linkScannerURLStatusService;
	}

	@Override
	public LinkScannerURLStatusService getWrappedService() {
		return _linkScannerURLStatusService;
	}

	@Override
	public void setWrappedService(
		LinkScannerURLStatusService linkScannerURLStatusService) {
		_linkScannerURLStatusService = linkScannerURLStatusService;
	}

	private LinkScannerURLStatusService _linkScannerURLStatusService;
}