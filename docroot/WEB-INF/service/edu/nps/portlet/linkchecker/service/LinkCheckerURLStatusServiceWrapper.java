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

package edu.nps.portlet.linkchecker.service;

import com.liferay.portal.service.ServiceWrapper;

/**
 * Provides a wrapper for {@link LinkCheckerURLStatusService}.
 *
 * @author Craig Vershaw
 * @see LinkCheckerURLStatusService
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
	@Override
	public java.lang.String getBeanIdentifier() {
		return _linkCheckerURLStatusService.getBeanIdentifier();
	}

	/**
	* Sets the Spring bean ID for this bean.
	*
	* @param beanIdentifier the Spring bean ID for this bean
	*/
	@Override
	public void setBeanIdentifier(java.lang.String beanIdentifier) {
		_linkCheckerURLStatusService.setBeanIdentifier(beanIdentifier);
	}

	@Override
	public java.lang.Object invokeMethod(java.lang.String name,
		java.lang.String[] parameterTypes, java.lang.Object[] arguments)
		throws java.lang.Throwable {
		return _linkCheckerURLStatusService.invokeMethod(name, parameterTypes,
			arguments);
	}

	@Override
	public java.lang.String[] getResponse(java.lang.String url)
		throws com.liferay.portal.kernel.exception.PortalException,
			com.liferay.portal.kernel.exception.SystemException {
		return _linkCheckerURLStatusService.getResponse(url);
	}

	@Override
	public java.lang.String[] getResponse(java.lang.String url,
		java.lang.String userAgent)
		throws com.liferay.portal.kernel.exception.PortalException,
			com.liferay.portal.kernel.exception.SystemException {
		return _linkCheckerURLStatusService.getResponse(url, userAgent);
	}

	@Override
	public java.lang.String getResponseCode(java.lang.String url)
		throws com.liferay.portal.kernel.exception.PortalException,
			com.liferay.portal.kernel.exception.SystemException {
		return _linkCheckerURLStatusService.getResponseCode(url);
	}

	@Override
	public java.lang.String getResponseCode(java.lang.String url,
		java.lang.String userAgent)
		throws com.liferay.portal.kernel.exception.PortalException,
			com.liferay.portal.kernel.exception.SystemException {
		return _linkCheckerURLStatusService.getResponseCode(url, userAgent);
	}

	@Override
	public java.lang.String getResponseString(java.lang.String url)
		throws com.liferay.portal.kernel.exception.PortalException,
			com.liferay.portal.kernel.exception.SystemException {
		return _linkCheckerURLStatusService.getResponseString(url);
	}

	@Override
	public java.lang.String getResponseString(java.lang.String url,
		java.lang.String userAgent)
		throws com.liferay.portal.kernel.exception.PortalException,
			com.liferay.portal.kernel.exception.SystemException {
		return _linkCheckerURLStatusService.getResponseString(url, userAgent);
	}

	/**
	 * @deprecated As of 6.1.0, replaced by {@link #getWrappedService}
	 */
	public LinkCheckerURLStatusService getWrappedLinkCheckerURLStatusService() {
		return _linkCheckerURLStatusService;
	}

	/**
	 * @deprecated As of 6.1.0, replaced by {@link #setWrappedService}
	 */
	public void setWrappedLinkCheckerURLStatusService(
		LinkCheckerURLStatusService linkCheckerURLStatusService) {
		_linkCheckerURLStatusService = linkCheckerURLStatusService;
	}

	@Override
	public LinkCheckerURLStatusService getWrappedService() {
		return _linkCheckerURLStatusService;
	}

	@Override
	public void setWrappedService(
		LinkCheckerURLStatusService linkCheckerURLStatusService) {
		_linkCheckerURLStatusService = linkCheckerURLStatusService;
	}

	private LinkCheckerURLStatusService _linkCheckerURLStatusService;
}