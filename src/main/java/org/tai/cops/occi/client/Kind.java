package org.tai.cops.occi.client;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import fj.P;
import fj.P2;

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

	@Override
	public String getRequestFilter() {
		return (String.format("%s; scheme=\"%s\"; class=%s; rel=\"%s\"",
				getTerm(), getScheme(), getClaz(), getRelated()));		
	};
	
	public Set<TypeIdentifier> getActions() {
		return actions;
	}

	public TypeIdentifier getRelated() {
		return related;
	}
	
	@Override
	protected List<P2<String, String>> toExtraOcciRenderParts() {
		List<P2<String, String>> l = new ArrayList<>(Arrays.asList(
				P.p("title", getTitle()),
				P.p("rel", getRelated().org.toString()),
				P.p("location", getLocation().toString()),
				P.p("attributes", getAttributes())
				));
		StringBuilder actionsList = new StringBuilder();
		int count = 0;
		for(TypeIdentifier act : actions) {
			count++;
			if (count > 1)
				actionsList.append(" ");
			actionsList.append(act.org.toString());
		}
		l.add(P.p("actions", actionsList.toString()));
		return l;
	}

}
