package org.gcube.messaging.accounting.portalaccountingportlet.client.obj;

import com.google.gwt.editor.client.Editor.Path;
import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

public interface StatisticsProperties extends PropertyAccess<Statistics> {

	@Path("id")
	ModelKeyProvider<Statistics> key();
	
	ValueProvider<Statistics, String> user();
	ValueProvider<Statistics, String> type();
	ValueProvider<Statistics, String> vre();
	ValueProvider<Statistics, String> date();
	ValueProvider<Statistics, String> CNT();	
}
