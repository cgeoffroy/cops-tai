package org.tai.cops.server;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Nonnull;
import javax.servlet.DispatcherType;

import org.antlr.runtime.RecognitionException;
import org.codehaus.jackson.map.ObjectMapper;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.tai.cops.occi.ERenderingStructures;
import org.tai.cops.occi.client.Categories;
import org.tai.cops.occi.client.Category;
import org.tai.cops.occi.client.Publication;

import com.beust.jcommander.internal.Lists;
import com.google.inject.servlet.GuiceFilter;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;
import com.sun.jersey.api.client.filter.LoggingFilter;

import fj.data.Option;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import occi.lexpar.OcciParser;

/**
 * Starts an embedded Jetty server.
 */
public class Main {
	public static URI PUBLISHER_URL;
	
	private static ObjectMapper mapper = new ObjectMapper();
	
	private static String rebuildOcciHeaders(Map<String, List<String>> headers) {
		StringBuilder buffer = new StringBuilder();
		for (Entry<String, List<String>> i_elt : headers.entrySet()) {
			ERenderingStructures ers = ERenderingStructures.fromHttpHeader(i_elt.getKey());
			if (ers != null)
				for (String v : i_elt.getValue()) {
					buffer.append(i_elt.getKey()).append(": ").append(v).append('\n');
				}
		}
		return buffer.toString();
	}
	
	final static Logger logger = LoggerFactory.getLogger(Main.class);
	
	private static List<Category> loadRoot(URI location) throws Exception {
		Client client = Client.create();
		client.addFilter(new LoggingFilter(System.out));
		WebResource webResource = client.resource(PUBLISHER_URL + "/-/");
		
		logger.debug("connecting to location: {}", location);
		ClientResponse response = webResource.accept("text/occi").type("text/occi")
                   .get(ClientResponse.class);		
        
		logger.debug("rebuilding occi headers with the response");		
        String occiHeaders = rebuildOcciHeaders(response.getHeaders());
        logger.debug("rebuild result:\n" + occiHeaders);
        
        OcciParser op = OcciParser.getParser(occiHeaders);
        Map<String, ArrayList> hs = op.headers();
		logger.debug("parsed {} catogories: {}", hs.get(OcciParser.occi_categories).size(),
				mapper.writeValueAsString(hs.get(OcciParser.occi_categories)));
		
		return hs.get(OcciParser.occi_categories);
	}
	
	
	private static @Nonnull List<URL> makeRequestesToLocation(URI root, Category cat, List<String> filters) {
		URI ptionUri = root.resolve(cat.getLocation());
		logger.debug("next location = {}", ptionUri);
		
		Client client = Client.create();
		client.addFilter(new LoggingFilter(System.out));
		
		logger.debug("connecting to the publication category");
		Builder b = client.resource(ptionUri).accept("text/occi").type("text/occi")
				.header("Category", cat.getRequestFilter());
		for (String s : filters) {
			b = b.header("X-OCCI-Attribute", s);
		}
		ClientResponse response = b.get(ClientResponse.class);
		
		List<URL> possibleLocUrl = new ArrayList<>();
		
		if (response.getStatus() != 200 && !response.getHeaders().containsKey("X-OCCI-Location")) {
			logger.error("the provider location was not found");
			return possibleLocUrl;
		}
		List<String> possibleLoc = response.getHeaders().get("X-OCCI-Location");
		for (String s : possibleLoc) {
			try {
				possibleLocUrl.addAll(OcciParser.getParser(s).location_values());
			} catch (RecognitionException e) {
				logger.error("parsing recongintion error while reading location: {}", e);
			} catch (Exception e) {
				logger.error("generic parsing error while reading location: {}", e);
			}
		}
		return possibleLocUrl;
	}
	
    public static void main(String[] args) throws Exception {
    	PUBLISHER_URL = new URI("http://127.0.0.1:8086");
    	
        Server server = new Server(8080);
        ServletContextHandler root = new ServletContextHandler(server, "/", ServletContextHandler.SESSIONS);

        root.addEventListener(new SampleConfig());
        root.addFilter(GuiceFilter.class, "/*", EnumSet.of(DispatcherType.REQUEST));
        root.addServlet(EmptyServlet.class, "/*");
        
        Client client = Client.create();
        WebResource webResource;
        ClientResponse response;
        
        List<Category> tmp = loadRoot(PUBLISHER_URL);
        if (null == tmp || tmp.size() <= 0) {
        	logger.error("Unable to load categories from the publisher");
        }
        logger.debug("got some categories: {}", mapper.writeValueAsString(tmp));
        Category pubCat;
        {	Option<Category> oc = Categories.findCategory(tmp, "publication");
        	if (oc.isNone()) {
        		logger.error("Cannot find the publication category in the listing");
    			System.exit(1);
        	}
        	pubCat = oc.some();
        }
        
        List<URL> possibleLocUrl = makeRequestesToLocation(PUBLISHER_URL, pubCat,
        		Arrays.asList("occi.publication.where=\"marketplace\"", "occi.publication.what=\"provider\""));
		
		response = null;
		for (URL u1 : possibleLocUrl) {
			logger.debug("trying to reach provider '{}'", u1);
			webResource = client.resource(u1.toString());
			response = webResource.accept("text/occi").type("text/occi")
					.header("Category", pubCat.getRequestFilter())
					.header("X-OCCI-Attribute", "occi.publication.where=\"marketplace\"")
					.header("X-OCCI-Attribute", "occi.publication.what=\"provider\"")
					.get(ClientResponse.class);
			if (response.getStatus() == 200) {
				break;
			}
			response = null;
		}
		if (null == response) {
			logger.error("no provider servers were reachable");
			System.exit(1);
		}
		
		Map<String, String> possibleAttributes = new HashMap<>();
		for (String s : response.getHeaders().get("X-OCCI-Attribute")) {
			for (Entry<String, Object> z : OcciParser.getParser(s).attributes_attr().entrySet()) {
				possibleAttributes.put(z.getKey(), (String) z.getValue());
			}
		}
		
		Publication p = new Publication(possibleAttributes);
		logger.debug("parsed the publication: {}", 
				mapper.writeValueAsString(p));
		
        server.start();
    }
}
