package org.tai.cops.server.resources;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import occi.lexpar.OcciParser;

import org.antlr.runtime.RecognitionException;
import org.codehaus.jackson.map.ObjectMapper;
import org.hibernate.Session;
import org.tai.cops.concepts.Placement;
import org.tai.cops.server.persistence.HibernateUtils;

@Path("/placement")
public class PlacementResource {
	@GET
	@Produces("text/occi")
	@Path("{who}")
	public Response sayGreeting(@PathParam("who") String name) {
		return	Response.status(201).entity("Greetings, " + name + "!").build();
	}
	
	@javax.ws.rs.POST
	@Consumes("text/occi")
	@Produces("text/occi")
	public Response POST (@Context HttpHeaders headers) throws RecognitionException, Exception{
        Map<String, String> possibleAttributes = new HashMap<>();
        /* we fetch the first publication resource, and retrieve his 'Attribute' headers */
        for (String s : headers.getRequestHeader("X-OCCI-Attribute")) {
        	for (Entry<String, Object> z : OcciParser.getParser(s).attributes_attr().entrySet()) {
        		possibleAttributes.put(z.getKey(), (String) z.getValue());
        	}
        }
        ResponseBuilder result = Response.created(URI.create("ffjghuti/msdmfsmdf"));
        try {
        	Placement p = new Placement(Placement.identifyBy, possibleAttributes);
        	
            Session session = HibernateUtils.getSessionFactory().openSession();          
            session.beginTransaction();
            session.save(p);
            session.getTransaction().commit();
            session.close();
            
           	result = result.status(201).entity("OK" + " " + new ObjectMapper().writeValueAsString(p));
        } catch (URISyntaxException | RuntimeException z) {
        	result = result.status(400).entity("KO");
        }
        return result.build();
	}
}
