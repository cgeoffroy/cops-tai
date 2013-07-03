package org.tai.cops.occi.client;

import java.net.URI;

import javax.annotation.Nonnull;


public class Category {

	private final @Nonnull URI scheme;
	private final @Nonnull String term;
	private final @Nonnull String claz;
	
	private final String title;
	private final URI location;
	private final String attributes;
	
	
	public Category(@Nonnull String term, @Nonnull URI scheme, @Nonnull String claz,
			String title, URI location, String attributes) {
		this.term = term;
		this.scheme = scheme;
		this.claz = claz;
		this.title = title;
		this.location = location;
		this.attributes = attributes;
	}

	public String getRequestFilter() {
		return (String.format("%s; scheme=\"%s\"; class=%s",
					getTerm(), getScheme(), getClaz()));
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
	

	public URI getLocation() {
		return location;
	}


	public String getAttributes() {
		return attributes;
	}

	public TypeIdentifier toTypeIdentifier() {
		return new TypeIdentifier(URI.create(scheme + term));
	}
	
}
