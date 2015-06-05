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

package edu.nps.portlet.linkscanner.service.http;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import edu.nps.portlet.linkscanner.service.LinkScannerURLStatusServiceUtil;

import java.rmi.RemoteException;

/**
 * Provides the SOAP utility for the
 * {@link edu.nps.portlet.linkscanner.service.LinkScannerURLStatusServiceUtil} service utility. The
 * static methods of this class calls the same methods of the service utility.
 * However, the signatures are different because it is difficult for SOAP to
 * support certain types.
 *
 * <p>
 * The benefits of using the SOAP utility is that it is cross platform
 * compatible. SOAP allows different languages like Java, .NET, C++, PHP, and
 * even Perl, to call the generated services. One drawback of SOAP is that it is
 * slow because it needs to serialize all calls into a text format (XML).
 * </p>
 *
 * <p>
 * You can see a list of services at http://localhost:8080/api/axis. Set the
 * property <b>axis.servlet.hosts.allowed</b> in portal.properties to configure
 * security.
 * </p>
 *
 * <p>
 * The SOAP utility is only generated for remote services.
 * </p>
 *
 * @author Craig Vershaw
 * @see LinkScannerURLStatusServiceHttp
 * @see edu.nps.portlet.linkscanner.service.LinkScannerURLStatusServiceUtil
 * @generated
 */
public class LinkScannerURLStatusServiceSoap {
	public static java.lang.String[] getResponse(java.lang.String url)
		throws RemoteException {
		try {
			java.lang.String[] returnValue = LinkScannerURLStatusServiceUtil.getResponse(url);

			return returnValue;
		}
		catch (Exception e) {
			_log.error(e, e);

			throw new RemoteException(e.getMessage());
		}
	}

	public static java.lang.String[] getResponse(java.lang.String url,
		java.lang.String userAgent) throws RemoteException {
		try {
			java.lang.String[] returnValue = LinkScannerURLStatusServiceUtil.getResponse(url,
					userAgent);

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
			java.lang.String returnValue = LinkScannerURLStatusServiceUtil.getResponseCode(url);

			return returnValue;
		}
		catch (Exception e) {
			_log.error(e, e);

			throw new RemoteException(e.getMessage());
		}
	}

	public static java.lang.String getResponseCode(java.lang.String url,
		java.lang.String userAgent) throws RemoteException {
		try {
			java.lang.String returnValue = LinkScannerURLStatusServiceUtil.getResponseCode(url,
					userAgent);

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
			java.lang.String returnValue = LinkScannerURLStatusServiceUtil.getResponseString(url);

			return returnValue;
		}
		catch (Exception e) {
			_log.error(e, e);

			throw new RemoteException(e.getMessage());
		}
	}

	public static java.lang.String getResponseString(java.lang.String url,
		java.lang.String userAgent) throws RemoteException {
		try {
			java.lang.String returnValue = LinkScannerURLStatusServiceUtil.getResponseString(url,
					userAgent);

			return returnValue;
		}
		catch (Exception e) {
			_log.error(e, e);

			throw new RemoteException(e.getMessage());
		}
	}

	private static Log _log = LogFactoryUtil.getLog(LinkScannerURLStatusServiceSoap.class);
}