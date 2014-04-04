<%@ include file="/html/init.jsp" %>

<%
boolean checkWebContent = ParamUtil.getBoolean(request, "web-content", false);
%>

<liferay-ui:header
	backURL="<%= redirect %>"
	title=""
/>

<c:if test="<%= checkWebContent %>" >
<h2>Web Content</h2>
<div id="web-content">
<table width="100%">
	<tr>
		<th>PK</th>
		<th>Title</th>
		<th>Status</th>
	</tr>
<%
	List<ContentLinks> contentLinksList = LinkCheckerUtil.getWebContentLinks(scopeGroupId, languagId, themeDisplay);

	for (ContentLinks contentLinks : contentLinksList) {
%>
	<tr>
		<td><%= contentLinks.getClassPK() %></td>
		<td><a href="<%= contentLinks.getContentEditLink() %>" target="_blank"><%= contentLinks.getContentTitle() %></a></td>
		<td><%= LanguageUtil.get(pageContext, WorkflowConstants.toLabel(contentLinks.getStatus())) %></td>
	</tr>
<%
		for (String link : contentLinks.getLinks()) {

			String linkShort = (link.length() > 150 ? link.substring(0, 150) + "..." : link);
%>
	<tr>
		<td>&nbsp;</td>
		<td colspan="2"><a href="<%= link %>" target="_blank"><%= linkShort %></a></td>
	</tr>
<%
		}
	}
%>
</table>
</div>
</c:if>
