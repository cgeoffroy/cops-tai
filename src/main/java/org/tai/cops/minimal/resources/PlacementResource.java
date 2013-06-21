package org.tai.cops.minimal.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/placement")
public class PlacementResource {
	@GET
	@Produces("text/occi")
	@Path("{who}")
	public Response sayGreeting(@PathParam("who") String name) {
		return	Response.status(201).entity("Greetings, " + name + "!").build();
	}
}
