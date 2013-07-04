package org.tai.cops.server.resources;

import java.io.IOException;
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
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tai.cops.concepts.Placement;
import org.tai.cops.occi.OcciUtils;
import org.tai.cops.server.Main;
import org.tai.cops.server.persistence.DAO;
import org.tai.cops.server.persistence.HibernateUtils;

@Path("/placement")
@Consumes("text/occi")
@Produces("text/occi")
public class PlacementResource {
	final static Logger logger = LoggerFactory.getLogger(PlacementResource.class);
	
	@GET
	@Path("{who}")
	public Response sayGreeting(@PathParam("who") String name) throws JsonGenerationException, JsonMappingException, IOException {
		Placement target = null;
		try {
			target = DAO.findById(new URI(name));
		} catch (URISyntaxException e) {
			logger.warn("error while looking for a Placement", e);
		}
		Response res = null;
		if (null == target) {
			res = Response.status(401).build();
		} else {
			res = Response.status(200).entity("OK" + " " + new ObjectMapper().writeValueAsString(target)).build();
		}
		return	res;
	}
	
	@javax.ws.rs.POST
	public Response POST (@Context HttpHeaders headers) throws RecognitionException, Exception{
        ResponseBuilder result = null;
        try {
        	Placement p = new Placement(Placement.identifyBy, OcciUtils.findOcciAttributes(headers));
        	
        	DAO.save(p);
            
        	result = Response.ok();
        	result = result.header("X-OCCI-Location", "/placement/" + p.getId());
           	result = result.status(201).entity("OK" + " " + new ObjectMapper().writeValueAsString(p));
        } catch (URISyntaxException | RuntimeException z) {
        	logger.error("error during Post on PlacementResource", z);
        	result = Response.serverError();
        	result = result.status(400).entity("KO");
        }
        return result.build();
	}
}
