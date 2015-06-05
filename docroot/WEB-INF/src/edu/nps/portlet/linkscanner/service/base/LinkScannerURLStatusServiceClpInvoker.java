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

package edu.nps.portlet.linkscanner.service.base;

import edu.nps.portlet.linkscanner.service.LinkScannerURLStatusServiceUtil;

import java.util.Arrays;

/**
 * @author Craig Vershaw
 * @generated
 */
public class LinkScannerURLStatusServiceClpInvoker {
	public LinkScannerURLStatusServiceClpInvoker() {
		_methodName16 = "getBeanIdentifier";

		_methodParameterTypes16 = new String[] {  };

		_methodName17 = "setBeanIdentifier";

		_methodParameterTypes17 = new String[] { "java.lang.String" };

		_methodName20 = "getResponse";

		_methodParameterTypes20 = new String[] { "java.lang.String" };

		_methodName21 = "getResponse";

		_methodParameterTypes21 = new String[] {
				"java.lang.String", "java.lang.String"
			};

		_methodName22 = "getResponseCode";

		_methodParameterTypes22 = new String[] { "java.lang.String" };

		_methodName23 = "getResponseCode";

		_methodParameterTypes23 = new String[] {
				"java.lang.String", "java.lang.String"
			};

		_methodName24 = "getResponseString";

		_methodParameterTypes24 = new String[] { "java.lang.String" };

		_methodName25 = "getResponseString";

		_methodParameterTypes25 = new String[] {
				"java.lang.String", "java.lang.String"
			};
	}

	public Object invokeMethod(String name, String[] parameterTypes,
		Object[] arguments) throws Throwable {
		if (_methodName16.equals(name) &&
				Arrays.deepEquals(_methodParameterTypes16, parameterTypes)) {
			return LinkScannerURLStatusServiceUtil.getBeanIdentifier();
		}

		if (_methodName17.equals(name) &&
				Arrays.deepEquals(_methodParameterTypes17, parameterTypes)) {
			LinkScannerURLStatusServiceUtil.setBeanIdentifier((java.lang.String)arguments[0]);

			return null;
		}

		if (_methodName20.equals(name) &&
				Arrays.deepEquals(_methodParameterTypes20, parameterTypes)) {
			return LinkScannerURLStatusServiceUtil.getResponse((java.lang.String)arguments[0]);
		}

		if (_methodName21.equals(name) &&
				Arrays.deepEquals(_methodParameterTypes21, parameterTypes)) {
			return LinkScannerURLStatusServiceUtil.getResponse((java.lang.String)arguments[0],
				(java.lang.String)arguments[1]);
		}

		if (_methodName22.equals(name) &&
				Arrays.deepEquals(_methodParameterTypes22, parameterTypes)) {
			return LinkScannerURLStatusServiceUtil.getResponseCode((java.lang.String)arguments[0]);
		}

		if (_methodName23.equals(name) &&
				Arrays.deepEquals(_methodParameterTypes23, parameterTypes)) {
			return LinkScannerURLStatusServiceUtil.getResponseCode((java.lang.String)arguments[0],
				(java.lang.String)arguments[1]);
		}

		if (_methodName24.equals(name) &&
				Arrays.deepEquals(_methodParameterTypes24, parameterTypes)) {
			return LinkScannerURLStatusServiceUtil.getResponseString((java.lang.String)arguments[0]);
		}

		if (_methodName25.equals(name) &&
				Arrays.deepEquals(_methodParameterTypes25, parameterTypes)) {
			return LinkScannerURLStatusServiceUtil.getResponseString((java.lang.String)arguments[0],
				(java.lang.String)arguments[1]);
		}

		throw new UnsupportedOperationException();
	}

	private String _methodName16;
	private String[] _methodParameterTypes16;
	private String _methodName17;
	private String[] _methodParameterTypes17;
	private String _methodName20;
	private String[] _methodParameterTypes20;
	private String _methodName21;
	private String[] _methodParameterTypes21;
	private String _methodName22;
	private String[] _methodParameterTypes22;
	private String _methodName23;
	private String[] _methodParameterTypes23;
	private String _methodName24;
	private String[] _methodParameterTypes24;
	private String _methodName25;
	private String[] _methodParameterTypes25;
}