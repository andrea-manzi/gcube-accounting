package org.gcube.messaging.accounting.portalaccountingportlet.client.obj;

import com.google.gwt.editor.client.Editor.Path;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

public interface UserProperties extends PropertyAccess<User> {

	@Path("id")
	ModelKeyProvider<User> key();
	
	ValueProvider<User, String> name();

}
