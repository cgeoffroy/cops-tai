package org.tai.cops.occi.client;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import org.apache.commons.io.FilenameUtils;

public class Category {

	private final @Nonnull URI scheme;
	private final @Nonnull String term;
	private final @Nonnull String claz;
	
	private String title;
	private TypeIdentifier rel;
	private URI location;
	private Map<String, String> attributes;
	private List<TypeIdentifier> actions;
	
	
	private Category(@Nonnull String term, @Nonnull URI scheme, @Nonnull String claz,
			String title, URI rel, URI location, Map<String, String> attributes,
			List<TypeIdentifier> actions) {
		this.term = term;
		this.scheme = scheme;
		this.claz = claz;
		this.title = title;
		this.rel = new TypeIdentifier(rel);
		this.location = location;
		this.attributes = attributes;
		this.actions = actions;
	}
	
	/*public Category(Map<String, String> parsedValues) throws URISyntaxException {
		this(	parsedValues.get("occi.core.term"), 
				new URI(parsedValues.get("occi.core.scheme")),
				parsedValues.get("occi.core.class")	);	
	}*/
	
	private class TypeIdentifier {
		public final URI scheme;
		public final String term;
	
		public TypeIdentifier(URI ti) {
			this.term = FilenameUtils.getName(ti.toString());
			URI tmp = null;
			try {
				tmp = new URI(FilenameUtils.getFullPath(ti.toString()));
			} catch (URISyntaxException e) {
				e.printStackTrace();
				assert false; // impossible, it was an URI at the beginning
			}
			this.scheme = tmp;
		}
	}
	
}
