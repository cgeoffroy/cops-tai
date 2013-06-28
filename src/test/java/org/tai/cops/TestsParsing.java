package org.tai.cops;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import occi.lexpar.OcciParser;

import org.tai.cops.occi.client.Category;
import org.tai.cops.occi.client.Publication;
import org.tai.cops.occi.client.Resource;
import org.tai.cops.occi.client.TypeIdentifier;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.google.common.io.CharStreams;

import static org.testng.Assert.assertEquals;

public class TestsParsing {
	
	@Test void a() throws URISyntaxException {
		HashMap<String, String> m = new HashMap<>();
		m.put("occi.core.id", "http://www.truc.machin:465");
		m.put("occi.core.summary", "un petit résumé");
		Resource r = new Resource(m);
		
		assertEquals(r.getId(), new URI("http://www.truc.machin:465"));
		assertEquals(r.getSummary(), "un petit résumé");
	}
	
	@Test(expectedExceptions=RuntimeException.class, expectedExceptionsMessageRegExp="the '.*' field is mandatory")
	public void b() throws URISyntaxException {
		new Publication(new HashMap<String, String>());
	}
	
	@Test
	public void f() throws Exception {
		String input = "Category: publication; "
				+ "scheme=\"http://scheme.compatibleone.fr/scheme/compatible#\"; "
				+ "class=kind; "
				+ "rel=\"http://scheme.ogf.org/occi/resource#\"; "
				+ "attributes=\"occi.publication.rating occi.publication.what occi.publication.remote occi.publication.zone occi.publication.pid occi.publication.who occi.publication.when occi.publication.uptime occi.publication.state occi.publication.pass occi.publication.where occi.publication.why occi.publication.identity occi.publication.operator occi.publication.price\"; "
				+ "actions=\"http://scheme.compatibleone.fr/scheme/compatible/publication/action#DELETE http://scheme.compatibleone.fr/scheme/compatible/publication/action#suspend http://scheme.compatibleone.fr/scheme/compatible/publication/action#restart\"; "
				+ "location=\"/publication/\";";
		
		OcciParser op = OcciParser.getParser(input);
		
		List<Category> cats = op.category();
		assertEquals(cats.size(), 1);
		
		Category cat = cats.get(0);
		assertEquals(cat.getTerm(), "publication");
		assertEquals(cat.getScheme(), new URI("http://scheme.compatibleone.fr/scheme/compatible#"));
		assertEquals(cat.getClaz(), "kind");
		assertEquals(cat.getRel(), new TypeIdentifier(new URI("http://scheme.ogf.org/occi/resource#")));
		assertEquals(cat.getAttributes(), "occi.publication.rating occi.publication.what occi.publication.remote occi.publication.zone occi.publication.pid occi.publication.who occi.publication.when occi.publication.uptime occi.publication.state occi.publication.pass occi.publication.where occi.publication.why occi.publication.identity occi.publication.operator occi.publication.price");
		assertEquals(cat.getActions(), "http://scheme.compatibleone.fr/scheme/compatible/publication/action#DELETE http://scheme.compatibleone.fr/scheme/compatible/publication/action#suspend http://scheme.compatibleone.fr/scheme/compatible/publication/action#restart");
		assertEquals(cat.getLocation(), new URI("/publication/"));
	}
	
	@Test
	public void g() throws Exception {
		InputStream in = ClassLoader.getSystemClassLoader().getResourceAsStream("i.txt");
		Assert.assertNotNull(in);
		String input = CharStreams.toString(new InputStreamReader(in));
		OcciParser op = OcciParser.getParser(input);
		
		Map<String, ArrayList<String>> hds = op.headers();
		
		assertEquals(hds.get(OcciParser.occi_categories).size(), 15);
	}
	
	@Test
	public void h() throws Exception {
		String input = "X-OCCI-Location: http://127.0.0.1:8086/publication/93676c24-de2f-4731-83bd-24bc8daaf448,"
				+ "http://127.0.0.1:8086/publication/93676c24-de2f-4731-83bd-24bc8daaf448";
		
		OcciParser op = OcciParser.getParser(input);
		
		List<URL> cats = op.location();
		assertEquals(cats.size(), 2);
		assertEquals(cats.get(0).toString(), "http://127.0.0.1:8086/publication/93676c24-de2f-4731-83bd-24bc8daaf448");
		assertEquals(cats.get(1).toString(), "http://127.0.0.1:8086/publication/93676c24-de2f-4731-83bd-24bc8daaf448");
	}
	
}
