package org.tai.cops.occi.client;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import javax.annotation.Nonnull;

import org.tai.cops.occi.annotations.Attribute;

public class Resource extends Entity {
	@Attribute(name = "occi.core.summary")
	private String summary;

	public Resource(@Nonnull URI id, String title, String summary) {
		super(id, title);
	}
	
	public Resource(Map<String, String> attributes) throws URISyntaxException {
		super(attributes);
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}
}
