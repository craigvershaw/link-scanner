package edu.nps.portlet.linkchecker.util;


public class LinkCheckerConstants {

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

}
