package edu.nps.portlet.linkchecker.util;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.parsers.bbcode.BBCodeTranslatorUtil;
import com.liferay.portal.kernel.util.HttpUtil;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.model.Layout;
import com.liferay.portal.model.LayoutTypePortlet;
import com.liferay.portal.model.Portlet;
import com.liferay.portal.service.LayoutLocalServiceUtil;
import com.liferay.portal.service.PortletPreferencesLocalServiceUtil;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portal.util.PortletKeys;
import com.liferay.portal.util.comparator.LayoutComparator;
import com.liferay.portlet.blogs.model.BlogsEntry;
import com.liferay.portlet.blogs.service.BlogsEntryLocalServiceUtil;
import com.liferay.portlet.bookmarks.model.BookmarksEntry;
import com.liferay.portlet.bookmarks.service.BookmarksEntryLocalServiceUtil;
import com.liferay.portlet.calendar.model.CalEvent;
import com.liferay.portlet.calendar.service.CalEventLocalServiceUtil;
import com.liferay.portlet.journal.model.JournalArticle;
import com.liferay.portlet.journalcontent.util.JournalContentUtil;
import com.liferay.portlet.journal.service.JournalArticleLocalServiceUtil;
import com.liferay.portlet.messageboards.model.MBMessage;
import com.liferay.portlet.messageboards.service.MBMessageLocalServiceUtil;
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

		if (contentType.equals("blog-entries")) {
			return getBlogLinks(groupId, languageId, themeDisplay, getLinks, getImages);
		}
		else if (contentType.equals("bookmarks")) {
			return getBookmarkLinks(groupId, languageId, themeDisplay, getLinks, getImages);
		}
		else if (contentType.equals("calendar-events")) {
			return getCalendarLinks(groupId, languageId, themeDisplay, getLinks, getImages);
		}
		else if (contentType.equals("message-board-messages")) {
			return getMBMessageLinks(groupId, languageId, themeDisplay, getLinks, getImages);
		}
		else if (contentType.equals("rss-portlet-subscriptions")) {
			return getRSSPortletLinks(groupId, languageId, themeDisplay, getLinks, getImages);
		}
		else if (contentType.equals("web-content")) {
			return getWebContentLinks(groupId, languageId, themeDisplay, getLinks, getImages);
		}
		else if (contentType.equals("wiki-pages")) {
			return getWikiContentLinks(groupId, languageId, themeDisplay, getLinks, getImages);
		}
		else {
			return null;
		}
	}

	public static List<ContentLinks> getBlogLinks(long groupId, String languageId, ThemeDisplay themeDisplay, boolean getLinks, boolean getImages)
		throws Exception {

		_log.info("getBlogLinks for groupId " + String.valueOf(groupId));

		List<ContentLinks> contentLinksList = new ArrayList<ContentLinks>();

		List<BlogsEntry> blogsEntryList = BlogsEntryLocalServiceUtil.getGroupEntries(groupId, WorkflowConstants.STATUS_APPROVED, 0, -1);

		String editLink = themeDisplay.getURLControlPanel();
		editLink = HttpUtil.setParameter(editLink, "p_p_id", PortletKeys.BLOGS_ADMIN);
		editLink = HttpUtil.setParameter(editLink, "p_p_lifecycle", "0");
		editLink = HttpUtil.setParameter(editLink, "p_p_state", "maximized");
		editLink = HttpUtil.setParameter(editLink, "p_p_mode", "view");
		editLink = HttpUtil.setParameter(editLink, PortalUtil.getPortletNamespace(PortletKeys.BLOGS_ADMIN) + "struts_action", "/blogs_admin/edit_entry");
		editLink = HttpUtil.setParameter(editLink, PortalUtil.getPortletNamespace(PortletKeys.BLOGS_ADMIN) + "redirect", themeDisplay.getURLCurrent());
		editLink = HttpUtil.setParameter(editLink, PortalUtil.getPortletNamespace(PortletKeys.BLOGS_ADMIN) + "groupId", groupId);

		for (BlogsEntry blogsEntry : blogsEntryList) {

			String content = blogsEntry.getContent();

			if (content != null) {

				List<String> links = parseLinks(content, getLinks, getImages);

				if (links.size() > 0) {

					editLink = HttpUtil.setParameter(editLink, PortalUtil.getPortletNamespace(PortletKeys.BLOGS_ADMIN) + "entryId", blogsEntry.getEntryId());

					ContentLinks contentLinks = new ContentLinks();
					contentLinks.setClassName(blogsEntry.getModelClassName());
					contentLinks.setClassPK(blogsEntry.getEntryId());
					contentLinks.setContentTitle(blogsEntry.getTitle());
					contentLinks.setContentEditLink(editLink);
					contentLinks.setModifiedDate(blogsEntry.getModifiedDate());
					contentLinks.setStatus(blogsEntry.getStatus());
					
					_log.debug("Extracting links from blog entry " + blogsEntry.getEntryId() + " - " + blogsEntry.getTitle());
					
					for (String link : links) {

						contentLinks.addLink(link);
					}
					
					contentLinksList.add(contentLinks);
				}
			}
		}

		return contentLinksList;
	}

	public static List<ContentLinks> getBookmarkLinks(long groupId, String languageId, ThemeDisplay themeDisplay, boolean getLinks, boolean getImages)
		throws Exception {

		_log.info("getBookmarkLinks for groupId " + String.valueOf(groupId));

		List<ContentLinks> contentLinksList = new ArrayList<ContentLinks>();

		List<BookmarksEntry> bookmarksEntryList = BookmarksEntryLocalServiceUtil.getGroupEntries(groupId, 0, -1);

		String editLink = themeDisplay.getURLControlPanel();
		editLink = HttpUtil.setParameter(editLink, "p_p_id", PortletKeys.BOOKMARKS);
		editLink = HttpUtil.setParameter(editLink, "p_p_lifecycle", "0");
		editLink = HttpUtil.setParameter(editLink, "p_p_state", "maximized");
		editLink = HttpUtil.setParameter(editLink, "p_p_mode", "view");
		editLink = HttpUtil.setParameter(editLink, PortalUtil.getPortletNamespace(PortletKeys.BOOKMARKS) + "struts_action", "/bookmarks/edit_entry");
		editLink = HttpUtil.setParameter(editLink, PortalUtil.getPortletNamespace(PortletKeys.BOOKMARKS) + "redirect", themeDisplay.getURLCurrent());
		editLink = HttpUtil.setParameter(editLink, PortalUtil.getPortletNamespace(PortletKeys.BOOKMARKS) + "groupId", groupId);

		for (BookmarksEntry bookmarksEntry : bookmarksEntryList) {

			String link = bookmarksEntry.getUrl();

			if (link != null) {

				editLink = HttpUtil.setParameter(editLink, PortalUtil.getPortletNamespace(PortletKeys.BOOKMARKS) + "entryId", bookmarksEntry.getEntryId());

				ContentLinks contentLinks = new ContentLinks();
				contentLinks.setClassName(bookmarksEntry.getModelClassName());
				contentLinks.setClassPK(bookmarksEntry.getEntryId());
				contentLinks.setContentTitle(bookmarksEntry.getName());
				contentLinks.setContentEditLink(editLink);
				contentLinks.setModifiedDate(bookmarksEntry.getModifiedDate());
				
				_log.debug("Extracting link from bookmarks entry " + bookmarksEntry.getEntryId() + " - " + bookmarksEntry.getName());
				
				contentLinks.addLink(link);
				
				contentLinksList.add(contentLinks);
			}
		}

		return contentLinksList;
	}

	public static List<ContentLinks> getCalendarLinks(long groupId, String languageId, ThemeDisplay themeDisplay, boolean getLinks, boolean getImages)
		throws Exception {

		_log.info("getCalendarLinks for groupId " + String.valueOf(groupId));

		List<ContentLinks> contentLinksList = new ArrayList<ContentLinks>();

		List<CalEvent> calEventList = CalEventLocalServiceUtil.getEvents(groupId, "", 0, -1);

		String editLink = themeDisplay.getURLControlPanel();
		editLink = HttpUtil.setParameter(editLink, "p_p_id", PortletKeys.CALENDAR);
		editLink = HttpUtil.setParameter(editLink, "p_p_lifecycle", "0");
		editLink = HttpUtil.setParameter(editLink, "p_p_state", "maximized");
		editLink = HttpUtil.setParameter(editLink, "p_p_mode", "view");
		editLink = HttpUtil.setParameter(editLink, PortalUtil.getPortletNamespace(PortletKeys.CALENDAR) + "struts_action", "/calendar/edit_event");
		editLink = HttpUtil.setParameter(editLink, PortalUtil.getPortletNamespace(PortletKeys.CALENDAR) + "redirect", themeDisplay.getURLCurrent());
		editLink = HttpUtil.setParameter(editLink, PortalUtil.getPortletNamespace(PortletKeys.CALENDAR) + "groupId", groupId);

		for (CalEvent calEvent : calEventList) {

			String content = calEvent.getDescription();

			if (content != null) {

				List<String> links = parseLinks(content, getLinks, getImages);

				if (links.size() > 0) {

					editLink = HttpUtil.setParameter(editLink, PortalUtil.getPortletNamespace(PortletKeys.CALENDAR) + "eventId", calEvent.getEventId());

					ContentLinks contentLinks = new ContentLinks();
					contentLinks.setClassName(calEvent.getModelClassName());
					contentLinks.setClassPK(calEvent.getEventId());
					contentLinks.setContentTitle(calEvent.getTitle());
					contentLinks.setContentEditLink(editLink);
					contentLinks.setModifiedDate(calEvent.getModifiedDate());
					
					_log.debug("Extracting links from calendar event " + calEvent.getEventId() + " - " + calEvent.getTitle());
					
					for (String link : links) {

						contentLinks.addLink(link);
					}
					
					contentLinksList.add(contentLinks);
				}
			}
		}

		return contentLinksList;
	}

	public static List<ContentLinks> getMBMessageLinks(long groupId, String languageId, ThemeDisplay themeDisplay, boolean getLinks, boolean getImages)
		throws Exception {

		_log.info("getMBMessageLinks for groupId " + String.valueOf(groupId));

		List<ContentLinks> contentLinksList = new ArrayList<ContentLinks>();

		List<MBMessage> messageList = MBMessageLocalServiceUtil.getGroupMessages(groupId, WorkflowConstants.STATUS_APPROVED, 0, -1);

		String editLink = themeDisplay.getURLControlPanel();
		editLink = HttpUtil.setParameter(editLink, "p_p_id", PortletKeys.MESSAGE_BOARDS_ADMIN);
		editLink = HttpUtil.setParameter(editLink, "p_p_lifecycle", "0");
		editLink = HttpUtil.setParameter(editLink, "p_p_state", "maximized");
		editLink = HttpUtil.setParameter(editLink, "p_p_mode", "view");
		editLink = HttpUtil.setParameter(editLink, PortalUtil.getPortletNamespace(PortletKeys.MESSAGE_BOARDS_ADMIN) + "struts_action", "/message_boards_admin/edit_message");
		editLink = HttpUtil.setParameter(editLink, PortalUtil.getPortletNamespace(PortletKeys.MESSAGE_BOARDS_ADMIN) + "redirect", themeDisplay.getURLCurrent());
		editLink = HttpUtil.setParameter(editLink, PortalUtil.getPortletNamespace(PortletKeys.MESSAGE_BOARDS_ADMIN) + "groupId", groupId);

		for (MBMessage message : messageList) {

			String content = message.getBody();
			if (message.isFormatBBCode()) {
				content = BBCodeTranslatorUtil.getHTML(message.getBody());
				content = StringUtil.replace(content, "@theme_images_path@/emoticons", themeDisplay.getPathThemeImages() + "/emoticons");
			}

			if (content != null) {

				List<String> links = parseLinks(content, getLinks, getImages);

				if (links.size() > 0) {

					editLink = HttpUtil.setParameter(editLink, PortalUtil.getPortletNamespace(PortletKeys.MESSAGE_BOARDS_ADMIN) + "messageId", message.getMessageId());

					ContentLinks contentLinks = new ContentLinks();
					contentLinks.setClassName(message.getModelClassName());
					contentLinks.setClassPK(message.getMessageId());
					contentLinks.setContentTitle(message.getSubject());
					contentLinks.setContentEditLink(editLink);
					contentLinks.setModifiedDate(message.getModifiedDate());
					contentLinks.setStatus(message.getStatus());
					
					_log.debug("Extracting links from message " + message.getMessageId() + " - " + message.getSubject());
					
					for (String link : links) {

						contentLinks.addLink(link);
					}
					
					contentLinksList.add(contentLinks);
				}
			}
		}

		return contentLinksList;
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
