package org.tai.cops;

import java.net.URI;
import java.net.URISyntaxException;

import org.testng.Assert;
import org.testng.annotations.Test;
import static org.testng.Assert.assertEquals;

public class TestsOthers {

	@Test
	public void aboutResolve_1() throws URISyntaxException {
		URI res = new URI("http://www.tai.org").resolve(new URI("/index.html"));
		assertEquals(res, new URI("http://www.tai.org/index.html"));
	}
	
	@Test
	public void aboutResolve_2() throws URISyntaxException {
		URI res = new URI("http://www.tai.org/").resolve(new URI("/index.html"));
		assertEquals(res, new URI("http://www.tai.org/index.html"));
	}
	
	@Test
	public void aboutResolve_3() throws URISyntaxException {
		URI res = new URI("http://www.tai.org").resolve(new URI("http://www.foo.com"));
		assertEquals(res, new URI("http://www.foo.com"));
	}

}
