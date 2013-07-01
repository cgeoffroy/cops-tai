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

	private interface ISomeInterface {
		public String getOcci();
	}
	private static enum Ee implements ISomeInterface  {
		A("aaaa"), B("bbbb");
		private static final String domain = "ping";
		private final String occi;
		private Ee(String s) {
			this.occi = domain + "." + s;
		}
		private String saved;
		public void setSaved(String s) { this.saved = s; }
		public String getSaved() { return this.saved; }
		@Override
		public String getOcci() {
			return occi;
		}
	}
	
	
	@Test
	private void aboutEnum_1() {
		Ee a1 = Ee.A;
		Ee a2 = Ee.B;
		assertEquals(a1.occi, "ping.aaaa");
		assertEquals(a2.occi, "ping.bbbb");
		ISomeInterface si = Ee.A;
		Assert.assertNotNull(si);
		si = a2;
		a1.setSaved("toto");
		a2.saved = "riri";
		assertEquals(a1.getSaved(), "toto");
		assertEquals(a2.saved, "riri");
	}
	
	@Test
	private void aboutEnum_2() {
		Ee a1 = Ee.A;
		Ee a2 = Ee.A;
		a1.setSaved("tutu");
		a2.saved = "fifi";
		Assert.assertNotEquals(a1.getSaved(), "tutu");
		assertEquals(a1.saved, "fifi");
		assertEquals(a2.saved, "fifi");
	}
	
}
