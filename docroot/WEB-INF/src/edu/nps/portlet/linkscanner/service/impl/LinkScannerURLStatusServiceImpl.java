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

package edu.nps.portlet.linkscanner.service.impl;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;

import edu.nps.portlet.linkscanner.service.LinkScannerURLStatusLocalServiceUtil;
import edu.nps.portlet.linkscanner.service.base.LinkScannerURLStatusServiceBaseImpl;

/**
 * The implementation of the link scanner u r l status remote service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the {@link edu.nps.portlet.linkscanner.service.LinkScannerURLStatusService} interface.
 *
 * <p>
 * This is a remote service. Methods of this service are expected to have security checks based on the propagated JAAS credentials because this service can be accessed remotely.
 * </p>
 *
 * @author Craig Vershaw
 * @see edu.nps.portlet.linkscanner.service.base.LinkScannerURLStatusServiceBaseImpl
 * @see edu.nps.portlet.linkscanner.service.LinkScannerURLStatusServiceUtil
 */
public class LinkScannerURLStatusServiceImpl
	extends LinkScannerURLStatusServiceBaseImpl {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never reference this interface directly. Always use {@link edu.nps.portlet.linkscanner.service.LinkScannerURLStatusServiceUtil} to access the link scanner u r l status remote service.
	 */

	public String[] getResponse(String url)
		throws PortalException, SystemException {

		return LinkScannerURLStatusLocalServiceUtil.getResponse(url);
	}

	public String[] getResponse(String url, String userAgent)
		throws PortalException, SystemException {

		return LinkScannerURLStatusLocalServiceUtil.getResponse(url, userAgent);
	}

	public String getResponseCode(String url)
		throws PortalException, SystemException {

		return LinkScannerURLStatusLocalServiceUtil.getResponseCode(url);
	}

	public String getResponseCode(String url, String userAgent)
		throws PortalException, SystemException {

		return LinkScannerURLStatusLocalServiceUtil.getResponseCode(url, userAgent);
	}

	public String getResponseString(String url)
		throws PortalException, SystemException {

		return LinkScannerURLStatusLocalServiceUtil.getResponseString(url);
	}

	public String getResponseString(String url, String userAgent)
		throws PortalException, SystemException {

		return LinkScannerURLStatusLocalServiceUtil.getResponseString(url, userAgent);
	}
}