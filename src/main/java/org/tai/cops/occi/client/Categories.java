package org.tai.cops.occi.client;

import java.util.List;

import fj.F;
import fj.data.Option;

public final class Categories {
	
	public static Option<Category> findCategory(List<Category> categories, final String term) {
		fj.data.List<Category> u = fj.data.List.iterableList(categories);
		Option<Category> o = u.find(new F<Category, Boolean>() {		
			@Override
			public Boolean f(Category a) {
				return term.equals(a.getTerm());
			}
		});
		return o;
	}
	
}
