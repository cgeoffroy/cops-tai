package org.tai.cops.occi.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

import org.tai.cops.occi.enums.EMultiplicity;

import com.google.common.base.Function;

@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Attribute {
	
	String name();
	
	EMultiplicity multiplicity() default EMultiplicity.ZERO_ONE;
	
	boolean mutable() default true;
	
	boolean discoverable() default true;
	
	static class R {
		
	}
	
	Class<? extends Function<String, ?>> trans() default Transformations.Identity.class;
}
