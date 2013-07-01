package org.tai.cops.occi.annotations;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

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
	
	public static final class StringToUrl implements Function<String, URL> {
		@Override
		@Nullable
		public URL apply(@Nullable String input) {
			try {
				return new URL(input);
			} catch (MalformedURLException e) {
				throw new IllegalArgumentException(e);
			}
		}		
	}

}
