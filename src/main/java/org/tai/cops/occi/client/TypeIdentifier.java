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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((scheme == null) ? 0 : scheme.hashCode());
		result = prime * result + ((term == null) ? 0 : term.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof TypeIdentifier))
			return false;
		TypeIdentifier other = (TypeIdentifier) obj;
		if (scheme == null) {
			if (other.scheme != null)
				return false;
		} else if (!scheme.equals(other.scheme))
			return false;
		if (term == null) {
			if (other.term != null)
				return false;
		} else if (!term.equals(other.term))
			return false;
		return true;
	}
	
	
}