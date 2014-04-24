package edu.nps.portlet.linkchecker.util;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.HttpUtil;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portal.util.PortletKeys;
import com.liferay.portlet.journal.model.JournalArticle;
import com.liferay.portlet.journalcontent.util.JournalContentUtil;
import com.liferay.portlet.journal.service.JournalArticleLocalServiceUtil;
import com.liferay.portlet.wiki.model.WikiNode;
import com.liferay.portlet.wiki.model.WikiPage;
import com.liferay.portlet.wiki.model.WikiPageConstants;
import com.liferay.portlet.wiki.model.WikiPageDisplay;
import com.liferay.portlet.wiki.service.WikiNodeLocalServiceUtil;
import com.liferay.portlet.wiki.service.WikiPageLocalServiceUtil;
import com.liferay.util.HTMLParser;

import edu.nps.portlet.linkchecker.util.ContentLinks;

import java.io.IOException;
import java.io.StringReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LinkCheckerUtil {

	public static List<ContentLinks> getContentLinks(String contentType, long groupId, String languageId, ThemeDisplay themeDisplay, boolean getLinks, boolean getImages)
		throws Exception {

		if (contentType.equals("web-content")) {
			return getWebContentLinks(groupId, languageId, themeDisplay, getLinks, getImages);
		}
		else if (contentType.equals("wiki-pages")) {
			return getWikiContentLinks(groupId, languageId, themeDisplay, getLinks, getImages);
		}
		else {
			return null;
		}
	}

	public static List<ContentLinks> getWebContentLinks(long groupId, String languageId, ThemeDisplay themeDisplay, boolean getLinks, boolean getImages)
		throws Exception {

		_log.info("getWebContentLinks for groupId " + String.valueOf(groupId));

		List<ContentLinks> contentLinksList = new ArrayList<ContentLinks>();

		List<JournalArticle> journalArticleList = JournalArticleLocalServiceUtil.getArticles(groupId, 1, 1000);

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
						contentLinks.setContentTitle(journalArticle.getTitle());
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

		List<WikiPage> wikiPageList = new ArrayList<WikiPage>(); //= WikiPageLocalServiceUtil.getPages(WikiPageConstants.DEFAULT_FORMAT);

		for (WikiNode wikiNode : wikiNodeList) {

			wikiPageList.addAll(WikiPageLocalServiceUtil.getPages(wikiNode.getNodeId(), true, 0, WikiPageLocalServiceUtil.getPagesCount(wikiNode.getNodeId(), true)));
		}

		String editLink = themeDisplay.getURLControlPanel();
		editLink = HttpUtil.setParameter(editLink, "p_p_id", PortletKeys.WIKI_ADMIN);
		editLink = HttpUtil.setParameter(editLink, "p_p_lifecycle", "0");
		editLink = HttpUtil.setParameter(editLink, "p_p_state", "maximized");
		editLink = HttpUtil.setParameter(editLink, "p_p_mode", "view");
		editLink = HttpUtil.setParameter(editLink, PortalUtil.getPortletNamespace(PortletKeys.JOURNAL) + "struts_action", "/journal/edit_article");
		editLink = HttpUtil.setParameter(editLink, PortalUtil.getPortletNamespace(PortletKeys.JOURNAL) + "redirect", themeDisplay.getURLCurrent());
		editLink = HttpUtil.setParameter(editLink, PortalUtil.getPortletNamespace(PortletKeys.JOURNAL) + "groupId", groupId);

		for (WikiPage wikiPage : wikiPageList) {

			String content = wikiPage.getContent();
			
			//WikiPageDisplay pageDisplay = WikiPageLocalServiceUtil.getPageDisplay(page, viewPageURL, editPageURL, attachmentURLPrefix);

			if (content != null && wikiPage.getFormat().equals("html")) {

				List<String> links = parseLinks(content, getLinks, getImages);

				if (links.size() > 0) {

					editLink = HttpUtil.setParameter(editLink, PortalUtil.getPortletNamespace(PortletKeys.WIKI_ADMIN) + "pageId", wikiPage.getPageId());

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

	private static Log _log = LogFactoryUtil.getLog(LinkCheckerUtil.class);

}
