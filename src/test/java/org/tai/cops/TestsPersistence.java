package org.tai.cops;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.List;

import org.tai.cops.concepts.Placement;
import org.tai.cops.server.persistence.DAO;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TestsPersistence {

	@Test
	public void f() throws MalformedURLException {
		Placement p = new Placement(Placement.identifyBy, URI.create("azertyuiop"), null, "un résumé", "riri");
		p.setAlgorithm(new URL("https://foo.bar/left#right"));
		DAO.save(p);
		p = new Placement(Placement.identifyBy, URI.create("544546"), null, null, "loulou");
		DAO.save(p);
		p = new Placement(Placement.identifyBy, URI.create("xxxx"), null, "truc", "riri");
		DAO.save(p);
		
		List<Placement> tmp = DAO.findByName("riri");
		Assert.assertEquals(tmp.size(), 2);
		
		Placement found = tmp.get(0);
		Assert.assertEquals(found.getSummary(), "un résumé");
		Assert.assertEquals(found.getAlgorithm(), new URL("https://foo.bar/left#right"));
	}

}
