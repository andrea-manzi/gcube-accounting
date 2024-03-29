package org.gcube.messaging.accounting.portalaccountingportlet.client.obj;

import com.google.gwt.editor.client.Editor.Path;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;


public interface RecordTypeProperties extends PropertyAccess<RecordType> {

	@Path("id")
	ModelKeyProvider<RecordType> key();
	
	ValueProvider<RecordType, String> name();

}