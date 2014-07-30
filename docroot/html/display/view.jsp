<%@ include file="/html/init.jsp" %>

<p>
	Select the content type to process for links.</p>

<liferay-portlet:renderURL varImpl="extractLinksURL">
	<portlet:param name="mvcPath" value='<%= templatePath + "checker.jsp" %>' />
</liferay-portlet:renderURL>

<aui:form action="<%= extractLinksURL %>" method="get" name="fm">
	<liferay-portlet:renderURLParams varImpl="extractLinksURL" />
	<aui:input name="redirect" type="hidden" value="<%= redirect %>" />
	<aui:fieldset>

		<aui:field-wrapper name="content-types" label="content-types">
			<aui:select name="content-type" inlineLabel="right" label="">
				<aui:option label="web-content" selected="<%= true %>" />
				<aui:option label="wiki-pages" />
				<aui:option label="rss-portlet-subscriptions" />
			</aui:select>
			<!--
			<aui:input inlineLabel="right" name="calendar-events" type="checkbox" />
			<aui:input inlineLabel="right" name="message-board-messages" type="checkbox" />
			<aui:input inlineLabel="right" name="wiki-pages" type="checkbox" />
			<aui:input inlineLabel="right" name="blog-entries" type="checkbox" />
			-->
		</aui:field-wrapper>

		<liferay-ui:panel collapsible="<%= true %>" extended="<%= false %>" id="linkCheckerAdvanced" persistState="<%= true %>" title="advanced">
			<aui:input inlineLabel="right" name="check-links" type="checkbox" checked="<%= true %>" />
			<aui:input inlineLabel="right" name="check-images" type="checkbox" />
		</liferay-ui:panel>

		<aui:button-row>
			<aui:button onClick='<%= renderResponse.getNamespace() + "extractLinks();" %>' value="process" />
		</aui:button-row>

	</aui:fieldset>
</aui:form>

<aui:script>
	function <portlet:namespace />extractLinks() {
		submitForm(document.<portlet:namespace />fm);
	}

	AUI().use('aui-form-validator', 'aui-overlay-context-panel', function(A) {

		// Extending Alloy Default values for FormValidator RULES
		var DEFAULTS_FORM_VALIDATOR = A.config.FormValidator;
		A.mix(
  			DEFAULTS_FORM_VALIDATOR.RULES,
			{
				requiredCheckboxByContainer:function (val, container, ruleValue) {
					var counter = 0;
					var checkBoxes = container.all(':checkbox:checked');
					for (var i = 0, len = checkBoxes.length; i < len; i++ ) {
						if (checkBoxes[i] == 'true') { 
							counter++;
						};
					}
					return counter >= ruleValue;
				},
			},
			true
		);

		// Extending Alloy Default values for FormValidator STRINGS
		A.mix(
			DEFAULTS_FORM_VALIDATOR.STRINGS,
			{
				requiredCheckboxByContainer: '<liferay-ui:message key="required-at-least-one" />'
			},
			true
		);

		// Specify the form validation rules to be applied on this form
		var rules = {
			linkCheckerAdvanced: {
				requiredCheckboxByContainer: 1
			}
	    };

		// fieldStrings to override the standard error msgs
	    var fieldStrings = {
	    };

	    new A.FormValidator(
	      {
	        boundingBox: '#<portlet:namespace/>fm',
	        fieldStrings: fieldStrings,
	        rules: rules,
	        showAllMessages: true
	      }
	    );
	});		
</aui:script>
