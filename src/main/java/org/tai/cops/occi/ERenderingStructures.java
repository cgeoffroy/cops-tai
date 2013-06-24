package org.tai.cops.occi;


public enum ERenderingStructures {
	ECATEGORY("Category"),
	ELINK("Link"),
	EXOCCIATTR("X-OCCI-Attribute"),
	EXOCCILOC("X-OCCI-Location)");
	
	private final String value;
	
	private ERenderingStructures(String value) {
		this.value = value;
	}
	
	public String toHttpHeaders() {
		return this.value;
	}
	
	private static final boolean strictMode = false;
	
	public static ERenderingStructures fromHttpHeader(String value) {
		for (ERenderingStructures elt : ERenderingStructures.values()) {
			if (strictMode) {
				if (elt.toHttpHeaders().equals(value))
					return elt;
			} else {
				if (elt.toHttpHeaders().equalsIgnoreCase(value))
					return elt;
			}		
		}
		return null;
	}
	
}
