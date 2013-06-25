package org.tai.cops;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import occi.lexpar.OcciParser;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.google.common.io.CharStreams;

import static org.testng.Assert.assertEquals;

public class TestsParsing {

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
		
		List<Map<String, String>> cats = op.category();
		assertEquals(cats.size(), 1);
		
		Map<String, String> cat = cats.get(0);
		assertEquals(true, cat.containsKey("occi.core.term"));
		assertEquals(cat.get("occi.core.term"), "publication");
		assertEquals(true, cat.containsKey("occi.core.scheme"));
		assertEquals(cat.get("occi.core.scheme"), "http://scheme.compatibleone.fr/scheme/compatible#");
		assertEquals(true, cat.containsKey("occi.core.class"));
		assertEquals(cat.get("occi.core.class"), "kind");
		assertEquals(true, cat.containsKey("occi.core.rel"));
		assertEquals(cat.get("occi.core.rel"), "http://scheme.ogf.org/occi/resource#");
		
		assertEquals(true, cat.containsKey("occi.core.attributes"));
		assertEquals(cat.get("occi.core.attributes"), "occi.publication.rating occi.publication.what occi.publication.remote occi.publication.zone occi.publication.pid occi.publication.who occi.publication.when occi.publication.uptime occi.publication.state occi.publication.pass occi.publication.where occi.publication.why occi.publication.identity occi.publication.operator occi.publication.price");
		
		assertEquals(true, cat.containsKey("occi.core.actions"));
		assertEquals(cat.get("occi.core.actions"), "http://scheme.compatibleone.fr/scheme/compatible/publication/action#DELETE http://scheme.compatibleone.fr/scheme/compatible/publication/action#suspend http://scheme.compatibleone.fr/scheme/compatible/publication/action#restart");
		
		assertEquals(true, cat.containsKey("occi.core.location"));
		assertEquals(cat.get("occi.core.location"), "/publication/");
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
	
}
