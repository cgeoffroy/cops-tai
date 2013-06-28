package org.tai.cops.occi.client;

import java.lang.reflect.Field;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tai.cops.occi.annotations.Attribute;
import org.tai.cops.occi.annotations.Transformations;
import org.tai.cops.occi.enums.EMultiplicity;

import com.google.common.base.Function;

public abstract class Entity {
	private final static Logger logger = LoggerFactory.getLogger(Entity.class);
	
	@Attribute(name = "occi.core.id", multiplicity = EMultiplicity.ONE, trans = Transformations.StringToUri.class)
	private URI id;
	@Attribute(name = "occi.core.title")
	private String title;
	
	protected Entity(@Nonnull URI id, String title) {
		this.id = id;
		this.title = title;
	}
	
	protected Entity(Map<String, String> attributes) throws URISyntaxException {
		Class<?> c = this.getClass();
		while (! Entity.class.getSuperclass().equals(c)) {
			for (Field f : c.getDeclaredFields()) {
				logger.debug("checking field: {}", f);
				if (f.isAnnotationPresent(Attribute.class)) {
					Attribute a = f.getAnnotation(Attribute.class);
					String input = (String) attributes.get(a.name());
					logger.debug("input='{}'", input);
					if (EMultiplicity.ONE == a.multiplicity() && null == input) {
						throw new RuntimeException(String.format("the '%s' field is mandatory", a.name()));
					}
					try {
						Object o = null;
						if (null != input) {
							Function<String, ?> trans =  a.trans().newInstance();
							o = trans.apply(input);
						}
						f.setAccessible(true);
						f.set(this, o);
						f.setAccessible(false);
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
						assert false;
					} catch (InstantiationException e) {
						e.printStackTrace();
					}
				}
			}
			c = c.getSuperclass();
		}
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public URI getId() {
		return id;
	}
	
}
