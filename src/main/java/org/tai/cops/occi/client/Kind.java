package org.tai.cops.occi.client;

import java.net.URI;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.Sets;

public class Kind extends Category {

	private final @Nonnull Set<TypeIdentifier> actions;
	private final @Nullable TypeIdentifier related;
	private final @Nullable Class<? extends Entity> entity_type;
	//private final @Nonnull Set<Entity> entities; //TODO : how to handle this ?
	
	public Kind(@Nonnull String term, @Nonnull URI scheme, String title,
			URI location, String attributes, List<TypeIdentifier> actions, @Nullable TypeIdentifier rel) {
		super(term, scheme, "kind", title, location, attributes);
		this.actions = Sets.newHashSet();
		if (null != actions)
			this.actions.addAll(actions);
		this.related = rel;
		this.entity_type = Entity.class;
	}

	public Set<TypeIdentifier> getActions() {
		return actions;
	}

	public TypeIdentifier getRelated() {
		return related;
	}

}
