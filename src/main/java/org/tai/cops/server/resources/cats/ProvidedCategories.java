package org.tai.cops.server.resources.cats;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.tai.cops.occi.client.Category;
import org.tai.cops.occi.client.Kind;
import org.tai.cops.occi.client.TypeIdentifier;

import fj.P2;

@Path("/-/")
@Consumes("text/occi")
@Produces("text/occi")
public class ProvidedCategories {

	private final static Kind placementKind =
			new Kind("placement", URI.create("http://schemas.tai.org/infrastructure#"), "Placement category",
					URI.create("/placement"),
					"occi.placement.name",
					Arrays.asList(new TypeIdentifier(URI.create("http://schemas.tai.org/infrastructure/placement/action#choose"))),
					new TypeIdentifier(URI.create("http://schemas.ogf.org/occi/core#resource")));
	
	private final static List<Category> categories = Arrays.asList((Category) placementKind);
	
	@javax.ws.rs.GET
	public Response GET() {
		ResponseBuilder resb = Response.noContent();
		for(Category c : categories) {
			P2<String, String> tuple = c.toFullOcciRendering();
			resb = resb.header(tuple._1(), tuple._2());
		}
		resb = resb.status(200).entity("OK");
		return resb.build();
	}

}
