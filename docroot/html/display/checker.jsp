<%@ include file="/html/init.jsp" %>

<%
String contentType = ParamUtil.getString(request, "content-type", "web-content");

boolean checkLinks = ParamUtil.getBoolean(request, "check-links", true);
boolean checkImages = ParamUtil.getBoolean(request, "check-images", false);

String checkType = "Links";
if (!checkLinks && !checkImages) {
	checkLinks = true;
}
else if (!checkLinks && checkImages) {
	checkType = "Images";	
}
else if (checkLinks && checkImages) {
	checkType = "Links and Images";	
}

boolean rowAlt = true;

List<ContentLinks> contentLinksList = LinkCheckerUtil.getContentLinks(contentType, scopeGroupId, languageId, themeDisplay, checkLinks, checkImages);
%>

<liferay-ui:header
	backURL="<%= redirect %>"
	title="<%= contentType %>"
/>

<div class="lfr-search-container ">
	<div class="taglib-search-iterator-page-iterator-top">
		<div class="taglib-page-iterator" id="<portlet:namespace/>SearchContainerPageIteratorTop">
			<div class="search-results">Checking <%= checkType %> for <%= contentLinksList.size() %> <liferay-ui:message key="<%= contentType %>" /> items.</div>
		</div>
	</div>

	<div id="<portlet:namespace/>SearchContainer" class="yui3-widget aui-component aui-searchcontainer">
		<div class="results-grid aui-searchcontainer-content" id="<portlet:namespace/>SearchContainerSearchContainer">
			<table class="taglib-search-iterator" data-searchcontainerid="<portlet:namespace/>SearchContainer">
				<tbody>
					<tr class="portlet-section-header results-header">
						<th class="col-1 first" id="<portlet:namespace/>SearchContainer_col-result">
							<span class="result-column-name">Result</span>
						</th>
						<th class="col-2" id="<portlet:namespace/>SearchContainer_col-title-link">
							<span class="result-column-name">Title / Link(s)</span>
						</th>
						<th class="col-3 last" id="<portlet:namespace/>SearchContainer_col-status">
							<span class="result-column-name">Status</span>
						</th>
					</tr>

<%
	for (ContentLinks contentLinks : contentLinksList) {

		rowAlt = !rowAlt;
%>
					<tr class="link-checker-row-content results-row <%= (rowAlt?"portlet-section-alternate alt":"portlet-section-body") %>">
						<td class="align-left col-1 first valign-middle" colspan="1" headers="<portlet:namespace/>SearchContainer_col-result">&nbsp;</td>
						<td class="align-left col-2 valign-middle" colspan="1" headers="<portlet:namespace/>SearchContainer_col-title-link">
							<%= contentLinks.getContentTitle() %>
							<liferay-ui:icon
								image="edit"
								message="edit-content"
								target="_blank"
								url="<%= contentLinks.getContentEditLink() %>"
							/>
						</td>
						<td class="align-left col-3 last valign-middle" colspan="1" headers="<portlet:namespace/>SearchContainer_col-status">
							<%= LanguageUtil.get(pageContext, WorkflowConstants.toLabel(contentLinks.getStatus())) %>
						</td>
					</tr>
<%
		for (String link : contentLinks.getLinks()) {

			rowAlt = !rowAlt;
			String linkShort = (link.length() > 150 ? link.substring(0, 150) + "..." : link);
%>
					<tr class="link-checker-row-link results-row <%= (rowAlt?"portlet-section-alternate alt":"portlet-section-body") %>">
						<td class="align-left col-1 first valign-middle" colspan="1" headers="<portlet:namespace/>SearchContainer_col-result">
							<div class="link-checker-result link-checker-unchecked" data-link="<%= link %>"></div>
						</td>
						<td class="align-left col-2 valign-middle" colspan="1" headers="<portlet:namespace/>SearchContainer_col-title-link">
							<a href="<%= link %>" target="_blank" class="link-checker-link"><%= linkShort %></a>
						</td>
						<td class="align-left col-3 last valign-middle" colspan="1" headers="<portlet:namespace/>SearchContainer_col-status">&nbsp;</td>
					</tr>
<%
		}
	}
%>
				</tbody>
			</table>
		</div>
	</div>
</div>

<aui:script>
	Liferay.provide(
		window,
		'<portlet:namespace />checkLinks',
		function() {
			var A = AUI();

			var links = A.all('.link-checker-result');

			links.each(function (node) {
				A.io.request(
					node.attr('data-link'),
					{
						on: {
							failure: function(event, id, obj) {
								node.removeClass('link-checker-unchecked');
								node.removeClass('link-checker-success');
								node.addClass('link-checker-error');
							},
							success: function(event, id, obj) {
								node.removeClass('link-checker-unchecked');
								node.removeClass('link-checker-error');
								node.addClass('link-checker-success');
							}
						}
					}
				);
			});
		},
		['aui-io']
	);

	<portlet:namespace />checkLinks();

</aui:script>
