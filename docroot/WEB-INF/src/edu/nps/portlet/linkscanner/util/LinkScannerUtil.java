package edu.nps.portlet.linkscanner.util;

import com.liferay.calendar.model.Calendar;
import com.liferay.calendar.model.CalendarBooking;
import com.liferay.calendar.service.CalendarBookingLocalServiceUtil;
import com.liferay.calendar.service.CalendarLocalServiceUtil;
import com.liferay.calendar.util.comparator.CalendarNameComparator;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.parsers.bbcode.BBCodeTranslatorUtil;
import com.liferay.portal.kernel.portlet.LiferayPortletRequest;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.portlet.LiferayWindowState;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.model.Layout;
import com.liferay.portal.model.LayoutTypePortlet;
import com.liferay.portal.model.Portlet;
import com.liferay.portal.security.permission.PermissionChecker;
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
import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;

public class LinkScannerUtil {

	public static Set<String> getPortalURLPrefixes(ThemeDisplay themeDisplay)
		throws SystemException, PortalException {

		Set<String> portalURLPrefixes = new HashSet<String>();
		portalURLPrefixes.add(themeDisplay.getURLPortal());
		portalURLPrefixes.add("/");
		portalURLPrefixes.add(".");

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
			companyId, companyId, ownerType, plid, LinkScannerConstants.LINK_SCANNER_DISPLAY);

	}

	public static List<ContentLinks> getContentLinks(
			String contentType, long groupId, 
			LiferayPortletRequest liferayPortletRequest,
			LiferayPortletResponse liferayPortletResponse,
			ThemeDisplay themeDisplay, 
			boolean getLinks,
			boolean getImages)
		throws Exception {

		if (contentType.equals("blog-entries")) {
			return getBlogLinks(groupId, liferayPortletRequest, liferayPortletResponse, themeDisplay, getLinks, getImages);
		}
		else if (contentType.equals("bookmarks")) {
			return getBookmarkLinks(groupId, liferayPortletRequest, liferayPortletResponse, themeDisplay, getLinks, getImages);
		}
		else if (contentType.equals("calendar-events")) {
			return getCalendarLinks(groupId, liferayPortletRequest, liferayPortletResponse, themeDisplay, getLinks, getImages);
		}
		else if (contentType.equals("message-board-messages")) {
			return getMBMessageLinks(groupId, liferayPortletRequest, liferayPortletResponse, themeDisplay, getLinks, getImages);
		}
		else if (contentType.equals("rss-portlet-subscriptions")) {
			return getRSSPortletLinks(groupId, liferayPortletRequest, liferayPortletResponse, themeDisplay, getLinks, getImages);
		}
		else if (contentType.equals("web-content")) {
			return getWebContentLinks(groupId, liferayPortletRequest, liferayPortletResponse, themeDisplay, getLinks, getImages);
		}
		else if (contentType.equals("wiki-pages")) {
			return getWikiContentLinks(groupId, liferayPortletRequest, liferayPortletResponse, themeDisplay, getLinks, getImages);
		}
		else {
			return null;
		}
	}

	public static List<ContentLinks> getBlogLinks(
			long groupId, 
			LiferayPortletRequest liferayPortletRequest,
			LiferayPortletResponse liferayPortletResponse,
			ThemeDisplay themeDisplay, 
			boolean getLinks,
			boolean getImages)
		throws Exception {

		_log.debug("getBlogLinks for groupId " + String.valueOf(groupId));

		List<ContentLinks> contentLinksList = new ArrayList<ContentLinks>();

		List<BlogsEntry> blogsEntryList = BlogsEntryLocalServiceUtil.getGroupEntries(groupId, WorkflowConstants.STATUS_APPROVED, QueryUtil.ALL_POS, QueryUtil.ALL_POS);

		PortletURL portletURL = liferayPortletResponse.createLiferayPortletURL(
				PortalUtil.getControlPanelPlid(liferayPortletRequest), 
				PortletKeys.BLOGS_ADMIN,
				PortletRequest.RENDER_PHASE);

		portletURL.setWindowState(LiferayWindowState.POP_UP);
		portletURL.setParameter("struts_action", "/blogs_admin/edit_entry");

		for (BlogsEntry blogsEntry : blogsEntryList) {
			
			if (blogsEntry.isInTrash() ||
					!hasPermissionView(groupId, blogsEntry.getModelClassName(), blogsEntry.getPrimaryKey(), themeDisplay)) {
				continue;
			}

			String content = blogsEntry.getContent();

			if (content != null) {

				List<String> links = parseLinks(content, getLinks, getImages);

				if (links.size() > 0) {

					portletURL.setParameter("entryId", String.valueOf(blogsEntry.getEntryId()));

					ContentLinks contentLinks = new ContentLinks();
					contentLinks.setClassName(blogsEntry.getModelClassName());
					contentLinks.setClassPK(blogsEntry.getEntryId());
					contentLinks.setContentTitle(blogsEntry.getTitle());
					contentLinks.setContentEditLink(portletURL.toString());
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

	public static List<ContentLinks> getBookmarkLinks(
			long groupId, 
			LiferayPortletRequest liferayPortletRequest,
			LiferayPortletResponse liferayPortletResponse,
			ThemeDisplay themeDisplay, 
			boolean getLinks,
			boolean getImages)
		throws Exception {

		_log.debug("getBookmarkLinks for groupId " + String.valueOf(groupId));

		List<ContentLinks> contentLinksList = new ArrayList<ContentLinks>();

		List<BookmarksEntry> bookmarksEntryList = BookmarksEntryLocalServiceUtil.getGroupEntries(groupId, QueryUtil.ALL_POS, QueryUtil.ALL_POS);

		PortletURL portletURL = liferayPortletResponse.createLiferayPortletURL(
				PortalUtil.getControlPanelPlid(liferayPortletRequest), 
				PortletKeys.BOOKMARKS,
				PortletRequest.RENDER_PHASE);

		portletURL.setWindowState(LiferayWindowState.POP_UP);
		portletURL.setParameter("struts_action", "/bookmarks/edit_entry");

		for (BookmarksEntry bookmarksEntry : bookmarksEntryList) {
			
			if (bookmarksEntry.isInTrash() ||
					!hasPermissionView(groupId, bookmarksEntry.getModelClassName(), bookmarksEntry.getPrimaryKey(), themeDisplay)) {
				continue;
			}

			String link = bookmarksEntry.getUrl();

			if (link != null) {

				portletURL.setParameter("folderId", String.valueOf(bookmarksEntry.getFolderId()));
				portletURL.setParameter("entryId", String.valueOf(bookmarksEntry.getEntryId()));

				ContentLinks contentLinks = new ContentLinks();
				contentLinks.setClassName(bookmarksEntry.getModelClassName());
				contentLinks.setClassPK(bookmarksEntry.getEntryId());
				contentLinks.setContentTitle(bookmarksEntry.getName());
				contentLinks.setContentEditLink(portletURL.toString());
				contentLinks.setModifiedDate(bookmarksEntry.getModifiedDate());
				
				_log.debug("Extracting link from bookmarks entry " + bookmarksEntry.getEntryId() + " - " + bookmarksEntry.getName());
				
				contentLinks.addLink(link);
				
				contentLinksList.add(contentLinks);
			}
		}

		return contentLinksList;
	}

	public static List<ContentLinks> getCalendarLinks(
			long groupId, 
			LiferayPortletRequest liferayPortletRequest,
			LiferayPortletResponse liferayPortletResponse,
			ThemeDisplay themeDisplay, 
			boolean getLinks,
			boolean getImages)
		throws Exception {

		_log.debug("getCalendarLinks for groupId " + String.valueOf(groupId));

		List<ContentLinks> contentLinksList = new ArrayList<ContentLinks>();

		List<CalendarBooking> calendarBookingList = new ArrayList<CalendarBooking>();
		List<Calendar> calendarList = CalendarLocalServiceUtil.search(themeDisplay.getCompanyId(), new long[] {groupId}, null, null, false, QueryUtil.ALL_POS, QueryUtil.ALL_POS, new CalendarNameComparator(true));

		for (Calendar cal : calendarList) {
			calendarBookingList.addAll(CalendarBookingLocalServiceUtil.getCalendarBookings(cal.getCalendarId()));
		}

		PortletURL portletURL = liferayPortletResponse.createLiferayPortletURL(
				PortalUtil.getControlPanelPlid(liferayPortletRequest), 
				LinkScannerConstants.CALENDAR,
				PortletRequest.RENDER_PHASE);

		portletURL.setWindowState(LiferayWindowState.POP_UP);
		portletURL.setParameter("mvcPath", "/edit_calendar_booking.jsp");

		for (CalendarBooking calendarBooking : calendarBookingList) {
			
			if (calendarBooking.isInTrash() ||
					!hasPermissionView(groupId, calendarBooking.getModelClassName(), calendarBooking.getPrimaryKey(), themeDisplay)) {
				continue;
			}

			String content = calendarBooking.getDescription(themeDisplay.getLocale());

			if (content != null) {

				List<String> links = parseLinks(content, getLinks, getImages);

				if (links.size() > 0) {

					portletURL.setParameter("calendarBookingId",String.valueOf(calendarBooking.getCalendarBookingId()));

					ContentLinks contentLinks = new ContentLinks();
					contentLinks.setClassName(calendarBooking.getModelClassName());
					contentLinks.setClassPK(calendarBooking.getCalendarBookingId());
					contentLinks.setContentTitle(calendarBooking.getTitle(themeDisplay.getLocale()));
					contentLinks.setContentEditLink(portletURL.toString());
					contentLinks.setModifiedDate(calendarBooking.getModifiedDate());
					
					_log.debug("Extracting links from calendar event " + calendarBooking.getCalendarBookingId() + " - " + calendarBooking.getTitle(themeDisplay.getLocale()));
					
					for (String link : links) {

						contentLinks.addLink(link);
					}
					
					contentLinksList.add(contentLinks);
				}
			}
		}

		return contentLinksList;
	}

	public static List<ContentLinks> getMBMessageLinks(
			long groupId, 
			LiferayPortletRequest liferayPortletRequest,
			LiferayPortletResponse liferayPortletResponse,
			ThemeDisplay themeDisplay, 
			boolean getLinks,
			boolean getImages)
		throws Exception {

		_log.debug("getMBMessageLinks for groupId " + String.valueOf(groupId));

		List<ContentLinks> contentLinksList = new ArrayList<ContentLinks>();

		List<MBMessage> messageList = MBMessageLocalServiceUtil.getGroupMessages(groupId, WorkflowConstants.STATUS_APPROVED, QueryUtil.ALL_POS, QueryUtil.ALL_POS);

		PortletURL portletURL = liferayPortletResponse.createLiferayPortletURL(
				PortalUtil.getControlPanelPlid(liferayPortletRequest), 
				PortletKeys.MESSAGE_BOARDS,
				PortletRequest.RENDER_PHASE);

		portletURL.setWindowState(LiferayWindowState.POP_UP);
		portletURL.setParameter("struts_action", "/message_boards/edit_message");

		for (MBMessage message : messageList) {
			
			if (message.isInTrash() ||
					!hasPermissionView(groupId, message.getModelClassName(), message.getPrimaryKey(), themeDisplay)) {
				continue;
			}

			String content = message.getBody();
			if (message.isFormatBBCode()) {
				content = BBCodeTranslatorUtil.getHTML(message.getBody());
				content = StringUtil.replace(content, "@theme_images_path@/emoticons", themeDisplay.getPathThemeImages() + "/emoticons");
			}

			if (content != null) {

				List<String> links = parseLinks(content, getLinks, getImages);

				if (links.size() > 0) {

					portletURL.setParameter("messageId", String.valueOf(message.getMessageId()));

					ContentLinks contentLinks = new ContentLinks();
					contentLinks.setClassName(message.getModelClassName());
					contentLinks.setClassPK(message.getMessageId());
					contentLinks.setContentTitle(message.getSubject());
					contentLinks.setContentEditLink(portletURL.toString());
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

	public static List<ContentLinks> getRSSPortletLinks(
			long groupId, 
			LiferayPortletRequest liferayPortletRequest,
			LiferayPortletResponse liferayPortletResponse,
			ThemeDisplay themeDisplay, 
			boolean getLinks,
			boolean getImages)
		throws Exception {

		_log.debug("getRSSPortletLinks for groupId " + String.valueOf(groupId));

		List<ContentLinks> contentLinksList = new ArrayList<ContentLinks>();

		List<Layout> allLayouts = new LinkedList<Layout>();
		
		allLayouts.addAll(LayoutLocalServiceUtil.getLayouts(themeDisplay.getScopeGroupId(), false));
		allLayouts.addAll(LayoutLocalServiceUtil.getLayouts(themeDisplay.getScopeGroupId(), true));
		
		List<Layout> sortedLayouts = new ArrayList<Layout>(allLayouts);
		Collections.sort(sortedLayouts, new LayoutComparator());

		for (Layout layout : allLayouts) {
			
			if (!hasPermissionView(groupId, layout.getModelClassName(), layout.getPrimaryKey(), themeDisplay)) {
				continue;
			}
			
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

	public static List<ContentLinks> getWebContentLinks(
			long groupId, 
			LiferayPortletRequest liferayPortletRequest,
			LiferayPortletResponse liferayPortletResponse,
			ThemeDisplay themeDisplay, 
			boolean getLinks,
			boolean getImages)
		throws Exception {

		_log.debug("getWebContentLinks for groupId " + String.valueOf(groupId));

		List<ContentLinks> contentLinksList = new ArrayList<ContentLinks>();

		List<JournalArticle> journalArticleList = JournalArticleLocalServiceUtil.getArticles(groupId);

		PortletURL portletURL = liferayPortletResponse.createLiferayPortletURL(
				PortalUtil.getControlPanelPlid(liferayPortletRequest), 
				PortletKeys.JOURNAL,
				PortletRequest.RENDER_PHASE);

		portletURL.setWindowState(LiferayWindowState.POP_UP);
		portletURL.setParameter("struts_action", "/journal/edit_article");

		for (JournalArticle journalArticle : journalArticleList) {

			if (journalArticle.isInTrash() ||
					!hasPermissionView(groupId, journalArticle.getModelClassName(), journalArticle.getPrimaryKey(), themeDisplay)) {
				continue;
			}

			if (JournalArticleLocalServiceUtil.isLatestVersion(journalArticle.getGroupId(), journalArticle.getArticleId(), journalArticle.getVersion())) {

				String content = JournalContentUtil.getContent(groupId, journalArticle.getArticleId(), null, null, themeDisplay.getLanguageId(), themeDisplay);

				if (content != null) {

					List<String> links = parseLinks(content, getLinks, getImages);

					if (links.size() > 0) {

						portletURL.setParameter("groupId", String.valueOf(journalArticle.getGroupId()));
						portletURL.setParameter("articleId", journalArticle.getArticleId());
						portletURL.setParameter("version", String.valueOf(journalArticle.getVersion()));

						ContentLinks contentLinks = new ContentLinks();
						contentLinks.setClassName(journalArticle.getModelClassName());
						contentLinks.setClassPK(journalArticle.getArticleId());
						contentLinks.setContentTitle(journalArticle.getTitle(themeDisplay.getLocale()));
						contentLinks.setContentEditLink(portletURL.toString());
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

	public static List<ContentLinks> getWikiContentLinks(
			long groupId, 
			LiferayPortletRequest liferayPortletRequest,
			LiferayPortletResponse liferayPortletResponse,
			ThemeDisplay themeDisplay, 
			boolean getLinks,
			boolean getImages)
		throws Exception {

		_log.debug("getWikiContentLinks for groupId " + String.valueOf(groupId));

		List<ContentLinks> contentLinksList = new ArrayList<ContentLinks>();

		List<WikiNode> wikiNodeList = WikiNodeLocalServiceUtil.getNodes(groupId);

		List<WikiPage> wikiPageList = new ArrayList<WikiPage>();

		for (WikiNode wikiNode : wikiNodeList) {

			wikiPageList.addAll(WikiPageLocalServiceUtil.getPages(wikiNode.getNodeId(), true, 0, WikiPageLocalServiceUtil.getPagesCount(wikiNode.getNodeId(), true)));
		}

		PortletURL portletURL = liferayPortletResponse.createLiferayPortletURL(
				PortalUtil.getControlPanelPlid(liferayPortletRequest), 
				PortletKeys.WIKI,
				PortletRequest.RENDER_PHASE);

		portletURL.setWindowState(LiferayWindowState.POP_UP);
		portletURL.setParameter("struts_action", "/wiki/edit_page");

		for (WikiPage wikiPage : wikiPageList) {

			if (wikiPage.isInTrash() ||
					!hasPermissionView(groupId, wikiPage.getModelClassName(), wikiPage.getResourcePrimKey(), themeDisplay)) {
				continue;
			}

			String content = wikiPage.getContent();

			if (content != null && wikiPage.getFormat().equals("html")) {

				List<String> links = parseLinks(content, getLinks, getImages);

				if (links.size() > 0) {

					portletURL.setParameter("nodeId", String.valueOf(wikiPage.getNodeId()));
					portletURL.setParameter("title", wikiPage.getTitle());

					ContentLinks contentLinks = new ContentLinks();
					contentLinks.setClassName(wikiPage.getModelClassName());
					contentLinks.setClassPK(String.valueOf(wikiPage.getPageId()));
					contentLinks.setContentTitle(wikiPage.getTitle());
					contentLinks.setContentEditLink(portletURL.toString());
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

	public static boolean hasPermission(long groupId, String name, String primKey, String actionId, ThemeDisplay themeDisplay) {
		
		PermissionChecker permissionChecker = themeDisplay.getPermissionChecker();
		if (permissionChecker.hasPermission(
				groupId, name, primKey, actionId)) {
			
			return true;
		}
		
		return false;
	}

	public static boolean hasPermissionView(long groupId, ContentLinks contentLinks, ThemeDisplay themeDisplay) {
		
		return hasPermission(
			groupId, contentLinks.getClassName(), contentLinks.getClassPK(), "VIEW", themeDisplay);
	}

	public static boolean hasPermissionView(long groupId, String name, String primKey, ThemeDisplay themeDisplay) {
		
		return hasPermission(
			groupId, name, primKey, "VIEW", themeDisplay);
	}

	public static boolean hasPermissionView(long groupId, String name, long primKey, ThemeDisplay themeDisplay) {
		
		return hasPermission(
			groupId, name, String.valueOf(primKey), "VIEW", themeDisplay);
	}

	public static boolean isPortalLink(String url, ThemeDisplay themeDisplay)
		throws SystemException, PortalException {

		if (url.startsWith("//" + themeDisplay.getServerName()))
			return true;

		for (String portalURLPrefix : getPortalURLPrefixes(themeDisplay)) {

			if (url.startsWith(portalURLPrefix) && 
					(!url.startsWith("//") || portalURLPrefix.startsWith("//")))
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

	private static Log _log = LogFactoryUtil.getLog(LinkScannerUtil.class);

}
