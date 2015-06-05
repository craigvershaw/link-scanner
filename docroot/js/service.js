Liferay.Service.register("Liferay.Service.LinkScanner", "edu.nps.portlet.linkscanner.service", "link-scanner-portlet");

Liferay.Service.registerClass(
	Liferay.Service.LinkScanner, "LinkScannerURLStatus",
	{
		getResponse: true,
		getResponseCode: true,
		getResponseString: true
	}
);