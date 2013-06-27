package org.tai.cops.server;


import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.DispatcherType;

import org.codehaus.jackson.map.ObjectMapper;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.tai.cops.occi.ERenderingStructures;
import org.tai.cops.occi.client.Category;

import com.google.inject.servlet.GuiceFilter;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.LoggingFilter;

import fj.F;
import fj.data.Option;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import occi.lexpar.OcciParser;

/**
 * Starts an embedded Jetty server.
 */
public class Main {
	public static URI PUBLISHER_URL;
	
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
	
    public static void main(String[] args) throws Exception {
    	PUBLISHER_URL = new URI("http://127.0.0.1:8086");
    	
        Server server = new Server(8080);
        ServletContextHandler root = new ServletContextHandler(server, "/", ServletContextHandler.SESSIONS);

        root.addEventListener(new SampleConfig());
        root.addFilter(GuiceFilter.class, "/*", EnumSet.of(DispatcherType.REQUEST));
        root.addServlet(EmptyServlet.class, "/*");

        Client client = Client.create();
        
		WebResource webResource = client.resource(PUBLISHER_URL + "/-/");
		
		logger.debug("connecting to publisher");
		ClientResponse response = webResource.accept("text/occi").type("text/occi")
                   .get(ClientResponse.class);
		
		logger.debug("rebuilding occi headers with the response");		
        String occiHeaders = rebuildOcciHeaders(response.getHeaders());
        logger.debug("rebuild result:\n" + occiHeaders);
        
        OcciParser op = OcciParser.getParser(occiHeaders);
        Map<String, ArrayList> hs = op.headers();
        ObjectMapper mapper = new ObjectMapper();
		logger.debug("parsed {} catogories: {}", hs.get(OcciParser.occi_categories).size(),
				mapper.writeValueAsString(hs.get(OcciParser.occi_categories)));
		
		List<Category> tmp = hs.get(OcciParser.occi_categories);
		fj.data.List<Category> u = fj.data.List.iterableList(tmp);
		Option<Category> o = u.find(new F<Category, Boolean>() {		
			@Override
			public Boolean f(Category a) {
				return "publication".equals(a.getTerm());
			}
		});
		if (o.isNone()) {
			logger.error("Cannot find the publication category");
			System.exit(1);
		}

		URI ptionUri = PUBLISHER_URL.resolve(o.some().getLocation());
		logger.debug("next location = {}", ptionUri);
		
		logger.debug("connecting to the publication category");
		client.addFilter(new LoggingFilter(System.out));
		webResource = client.resource(ptionUri);
		response = webResource.accept("text/occi").type("text/occi")
				.header("Category", o.some().getRequestFilter())
				.header("X-OCCI-Attribute", "occi.publication.where=\"marketplace\"")
				.header("X-OCCI-Attribute", "occi.publication.what=\"provider\"")
				.get(ClientResponse.class);
		
		
        server.start();
    }
}
