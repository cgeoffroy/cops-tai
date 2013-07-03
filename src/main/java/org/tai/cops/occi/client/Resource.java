package org.tai.cops.occi.client;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.persistence.Column;

import org.tai.cops.occi.annotations.Attribute;

public class Resource extends Entity {
	public static final TypeIdentifier identifyBy = new TypeIdentifier(URI.create("http://schemas.ogf.org/occi/core#entity"));
	
	@Column
	@Attribute(name = "occi.core.summary")
	private String summary;

	public Resource(@Nonnull TypeIdentifier kind, @Nonnull URI id, String title, String summary) {
		super(kind, id, title);
	}
	
	public Resource(@Nonnull TypeIdentifier kind, Map<String, String> attributes) throws URISyntaxException {
		super(kind, attributes);
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}
}
