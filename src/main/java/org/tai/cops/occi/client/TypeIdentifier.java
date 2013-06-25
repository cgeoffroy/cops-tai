package org.tai.cops.occi.client;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.io.FilenameUtils;

public class TypeIdentifier {
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