<%@ include file="/html/init.jsp" %>

<%
Set<String> portalURLPrefixes = LinkScannerUtil.getPortalURLPrefixes(themeDisplay);
String[] portalURLPrefixesAdd = LinkScannerUtil.getPortalURLPrefixesAdd(company.getCompanyId());
%>

<liferay-portlet:renderURL portletConfiguration="true" var="portletURL">
	<portlet:param name="redirect" value="<%= redirect %>" />
</liferay-portlet:renderURL>

<liferay-portlet:actionURL portletConfiguration="true" var="configurationURL" />

<aui:form action="<%= configurationURL %>" method="post" name="fm" onSubmit='<%= "event.preventDefault(); " + renderResponse.getNamespace() + "saveConfiguration();" %>'>
	<aui:input name="<%= Constants.CMD %>" type="hidden" value="<%= Constants.UPDATE %>" />

	<h4><liferay-ui:message key="add-prefix-instructions" /></h4>

	<aui:fieldset cssClass="prefixes">

		<%
		if (portalURLPrefixesAdd.length == 0) {
			portalURLPrefixesAdd = new String[1];
			portalURLPrefixesAdd[0] = StringPool.BLANK;
		}

		for (int i = 0; i < portalURLPrefixesAdd.length; i++) {
		%>

			<div class="lfr-form-row lfr-form-row-inline">
				<div class="row-fields">
					<aui:input cssClass="lfr-input-text-container" label="prefix" name='<%= "portalURLPrefixesAdd" + i %>' value="<%= portalURLPrefixesAdd[i] %>" />
				</div>
			</div>

		<%
		}
		%>
		
	</aui:fieldset>

	<aui:button-row>
		<aui:button type="submit" />
	</aui:button-row>
</aui:form>

<aui:script>
	function <portlet:namespace />saveConfiguration() {

		submitForm(document.<portlet:namespace />fm);
	}
</aui:script>

<aui:script use="liferay-auto-fields">
	new Liferay.AutoFields(
		{
			contentBox: 'fieldset.prefixes',
			fieldIndexes: '<portlet:namespace />prefixIndexes'
		}
	).render();
</aui:script>
