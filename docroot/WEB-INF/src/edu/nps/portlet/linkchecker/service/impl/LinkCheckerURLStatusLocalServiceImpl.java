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

package edu.nps.portlet.linkchecker.service.impl;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import edu.nps.portlet.linkchecker.service.base.LinkCheckerURLStatusLocalServiceBaseImpl;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;

/**
 * The implementation of the link checker u r l status local service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the {@link edu.nps.portlet.linkchecker.service.LinkCheckerURLStatusLocalService} interface.
 *
 * <p>
 * This is a local service. Methods of this service will not have security checks based on the propagated JAAS credentials because this service can only be accessed from within the same VM.
 * </p>
 *
 * @author Craig Vershaw
 * @see edu.nps.portlet.linkchecker.service.base.LinkCheckerURLStatusLocalServiceBaseImpl
 * @see edu.nps.portlet.linkchecker.service.LinkCheckerURLStatusLocalServiceUtil
 */
public class LinkCheckerURLStatusLocalServiceImpl
	extends LinkCheckerURLStatusLocalServiceBaseImpl {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never reference this interface directly. Always use {@link edu.nps.portlet.linkchecker.service.LinkCheckerURLStatusLocalServiceUtil} to access the link checker u r l status local service.
	 */

	public String[] getResponse(String url)
		throws PortalException, SystemException {

		String[] result = new String[3];
		result[0] = "-1";
		result[1] = "Unknown Error";
		result[2] = "";

		try {

			URL urlObject = new URL(url);
			URLConnection urlConnection = urlObject.openConnection();
		
			HttpURLConnection.setFollowRedirects(true);
			HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
			httpURLConnection.setRequestMethod("HEAD");
		
			result[0] = String.valueOf(httpURLConnection.getResponseCode());
			result[1] = httpURLConnection.getResponseMessage();
			result[2] = httpURLConnection.getContentType();
		} 
		catch(UnknownHostException unknownHostException){

			_log.error("Unknown Host: " + url);
			result[1] = "Unknown Host";
		}
		catch (Exception e) {

			_log.error("Error: " + url);
			_log.error(e.getMessage());
			result[1] = e.getMessage();
		}

		return result;
	}

	public String getResponseCode(String url)
		throws PortalException, SystemException {

		return getResponse(url)[0];
	}

	public String getResponseString(String url)
		throws PortalException, SystemException {

		String[] result = getResponse(url);
		
		return result[0] + " " + result[1];
	}

	private static Log _log = LogFactoryUtil.getLog(LinkCheckerURLStatusLocalServiceImpl.class);
}