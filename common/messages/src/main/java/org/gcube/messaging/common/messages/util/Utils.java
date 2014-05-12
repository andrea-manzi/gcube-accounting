package org.gcube.messaging.common.messages.util;

import org.gcube.common.scope.impl.ScopeBean;
import org.gcube.common.scope.impl.ScopeBean.Type;

public class Utils {

	/**
	 * replace the "." char to "_" in the given string
	 * @param input the input string
	 * @return the underscore
	 */
	public static String replaceUnderscore(String input) {
		return input.replaceAll("\\.", "_");
	}
	
	
	public static String getInfraScope(String scope){
		ScopeBean sc = new ScopeBean(scope);
		while (!sc.is(Type.INFRASTRUCTURE))
			sc= sc.enclosingScope();
		return sc.name();
		
	}
}
