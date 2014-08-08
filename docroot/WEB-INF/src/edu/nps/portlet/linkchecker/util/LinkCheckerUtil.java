package edu.nps.portlet.linkchecker.util;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.HttpUtil;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.Layout;
import com.liferay.portal.model.LayoutTypePortlet;
import com.liferay.portal.model.Portlet;
import com.liferay.portal.service.LayoutLocalServiceUtil;
import com.liferay.portal.service.PortletPreferencesLocalServiceUtil;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portal.util.PortletKeys;
import com.liferay.portal.util.comparator.LayoutComparator;
import com.liferay.portlet.journal.model.JournalArticle;
import com.liferay.portlet.journalcontent.util.JournalContentUtil;
import com.liferay.portlet.journal.service.JournalArticleLocalServiceUtil;
import com.liferay.portlet.wiki.model.WikiNode;
import com.liferay.portlet.wiki.model.WikiPage;
import com.liferay.portlet.wiki.service.WikiNodeLocalServiceUtil;
import com.liferay.portlet.wiki.service.WikiPageLocalServiceUtil;
import com.liferay.util.HTMLParser;

import edu.nps.portlet.linkchecker.util.ContentLinks;

import java.io.IOException;
import java.io.StringReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.portlet.PortletPreferences;

public class LinkCheckerUtil {

	public static Set<String> getPortalURLPrefixes(ThemeDisplay themeDisplay)
		throws SystemException, PortalException {

		Set<String> portalURLPrefixes = new HashSet<String>();
		portalURLPrefixes.add(themeDisplay.getURLPortal());
		portalURLPrefixes.add("/");
		portalURLPrefixes.add(".");
		// TODO: protocol relative urls that begin with //

		String[] portalURLPrefixesAdd = getPortalURLPrefixesAdd(themeDisplay.getCompanyId());

		if (Validator.isNotNull(portalURLPrefixesAdd)) {
			portalURLPrefixes.addAll(ListUtil.fromArray(portalURLPrefixesAdd));
		}

		return portalURLPrefixes;
	}

	public static String[] getPortalURLPrefixesAdd(long companyId) 
		throws SystemException, PortalException {

		PortletPreferences preferences = getPreferences(companyId);

		return preferences.getValues("portalURLPrefixesAdd", new String[0]);
	}

	public static PortletPreferences getPreferences(long companyId) 
		throws SystemException, PortalException {

		int ownerType = PortletKeys.PREFS_OWNER_TYPE_COMPANY;
		long plid = PortletKeys.PREFS_PLID_SHARED;

		return PortletPreferencesLocalServiceUtil.getPreferences(
			companyId, companyId, ownerType, plid, LINK_CHECKER_DISPLAY);

	}

	public static List<ContentLinks> getContentLinks(String contentType, long groupId, String languageId, ThemeDisplay themeDisplay, boolean getLinks, boolean getImages)
		throws Exception {

		if (contentType.equals("web-content")) {
			return getWebContentLinks(groupId, languageId, themeDisplay, getLinks, getImages);
		}
		else if (contentType.equals("wiki-pages")) {
			return getWikiContentLinks(groupId, languageId, themeDisplay, getLinks, getImages);
		}
		else if (contentType.equals("rss-portlet-subscriptions")) {
			return getRSSPortletLinks(groupId, languageId, themeDisplay, getLinks, getImages);
		}
		else {
			return null;
		}
	}

	public static List<ContentLinks> getRSSPortletLinks(long groupId, String languageId, ThemeDisplay themeDisplay, boolean getLinks, boolean getImages)
		throws Exception {

		_log.info("getRSSPortletLinks for groupId " + String.valueOf(groupId));

		List<ContentLinks> contentLinksList = new ArrayList<ContentLinks>();

		List<Layout> allLayouts = new LinkedList<Layout>();
		
		allLayouts.addAll(LayoutLocalServiceUtil.getLayouts(themeDisplay.getScopeGroupId(), false));
		allLayouts.addAll(LayoutLocalServiceUtil.getLayouts(themeDisplay.getScopeGroupId(), true));
		
		List<Layout> sortedLayouts = new ArrayList<Layout>(allLayouts);
		Collections.sort(sortedLayouts, new LayoutComparator());

		for (Layout layout : allLayouts) {
			
			LayoutTypePortlet layoutTypePortlet = (LayoutTypePortlet) layout.getLayoutType();
			
			try {
				
				for (Portlet portlet : layoutTypePortlet.getAllPortlets()) {
					
					if (portlet.getPortletName().equals(PortletKeys.RSS)) {
						
						_log.debug("Extracting links from RSS portlet " + layout.getFriendlyURL() + " - " + layout.getName());

						ContentLinks contentLinks = new ContentLinks();
						contentLinks.setClassName(portlet.getPortletClass());
						contentLinks.setClassPK(portlet.getInstanceId());
						contentLinks.setContentEditLink(getLayoutURL(themeDisplay, layout));
						contentLinks.setModifiedDate(layout.getModifiedDate());
						
						javax.portlet.PortletPreferences portletPreferences = PortletPreferencesLocalServiceUtil.getPreferences(
							portlet.getCompanyId(), PortletKeys.PREFS_OWNER_ID_DEFAULT, PortletKeys.PREFS_OWNER_TYPE_LAYOUT, layout.getPlid(), portlet.getPortletId());

						contentLinks.setContentTitle(layout.getName() + " (" + portletPreferences.getValue("portletSetupTitle_" + themeDisplay.getLocale(), portlet.getDisplayName()) + ")");

						for (String url : portletPreferences.getValues("urls", new String[0])) {
							
							_log.debug("	 " + url);
							
							contentLinks.addLink(url);
						}

						contentLinksList.add(contentLinks);
					}
				}
			}
			catch (PortalException e) {
				_log.error(e.getMessage(), e);
			}
			catch (SystemException e) {
				_log.error(e.getMessage(), e);
			}
		}

		return contentLinksList;
	}

	public static List<ContentLinks> getWebContentLinks(long groupId, String languageId, ThemeDisplay themeDisplay, boolean getLinks, boolean getImages)
		throws Exception {

		_log.info("getWebContentLinks for groupId " + String.valueOf(groupId));

		List<ContentLinks> contentLinksList = new ArrayList<ContentLinks>();

		List<JournalArticle> journalArticleList = JournalArticleLocalServiceUtil.getArticles(groupId);

		String editLink = themeDisplay.getURLControlPanel();
		editLink = HttpUtil.setParameter(editLink, "p_p_id", PortletKeys.JOURNAL);
		editLink = HttpUtil.setParameter(editLink, "p_p_lifecycle", "0");
		editLink = HttpUtil.setParameter(editLink, "p_p_state", "maximized");
		editLink = HttpUtil.setParameter(editLink, "p_p_mode", "view");
		editLink = HttpUtil.setParameter(editLink, PortalUtil.getPortletNamespace(PortletKeys.JOURNAL) + "struts_action", "/journal/edit_article");
		editLink = HttpUtil.setParameter(editLink, PortalUtil.getPortletNamespace(PortletKeys.JOURNAL) + "redirect", themeDisplay.getURLCurrent());
		editLink = HttpUtil.setParameter(editLink, PortalUtil.getPortletNamespace(PortletKeys.JOURNAL) + "groupId", groupId);

		for (JournalArticle journalArticle : journalArticleList) {

			if (JournalArticleLocalServiceUtil.isLatestVersion(journalArticle.getGroupId(), journalArticle.getArticleId(), journalArticle.getVersion())) {

				String content = JournalContentUtil.getContent(groupId, journalArticle.getArticleId(), null, null, languageId, themeDisplay);

				if (content != null) {

					List<String> links = parseLinks(content, getLinks, getImages);

					if (links.size() > 0) {

						editLink = HttpUtil.setParameter(editLink, PortalUtil.getPortletNamespace(PortletKeys.JOURNAL) + "articleId", journalArticle.getArticleId());

						ContentLinks contentLinks = new ContentLinks();
						contentLinks.setClassName(journalArticle.getClassName());
						contentLinks.setClassPK(journalArticle.getArticleId());
						contentLinks.setContentTitle(journalArticle.getTitle(themeDisplay.getLocale()));
						contentLinks.setContentEditLink(editLink);
						contentLinks.setModifiedDate(journalArticle.getModifiedDate());
						contentLinks.setStatus(journalArticle.getStatus());
						
						_log.debug("Extracting links from journal article " + journalArticle.getArticleId() + " - " + journalArticle.getTitle());
						
						for (String link : links) {

							contentLinks.addLink(link);
						}
						
						contentLinksList.add(contentLinks);
					}
				}
			}
		}

		return contentLinksList;
	}

	public static List<ContentLinks> getWikiContentLinks(long groupId, String languageId, ThemeDisplay themeDisplay, boolean getLinks, boolean getImages)
		throws Exception {

		_log.info("getWikiContentLinks for groupId " + String.valueOf(groupId));

		List<ContentLinks> contentLinksList = new ArrayList<ContentLinks>();

		List<WikiNode> wikiNodeList = WikiNodeLocalServiceUtil.getNodes(groupId);

		List<WikiPage> wikiPageList = new ArrayList<WikiPage>();

		for (WikiNode wikiNode : wikiNodeList) {

			wikiPageList.addAll(WikiPageLocalServiceUtil.getPages(wikiNode.getNodeId(), true, 0, WikiPageLocalServiceUtil.getPagesCount(wikiNode.getNodeId(), true)));
		}

		String editLink = themeDisplay.getURLControlPanel();
		editLink = HttpUtil.setParameter(editLink, "p_p_id", PortletKeys.WIKI_ADMIN);
		editLink = HttpUtil.setParameter(editLink, "p_p_lifecycle", "0");
		editLink = HttpUtil.setParameter(editLink, "p_p_state", "maximized");
		editLink = HttpUtil.setParameter(editLink, "p_p_mode", "view");
		editLink = HttpUtil.setParameter(editLink, PortalUtil.getPortletNamespace(PortletKeys.WIKI_ADMIN) + "struts_action", "/wiki_admin/edit_page");
		editLink = HttpUtil.setParameter(editLink, PortalUtil.getPortletNamespace(PortletKeys.WIKI_ADMIN) + "redirect", themeDisplay.getURLCurrent());
		editLink = HttpUtil.setParameter(editLink, PortalUtil.getPortletNamespace(PortletKeys.WIKI_ADMIN) + "groupId", groupId);

		for (WikiPage wikiPage : wikiPageList) {

			String content = wikiPage.getContent();

			if (content != null && wikiPage.getFormat().equals("html")) {

				List<String> links = parseLinks(content, getLinks, getImages);

				if (links.size() > 0) {

					editLink = HttpUtil.setParameter(editLink, PortalUtil.getPortletNamespace(PortletKeys.WIKI_ADMIN) + "nodeId", wikiPage.getNodeId());
					editLink = HttpUtil.setParameter(editLink, PortalUtil.getPortletNamespace(PortletKeys.WIKI_ADMIN) + "title", wikiPage.getTitle());

					ContentLinks contentLinks = new ContentLinks();
					contentLinks.setClassName(wikiPage.getModelClassName());
					contentLinks.setClassPK(String.valueOf(wikiPage.getPageId()));
					contentLinks.setContentTitle(wikiPage.getTitle());
					contentLinks.setContentEditLink(editLink);
					contentLinks.setModifiedDate(wikiPage.getModifiedDate());
					contentLinks.setStatus(wikiPage.getStatus());
					
					_log.debug("Extracting links from wiki page " + wikiPage.getPageId() + " - " + wikiPage.getTitle());
					
					for (String link : links) {

						contentLinks.addLink(link);
					}
					
					contentLinksList.add(contentLinks);
				}
			}
		}

		return contentLinksList;
	}

	public static String getLayoutURL(ThemeDisplay themeDisplay, Layout layout) {

		String url = "";

		try {

			url = themeDisplay.getPortalURL() +
							(layout.isPrivateLayout()? themeDisplay.getPathFriendlyURLPrivateGroup(): themeDisplay.getPathFriendlyURLPublic()) +
							layout.getGroup().getFriendlyURL() + 
							layout.getFriendlyURL();
		}
		catch (PortalException e) {
			_log.error("PortalException: " + e.getMessage(), e);
		}
		catch (SystemException e) {
			_log.error("SystemException: " + e.getMessage(), e);
		}

		return url;
	}

	public static boolean isPortalLink(String url, ThemeDisplay themeDisplay)
		throws SystemException, PortalException {

		for (String portalURLPrefix : getPortalURLPrefixes(themeDisplay)) {

			if (url.startsWith(portalURLPrefix))
				return true;
		}
		
		return false;
	}

	public static List<String> parseLinks(String content, boolean getLinks, boolean getImages)
		throws IOException {

		List<String> links = new ArrayList<String>();

		Reader contentReader = new StringReader(content);
		HTMLParser htmlParser = new HTMLParser(contentReader);

		if (getLinks)
			links.addAll(htmlParser.getLinks());

		if (getImages)
			links.addAll(htmlParser.getImages());

		cleanLinks(links);

		return links;
	}

	private static void cleanLinks(List<String> links) {

		Iterator<String> linksIterator = links.iterator();

		while (linksIterator.hasNext()) {

			String link = linksIterator.next();

			if (link.startsWith("javascript") ||
				link.startsWith("mailto") ||
				link.startsWith("#")) {

				linksIterator.remove();
			}
		}
	}

	private static final String LINK_CHECKER_DISPLAY = "linkchecker_WAR_linkcheckerportlet";

	private static Log _log = LogFactoryUtil.getLog(LinkCheckerUtil.class);

}
