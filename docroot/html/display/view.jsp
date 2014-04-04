<%@ include file="/html/init.jsp" %>

<p>
	Select the content types to process for links.</p>

<liferay-portlet:renderURL varImpl="extractLinksURL">
	<portlet:param name="mvcPath" value='<%= templatePath + "checker.jsp" %>' />
</liferay-portlet:renderURL>

<aui:form action="<%= extractLinksURL %>" method="get" name="fm0">
	<liferay-portlet:renderURLParams varImpl="extractLinksURL" />
	<aui:input name="redirect" type="hidden" value="<%= redirect %>" />
	<aui:fieldset>

		<aui:field-wrapper name="contentTypes" label="content-types">
			<aui:input inlineLabel="right" name="web-content" type="checkbox" />
			<!--
			<aui:input inlineLabel="right" name="calendar-events" type="checkbox" />
			<aui:input inlineLabel="right" name="message-board-messages" type="checkbox" />
			<aui:input inlineLabel="right" name="rss-subscriptions" type="checkbox" />
			<aui:input inlineLabel="right" name="wiki-pages" type="checkbox" />
			<aui:input inlineLabel="right" name="blog-entries" type="checkbox" />
			-->
		</aui:field-wrapper>


		<aui:button-row>
			<aui:button type="submit" value="process" />
		</aui:button-row>

	</aui:fieldset>
</aui:form>
