package edu.nps.portlet.linkscanner.util;


public class LinkScannerConstants {

	public static final String LABEL_LINKS = "links";

	public static final String LABEL_IMAGES = "images";

	public static final String LABEL_LINKS_AND_IMAGES = "links-and-images";

	public static String linkImagesLabel(boolean checkLinks, boolean checkImages) {

		if (checkLinks && checkImages) {
			return LABEL_LINKS_AND_IMAGES;
		}
		else if (checkImages) {
			return LABEL_IMAGES;
		}
		else {
			return LABEL_LINKS;
		}
	}

	public static final String LINK_SCANNER_DISPLAY = "linkscanner_WAR_linkscannerportlet";

	public static final String CALENDAR = "1_WAR_calendarportlet";

}
