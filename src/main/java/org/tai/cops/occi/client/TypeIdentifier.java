package org.tai.cops.occi.client;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.io.FilenameUtils;

public class TypeIdentifier {
	public final URI scheme;
	public final String term;
	public final boolean isFragment;
	public final URI org;

	public TypeIdentifier(URI ti) {
		this.org = ti;
		URI tmpSch = null;
		String tmpTerm = null;
		isFragment = (null != ti.getFragment());
		try {
			if (isFragment) {
				tmpTerm = ti.getFragment();
				tmpSch = new URI(ti.getScheme(), ti.getUserInfo(), ti.getHost(), ti.getPort(),
						ti.getPath(), ti.getQuery(), null);
			} else {
				tmpTerm = FilenameUtils.getName(ti.toString());
				tmpSch = new URI(FilenameUtils.getFullPath(ti.toString()));
			}
		} catch (URISyntaxException e) {
			e.printStackTrace();
			assert false; // impossible, it was an URI at the beginning
		}
		this.term = tmpTerm;
		this.scheme = tmpSch;
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
	
	@Override
	public String toString() {
		return org.toString();
	}
	
	
}