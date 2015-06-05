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

import com.liferay.portal.kernel.bean.PortletBeanLocatorUtil;
import com.liferay.portal.kernel.util.ReferenceRegistry;
import com.liferay.portal.service.InvokableLocalService;

/**
 * Provides the local service utility for LinkScannerURLStatus. This utility wraps
 * {@link edu.nps.portlet.linkscanner.service.impl.LinkScannerURLStatusLocalServiceImpl} and is the
 * primary access point for service operations in application layer code running
 * on the local server. Methods of this service will not have security checks
 * based on the propagated JAAS credentials because this service can only be
 * accessed from within the same VM.
 *
 * @author Craig Vershaw
 * @see LinkScannerURLStatusLocalService
 * @see edu.nps.portlet.linkscanner.service.base.LinkScannerURLStatusLocalServiceBaseImpl
 * @see edu.nps.portlet.linkscanner.service.impl.LinkScannerURLStatusLocalServiceImpl
 * @generated
 */
public class LinkScannerURLStatusLocalServiceUtil {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to {@link edu.nps.portlet.linkscanner.service.impl.LinkScannerURLStatusLocalServiceImpl} and rerun ServiceBuilder to regenerate this class.
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

	public static java.lang.String[] getResponse(java.lang.String url,
		java.lang.String userAgent)
		throws com.liferay.portal.kernel.exception.PortalException,
			com.liferay.portal.kernel.exception.SystemException {
		return getService().getResponse(url, userAgent);
	}

	public static java.lang.String getResponseCode(java.lang.String url)
		throws com.liferay.portal.kernel.exception.PortalException,
			com.liferay.portal.kernel.exception.SystemException {
		return getService().getResponseCode(url);
	}

	public static java.lang.String getResponseCode(java.lang.String url,
		java.lang.String userAgent)
		throws com.liferay.portal.kernel.exception.PortalException,
			com.liferay.portal.kernel.exception.SystemException {
		return getService().getResponseCode(url, userAgent);
	}

	public static java.lang.String getResponseString(java.lang.String url)
		throws com.liferay.portal.kernel.exception.PortalException,
			com.liferay.portal.kernel.exception.SystemException {
		return getService().getResponseString(url);
	}

	public static java.lang.String getResponseString(java.lang.String url,
		java.lang.String userAgent)
		throws com.liferay.portal.kernel.exception.PortalException,
			com.liferay.portal.kernel.exception.SystemException {
		return getService().getResponseString(url, userAgent);
	}

	public static void clearService() {
		_service = null;
	}

	public static LinkScannerURLStatusLocalService getService() {
		if (_service == null) {
			InvokableLocalService invokableLocalService = (InvokableLocalService)PortletBeanLocatorUtil.locate(ClpSerializer.getServletContextName(),
					LinkScannerURLStatusLocalService.class.getName());

			if (invokableLocalService instanceof LinkScannerURLStatusLocalService) {
				_service = (LinkScannerURLStatusLocalService)invokableLocalService;
			}
			else {
				_service = new LinkScannerURLStatusLocalServiceClp(invokableLocalService);
			}

			ReferenceRegistry.registerReference(LinkScannerURLStatusLocalServiceUtil.class,
				"_service");
		}

		return _service;
	}

	/**
	 * @deprecated As of 6.2.0
	 */
	public void setService(LinkScannerURLStatusLocalService service) {
	}

	private static LinkScannerURLStatusLocalService _service;
}