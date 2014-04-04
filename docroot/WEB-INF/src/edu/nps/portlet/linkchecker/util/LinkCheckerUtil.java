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
import com.liferay.util.HTMLParser;

import edu.nps.portlet.linkchecker.util.ContentLinks;

import java.io.StringReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LinkCheckerUtil {

	public static List<ContentLinks> getWebContentLinks(long groupId, String languageId, ThemeDisplay themeDisplay)
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

					Reader contentReader = new StringReader(content);
					List<String> links = new HTMLParser(contentReader).getLinks();
					
					cleanLinks(links);
					
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

	private static void cleanLinks(List<String> links){

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
