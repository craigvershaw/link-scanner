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

package edu.nps.portlet.linkchecker.service.http;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import edu.nps.portlet.linkchecker.service.LinkCheckerURLStatusServiceUtil;

import java.rmi.RemoteException;

/**
 * <p>
 * This class provides a SOAP utility for the
 * {@link edu.nps.portlet.linkchecker.service.LinkCheckerURLStatusServiceUtil} service utility. The
 * static methods of this class calls the same methods of the service utility.
 * However, the signatures are different because it is difficult for SOAP to
 * support certain types.
 * </p>
 *
 * <p>
 * The benefits of using the SOAP utility is that it is cross platform
 * compatible. SOAP allows different languages like Java, .NET, C++, PHP, and
 * even Perl, to call the generated services. One drawback of SOAP is that it is
 * slow because it needs to serialize all calls into a text format (XML).
 * </p>
 *
 * <p>
 * You can see a list of services at
 * http://localhost:8080/api/secure/axis. Set the property
 * <b>axis.servlet.hosts.allowed</b> in portal.properties to configure
 * security.
 * </p>
 *
 * <p>
 * The SOAP utility is only generated for remote services.
 * </p>
 *
 * @author    Craig Vershaw
 * @see       LinkCheckerURLStatusServiceHttp
 * @see       edu.nps.portlet.linkchecker.service.LinkCheckerURLStatusServiceUtil
 * @generated
 */
public class LinkCheckerURLStatusServiceSoap {
	public static java.lang.String[] getResponse(java.lang.String url)
		throws RemoteException {
		try {
			java.lang.String[] returnValue = LinkCheckerURLStatusServiceUtil.getResponse(url);

			return returnValue;
		}
		catch (Exception e) {
			_log.error(e, e);

			throw new RemoteException(e.getMessage());
		}
	}

	public static java.lang.String getResponseCode(java.lang.String url)
		throws RemoteException {
		try {
			java.lang.String returnValue = LinkCheckerURLStatusServiceUtil.getResponseCode(url);

			return returnValue;
		}
		catch (Exception e) {
			_log.error(e, e);

			throw new RemoteException(e.getMessage());
		}
	}

	public static java.lang.String getResponseString(java.lang.String url)
		throws RemoteException {
		try {
			java.lang.String returnValue = LinkCheckerURLStatusServiceUtil.getResponseString(url);

			return returnValue;
		}
		catch (Exception e) {
			_log.error(e, e);

			throw new RemoteException(e.getMessage());
		}
	}

	private static Log _log = LogFactoryUtil.getLog(LinkCheckerURLStatusServiceSoap.class);
}