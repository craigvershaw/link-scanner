package edu.nps.portlet.linkscanner;

import com.liferay.portal.kernel.portlet.DefaultConfigurationAction;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;

public class ConfigurationActionImpl extends DefaultConfigurationAction {

	@Override
	public void processAction(
			PortletConfig portletConfig, ActionRequest actionRequest,
			ActionResponse actionResponse)
		throws Exception {

		int[] prefixIndexes = StringUtil.split(
			ParamUtil.getString(actionRequest, "prefixIndexes"), 0);

		Set<String> prefixSet = new LinkedHashSet<String>();

		for (int prefixIndex : prefixIndexes) {
			String prefix = ParamUtil.getString(
				actionRequest, "portalURLPrefixesAdd" + prefixIndex);

			if (Validator.isNull(prefix)) {
				continue;
			}

			prefixSet.add(prefix);
		}

		String[] portalURLPrefixesAdd = new String[prefixSet.size()];

		int i = 0;

		for (String prefix : prefixSet) {
			portalURLPrefixesAdd[i] = prefix;

			i++;
		}

		setPreference(actionRequest, "portalURLPrefixesAdd", portalURLPrefixesAdd);

		super.processAction(portletConfig, actionRequest, actionResponse);
	}

}
