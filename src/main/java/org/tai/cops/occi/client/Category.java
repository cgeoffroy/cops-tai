package org.tai.cops.occi.client;

import java.net.URI;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;


public class Category {

	private final @Nonnull URI scheme;
	private final @Nonnull String term;
	private final @Nonnull String claz;
	
	private String title;
	private TypeIdentifier rel;
	private URI location;
	private String attributes;
	private String actions;
	
	
	private Category(@Nonnull String term, @Nonnull URI scheme, @Nonnull String claz,
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
	
	/*public Category(Map<String, String> parsedValues) throws URISyntaxException {
		this(	parsedValues.get("occi.core.term"), 
				new URI(parsedValues.get("occi.core.scheme")),
				parsedValues.get("occi.core.class")	);	
	}*/
	
}
