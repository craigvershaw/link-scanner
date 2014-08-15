<%@ include file="/html/init.jsp" %>

<%
String contentType = ParamUtil.getString(request, "content-type", "web-content");

boolean checkLinks = ParamUtil.getBoolean(request, "check-links", true);
boolean checkImages = ParamUtil.getBoolean(request, "check-images", false);

String checkType = LinkCheckerConstants.linkImagesLabel(checkLinks, checkImages);

List<ContentLinks> contentLinksList = LinkCheckerUtil.getContentLinks(contentType, scopeGroupId, languageId, themeDisplay, checkLinks, checkImages);

int checkCount = 0;

for (ContentLinks contentLinks : contentLinksList) {
	checkCount = checkCount + contentLinks.getLinksSize();
}

boolean rowAlt = true;
%>

<liferay-ui:header
	backURL="<%= redirect %>"
	title="<%= contentType %>"
/>

<c:choose>
	<c:when test='<%= !(checkCount > 0) %>'>

		<div class="portlet-msg-info">
			<liferay-ui:message arguments="<%= contentType %>" key="no-links-were-found-for-x" />
		</div>
		
	</c:when>
	<c:otherwise>

<div class="lfr-search-container ">
	<div class="taglib-search-iterator-page-iterator-top">
		<div class="taglib-page-iterator" id="<portlet:namespace/>SearchContainerPageIteratorTop">
			<div class="search-results">Checking <%= checkCount %> <liferay-ui:message key="<%= checkType %>" /> for <%= contentLinksList.size() %> <liferay-ui:message key="<%= contentType %>" /> items.</div>

			<div id="linkCheckerProgressBarContainer">
				<div class="linkCheckerProgressBar"></div>
			</div>

			<liferay-ui:panel collapsible="<%= true %>" extended="<%= true %>" id="linkCheckerOptions" persistState="<%= true %>" title="options">
				<liferay-ui:message key="result-hover-description" />
				
				<div class="link-checker-legend">
					<strong>Legend</strong>
					<div class="link-checker-result-legend link-checker-unchecked"><liferay-ui:message key="link-unchecked" /></div>
					<div class="link-checker-result-legend link-checker-success"><liferay-ui:message key="link-success" /></div>
					<div class="link-checker-result-legend link-checker-redirect"><liferay-ui:message key="link-redirect" /></div>
					<div class="link-checker-result-legend link-checker-error"><liferay-ui:message key="link-error" /></div>
				</div>
			</liferay-ui:panel>
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
						<th class="col-2 last" id="<portlet:namespace/>SearchContainer_col-title-link">
							<span class="result-column-name">Title / Link(s)</span>
						</th>
					</tr>

<%
	for (ContentLinks contentLinks : contentLinksList) {

		rowAlt = !rowAlt;
%>
					<tr class="link-checker-row-content results-row <%= (rowAlt?"portlet-section-alternate alt":"portlet-section-body") %>">
						<td class="align-left col-1 first valign-middle" colspan="1" headers="<portlet:namespace/>SearchContainer_col-result">&nbsp;</td>
						<td class="align-left col-2 last valign-middle" colspan="1" headers="<portlet:namespace/>SearchContainer_col-title-link">
							<liferay-ui:icon
								image="edit"
								label="<%= true %>"
								message="<%= contentLinks.getContentTitle() %>"
								target="_blank"
								url="<%= contentLinks.getContentEditLink() %>"
							/>
						</td>
					</tr>
<%
		for (String link : contentLinks.getLinks()) {

			rowAlt = !rowAlt;
			String linkShort = (link.length() > 150 ? link.substring(0, 150) + "..." : link);
%>
					<tr class="link-checker-row-link results-row <%= (rowAlt?"portlet-section-alternate alt":"portlet-section-body") %>">
						<td class="align-left col-1 first valign-middle" colspan="1" headers="<portlet:namespace/>SearchContainer_col-result">
							<div class="link-checker-result link-checker-unchecked" title="" data-link="<%= link %>" data-isportal="<%= LinkCheckerUtil.isPortalLink(link, themeDisplay) %>"></div>
						</td>
						<td class="align-left col-2 last valign-middle" colspan="1" headers="<portlet:namespace/>SearchContainer_col-title-link">
							<a href="<%= link %>" target="_blank" class="link-checker-link"><%= linkShort %></a>
						</td>
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
				if (node.attr('data-isportal') == 'true') {
					A.io.request(
						node.attr('data-link'),
						{
							on: {
								failure: function(event, id, obj) {
									pbIncrement();
									node.removeClass('link-checker-unchecked');
									node.removeClass('link-checker-success');
									node.addClass('link-checker-error');
									node.attr('title','XMLRPC Failed');
								},
								success: function(event, id, obj) {
									pbIncrement();
									node.removeClass('link-checker-unchecked');
									node.removeClass('link-checker-error');
									node.addClass('link-checker-success');
									node.attr('title','XMLRPC Success');
								}
							}
						}
					);
				} else {
					encodelink=encodeURIComponent(node.attr('data-link'));
					A.io.request(
						'/api/jsonws/link-checker-portlet.linkcheckerurlstatus/get-response?url=' + encodelink,
						{
							dataType: 'json',
							on: {
								failure: function(event, id, obj) {
									pbIncrement();
									node.removeClass('link-checker-unchecked');
									node.removeClass('link-checker-success');
									node.addClass('link-checker-error');
									node.attr('title','Web Service Request Failed');
								},
								success: function(event, id, obj) {
									pbIncrement();
									var response = this.get('responseData');
									var exception = response.exception;
									
									if (!exception) {
										if (response[0] >= 100 && response[0] < 300) {
											node.removeClass('link-checker-unchecked');
											node.removeClass('link-checker-error');
											node.removeClass('link-checker-redirect');
											node.addClass('link-checker-success');
										} else if (response[0] >= 300 && response[0] < 400) {
											node.removeClass('link-checker-unchecked');
											node.removeClass('link-checker-error');
											node.removeClass('link-checker-success');
											node.addClass('link-checker-redirect');
										} else {
											node.removeClass('link-checker-unchecked');
											node.removeClass('link-checker-success');
											node.removeClass('link-checker-redirect');
											node.addClass('link-checker-error');
										}
										node.attr('title','WS: ' + response[0] + ' - ' + response[1]);
									} else {
										node.removeClass('link-checker-unchecked');
										node.removeClass('link-checker-success');
										node.removeClass('link-checker-redirect');
										node.addClass('link-checker-error');
										node.attr('title','WS: ' + exception);
									}
								}
							}
						}
					);
				}
			});
		},
		['aui-io']
	);

	var progressBarTotal = <%= checkCount %>;
	var progressBarCount = 0;
	var progressBarPercent = 0;
	var progressBar
	
	function pbIncrement() {
		++progressBarCount;
		progressBarPercent = (progressBarCount / progressBarTotal) * 100;
		progressBar.set('label', 'Checking... ' + Math.round(progressBarPercent) + '%');
		progressBar.set('value', Math.round(progressBarPercent));
	}
	
	AUI().ready('aui-tooltip', 
		'aui-io-plugin',
		'aui-progressbar',
		function(A) {
			
			var tipresult = new A.Tooltip({
				trigger: '.link-checker-result',
				align: { points: [ 'br', 'tl' ] },
				title: true
			})
			.render();
			
			progressBar = new A.ProgressBar(
				{
					boundingBox: '#linkCheckerProgressBarContainer',
					contentBox: '.linkCheckerProgressBar',
					label: 'Checking...',
					on: {
						complete: function(e) {
							this.set('label', 'Complete! 100%');
						}
					},
					value: progressBarCount
				}
			).render();
	<portlet:namespace />checkLinks();
		}
	);

</aui:script>

	</c:otherwise>
</c:choose>
