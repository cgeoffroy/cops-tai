package org.tai.cops.occi.annotations;

import java.net.URI;

import javax.annotation.Nullable;

import com.google.common.base.Function;

public abstract class Transformations {

	public static final class Identity implements Function<String, String> {
		@Override
		@Nullable
		public String apply(@Nullable String input) {
			return input;
		}
		
	}
	
	public static final class StringToUri implements Function<String, URI> {
		@Override
		@Nullable
		public URI apply(@Nullable String input) {
			return URI.create(input);
		}		
	}
	
	public static final class StringToInteger implements Function<String, Integer> {
		@Override
		@Nullable
		public Integer apply(@Nullable String input) {
			return Integer.parseInt(input);
		}		
	}

}
