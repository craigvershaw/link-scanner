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
 * Provides a wrapper for {@link LinkCheckerURLStatusLocalService}.
 *
 * @author Craig Vershaw
 * @see LinkCheckerURLStatusLocalService
 * @generated
 */
public class LinkCheckerURLStatusLocalServiceWrapper
	implements LinkCheckerURLStatusLocalService,
		ServiceWrapper<LinkCheckerURLStatusLocalService> {
	public LinkCheckerURLStatusLocalServiceWrapper(
		LinkCheckerURLStatusLocalService linkCheckerURLStatusLocalService) {
		_linkCheckerURLStatusLocalService = linkCheckerURLStatusLocalService;
	}

	/**
	* Returns the Spring bean ID for this bean.
	*
	* @return the Spring bean ID for this bean
	*/
	@Override
	public java.lang.String getBeanIdentifier() {
		return _linkCheckerURLStatusLocalService.getBeanIdentifier();
	}

	/**
	* Sets the Spring bean ID for this bean.
	*
	* @param beanIdentifier the Spring bean ID for this bean
	*/
	@Override
	public void setBeanIdentifier(java.lang.String beanIdentifier) {
		_linkCheckerURLStatusLocalService.setBeanIdentifier(beanIdentifier);
	}

	@Override
	public java.lang.Object invokeMethod(java.lang.String name,
		java.lang.String[] parameterTypes, java.lang.Object[] arguments)
		throws java.lang.Throwable {
		return _linkCheckerURLStatusLocalService.invokeMethod(name,
			parameterTypes, arguments);
	}

	@Override
	public java.lang.String[] getResponse(java.lang.String url)
		throws com.liferay.portal.kernel.exception.PortalException,
			com.liferay.portal.kernel.exception.SystemException {
		return _linkCheckerURLStatusLocalService.getResponse(url);
	}

	@Override
	public java.lang.String[] getResponse(java.lang.String url,
		java.lang.String userAgent)
		throws com.liferay.portal.kernel.exception.PortalException,
			com.liferay.portal.kernel.exception.SystemException {
		return _linkCheckerURLStatusLocalService.getResponse(url, userAgent);
	}

	@Override
	public java.lang.String getResponseCode(java.lang.String url)
		throws com.liferay.portal.kernel.exception.PortalException,
			com.liferay.portal.kernel.exception.SystemException {
		return _linkCheckerURLStatusLocalService.getResponseCode(url);
	}

	@Override
	public java.lang.String getResponseCode(java.lang.String url,
		java.lang.String userAgent)
		throws com.liferay.portal.kernel.exception.PortalException,
			com.liferay.portal.kernel.exception.SystemException {
		return _linkCheckerURLStatusLocalService.getResponseCode(url, userAgent);
	}

	@Override
	public java.lang.String getResponseString(java.lang.String url)
		throws com.liferay.portal.kernel.exception.PortalException,
			com.liferay.portal.kernel.exception.SystemException {
		return _linkCheckerURLStatusLocalService.getResponseString(url);
	}

	@Override
	public java.lang.String getResponseString(java.lang.String url,
		java.lang.String userAgent)
		throws com.liferay.portal.kernel.exception.PortalException,
			com.liferay.portal.kernel.exception.SystemException {
		return _linkCheckerURLStatusLocalService.getResponseString(url,
			userAgent);
	}

	/**
	 * @deprecated As of 6.1.0, replaced by {@link #getWrappedService}
	 */
	public LinkCheckerURLStatusLocalService getWrappedLinkCheckerURLStatusLocalService() {
		return _linkCheckerURLStatusLocalService;
	}

	/**
	 * @deprecated As of 6.1.0, replaced by {@link #setWrappedService}
	 */
	public void setWrappedLinkCheckerURLStatusLocalService(
		LinkCheckerURLStatusLocalService linkCheckerURLStatusLocalService) {
		_linkCheckerURLStatusLocalService = linkCheckerURLStatusLocalService;
	}

	@Override
	public LinkCheckerURLStatusLocalService getWrappedService() {
		return _linkCheckerURLStatusLocalService;
	}

	@Override
	public void setWrappedService(
		LinkCheckerURLStatusLocalService linkCheckerURLStatusLocalService) {
		_linkCheckerURLStatusLocalService = linkCheckerURLStatusLocalService;
	}

	private LinkCheckerURLStatusLocalService _linkCheckerURLStatusLocalService;
}