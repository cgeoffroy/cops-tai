package org.tai.cops.occi.client;

import java.net.URI;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;


public class Category {

	private final @Nonnull URI scheme;
	private final @Nonnull String term;
	private final @Nonnull String claz;
	
	private final String title;
	private final TypeIdentifier rel;
	private final URI location;
	private final String attributes;
	private final String actions;
	
	
	public Category(@Nonnull String term, @Nonnull URI scheme, @Nonnull String claz,
			String title, TypeIdentifier rel, URI location, String attributes,
			String actions) {
		this.term = term;
		this.scheme = scheme;
		this.claz = claz;
		this.title = title;
		this.rel = rel;
		this.location = location;
		this.attributes = attributes;
		this.actions = actions;
	}

	public String getRequestFilter() {
		return (String.format("%s; scheme=\"%s\"; class=%s; rel=\"%s\"",
					getTerm(), getScheme(), getClaz(), getRel()));
	}
	

	public URI getScheme() {
		return scheme;
	}


	public String getTerm() {
		return term;
	}


	public String getClaz() {
		return claz;
	}


	public String getTitle() {
		return title;
	}


	public TypeIdentifier getRel() {
		return rel;
	}


	public URI getLocation() {
		return location;
	}


	public String getAttributes() {
		return attributes;
	}


	public String getActions() {
		return actions;
	}
	
	/*public Category(Map<String, String> parsedValues) throws URISyntaxException {
		this(	parsedValues.get("occi.core.term"), 
				new URI(parsedValues.get("occi.core.scheme")),
				parsedValues.get("occi.core.class")	);	
	}*/
	
}
