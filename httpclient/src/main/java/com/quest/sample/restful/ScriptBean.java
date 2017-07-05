package com.quest.sample.restful;

public class ScriptBean extends AbstractBean {
	private String script;
	private String cartridgeName;
	private String scopeObjectId;
	public String getScript() {
		return script;
	}
	public void setScript(String script) {
		this.script = script;
	}
	public String getCartridgeName() {
		return cartridgeName;
	}
	public void setCartridgeName(String cartridgeName) {
		this.cartridgeName = cartridgeName;
	}
	public String getScopeObjectId() {
		return scopeObjectId;
	}
	public void setScopeObjectId(String scopeObjectId) {
		this.scopeObjectId = scopeObjectId;
	}
}
