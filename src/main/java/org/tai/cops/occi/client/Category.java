package org.tai.cops.occi.client;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;

import fj.P;
import fj.P2;


public class Category {

	private final @Nonnull URI scheme;
	private final @Nonnull String term;
	private final @Nonnull String claz;
	
	private final String title;
	private final URI location;
	private final Set<String> attributes;
	
	
	public Category(@Nonnull String term, @Nonnull URI scheme, @Nonnull String claz,
			String title, URI location, Set<String> attributes) {
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
	
	private List<P2<String, String>> toLittleOcciRenderParts() {
		List<P2<String, String>> l = new ArrayList<>(Arrays.asList(
				P.p("scheme", getScheme().toString()),
				P.p("class", getClaz())
				));
		return l;
	}
	
	protected List<P2<String, String>> toExtraOcciRenderParts() {
		List<P2<String, String>> l = Arrays.asList(
				P.p("title", getTitle()),
				P.p("location", getLocation().toString()),
				P.p("attributes", getAttributes())
				);
		return l;
	}
	
	private P2<String, String> toLowLevelOcciRendering(List<P2<String, String>> arg) {
		StringBuilder res = new StringBuilder();
		res.append(getTerm());
		for (P2<String, String> x : arg) {
			if (null != x._2())
				res.append("; ").append(x._1()).append("=\"").append(x._2()).append("\"");
		}
		return P.p("Category", res.toString());
	}
	
	public P2<String, String> toFullOcciRendering() {
		List<P2<String, String>> tmp = toLittleOcciRenderParts();
		tmp.addAll(toExtraOcciRenderParts());
		return toLowLevelOcciRendering(tmp);
	}
	
	public P2<String, String> toLittleOcciRendering() {
		return toLowLevelOcciRendering(toLittleOcciRenderParts());
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


	public Set<String> getAttributes() {
		return attributes;
	}

	public TypeIdentifier toTypeIdentifier() {
		return new TypeIdentifier(URI.create(scheme + term));
	}
	
}
