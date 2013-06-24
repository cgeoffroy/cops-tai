package org.tai.cops.server;


import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.DispatcherType;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.util.log.Logger;
import org.eclipse.jetty.util.log.LoggerLog;
import org.tai.cops.occi.ERenderingStructures;

import com.google.inject.servlet.GuiceFilter;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import occi.lexpar.OcciParser;

/**
 * Starts an embedded Jetty server.
 */
public class Main {
	public static final String PUBLISHER_URL = "http://127.0.0.1:8086";
	
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
	
    public static void main(String[] args) throws Exception {
        Server server = new Server(8080);
        ServletContextHandler root = new ServletContextHandler(server, "/", ServletContextHandler.SESSIONS);

        root.addEventListener(new SampleConfig());
        root.addFilter(GuiceFilter.class, "/*", EnumSet.of(DispatcherType.REQUEST));
        root.addServlet(EmptyServlet.class, "/*");

        Client client = Client.create();
        
		WebResource webResource = client.resource(PUBLISHER_URL + "/-/");
		System.out.println("Main.main() connecting to publisher");
		ClientResponse response = webResource.accept("text/occi").type("text/occi")
                   .get(ClientResponse.class);
        System.out.println("Main.main() got a response");
		
        String occiHeaders = rebuildOcciHeaders(response.getHeaders()); 
		System.out.println("Main.main() : " + occiHeaders);
		System.out.println("response = " +
				OcciParser.getParser(occiHeaders).category().toString());
		
        server.start();
    }
}
