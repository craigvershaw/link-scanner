Liferay.Service.register("Liferay.Service.LinkChecker", "edu.nps.portlet.linkchecker.service", "link-checker-portlet");

Liferay.Service.registerClass(
	Liferay.Service.LinkChecker, "LinkCheckerURLStatus",
	{
		getResponse: true,
		getResponseCode: true,
		getResponseString: true
	}
);