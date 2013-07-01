package org.tai.cops.server;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
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
import javax.annotation.Nullable;
import javax.servlet.DispatcherType;
import javax.ws.rs.core.MultivaluedMap;

import org.antlr.runtime.RecognitionException;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.tai.cops.occi.ERenderingStructures;
import org.tai.cops.occi.client.Categories;
import org.tai.cops.occi.client.Category;
import org.tai.cops.occi.client.Resource;
import org.tai.cops.occi.cords.Publication;

import com.google.inject.servlet.GuiceFilter;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;
import com.sun.jersey.api.client.filter.LoggingFilter;
import com.sun.jersey.core.util.MultivaluedMapImpl;

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
	
	private static Client buildClient() {
		Client client = Client.create();
		client.addFilter(new LoggingFilter(System.out));
		return client;
	}
	
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
		Client client = buildClient();
		WebResource webResource = client.resource(location + "/-/");
		
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
		
		Client client = buildClient();
		
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
	
	private static @Nonnull MultivaluedMap<String, String> fetchFirstResource(@Nonnull List<URL> locations,
			@Nonnull Category cat, @Nonnull List<String> filters) {
		Client client = buildClient();
		ClientResponse response = null;
		for (URL u1 : locations) {
			logger.debug("trying to reach provider '{}'", u1);
			Builder b = client.resource(u1.toString()).accept("text/occi").type("text/occi")
					.header("Category", cat.getRequestFilter());
			for (String s : filters) {
				b.header("X-OCCI-Attribute", s);
			}
			response = b.get(ClientResponse.class);
			if (response.getStatus() == 200) {
				break;
			}
			response = null;
		}
		if (null == response) {
			logger.error("no provider servers were reachable");
			return new MultivaluedMapImpl();
		} else {
			return response.getHeaders();
		}
	}
	
    public static void main(String[] args) throws Exception {
    	PUBLISHER_URL = new URI("http://127.0.0.1:8086");
    	
        Server server = new Server(8080);
        ServletContextHandler root = new ServletContextHandler(server, "/", ServletContextHandler.SESSIONS);

        root.addEventListener(new SampleConfig());
        root.addFilter(GuiceFilter.class, "/*", EnumSet.of(DispatcherType.REQUEST));
        root.addServlet(EmptyServlet.class, "/*");
        	
		
        Publication mgrResourcesProvider = null;
		try {
			mgrResourcesProvider = fetch(PUBLISHER_URL, "publication",
					Arrays.asList("occi.publication.where=\"marketplace\"", "occi.publication.what=\"provider\""),
					Publication.class);
		} catch (Exception e) {
			logger.error("error while looking the the Account manager", e);
		}
		
		Publication mgrResourcesAccount = null;
		try {
			mgrResourcesAccount = fetch(PUBLISHER_URL, "publication",
					Arrays.asList("occi.publication.where=\"marketplace\"", "occi.publication.what=\"account\""),
					Publication.class);
		} catch (Exception e) {
			logger.error("error while looking the the Account manager", e);
		}
		if (null == mgrResourcesAccount.getWhy()) {
			logger.error("The component that manage the 'Account' category doesn't have a 'why' field");
		}
		
		Resource mgrResourcesTmp1 = null;
		try {
			mgrResourcesTmp1 = fetch(mgrResourcesAccount.getWhy().toURI(), "account",
					Arrays.asList("occi.account.name=\"accords\""),
					Publication.class);
		} catch (Exception e) {
			logger.warn("error while looking the the 'accords' account instance", e);
		}
		
        server.start();
    }
    
    private static @Nullable <T extends Resource> T fetch(@Nonnull URI root, @Nonnull String catTermName,
    		@Nonnull List<String> filtersCatInstances, @Nonnull Class<T> t) throws Exception {
        /* we discover '/-/' on the publisher and get the categories he manage */
        List<Category> publisherCategories = loadRoot(root);
        if (null == publisherCategories || publisherCategories.size() <= 0) {
        	logger.error("Unable to load categories from the publisher");
        }
        //logger.debug("got some categories: {}", mapper.writeValueAsString(publisherCategories));
        
        /* we look for the <what> category */
        Category publicationCat;
        {	Option<Category> oc = Categories.findCategory(publisherCategories, catTermName);
        	if (oc.isNone()) {
        		logger.error("Cannot find the '{}' category in the listing", catTermName);
    			return null;
        	}
        	publicationCat = oc.some();
        }
        
        /* we ask the publisher to filter his publication instances and get some candidates */
        List<URL> possibleLocUrl = makeRequestesToLocation(root, publicationCat, filtersCatInstances);
        
        Map<String, String> possibleAttributes = new HashMap<>();
        /* we fetch the first publication resource, and retrieve his 'Attribute' headers */
        for (String s : fetchFirstResource(possibleLocUrl, publicationCat, filtersCatInstances).get("X-OCCI-Attribute")) {
        	for (Entry<String, Object> z : OcciParser.getParser(s).attributes_attr().entrySet()) {
        		possibleAttributes.put(z.getKey(), (String) z.getValue());
        	}
        }
		
		T mgrResourcesProvider = null;
		try {
			/* from the parsed headers, we build a 'Publication' instance */
			mgrResourcesProvider = t.getConstructor(Map.class).newInstance(possibleAttributes);
			logger.debug("parsed the resource: {}", mapper.writeValueAsString(mgrResourcesProvider));
		} catch (InstantiationException | IllegalAccessException | InvocationTargetException 
    			| NoSuchMethodException z) {
			logger.error("fatal error, all Resource sub-class must have this constructor", z);
			System.exit(2);
		} catch (RuntimeException z) {
			logger.error("error while building the publication resource", z);
		} catch (JsonGenerationException | JsonMappingException e) {
			logger.debug("json output error A: ", e);
		} catch (IOException e) {
			logger.debug("json output error B:", e);
		}	
		
		return mgrResourcesProvider;
    }
    
}
