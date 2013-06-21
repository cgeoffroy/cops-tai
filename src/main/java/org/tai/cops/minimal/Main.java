package org.tai.cops.minimal;


import java.util.EnumSet;
import java.util.List;
import java.util.Map.Entry;

import javax.servlet.DispatcherType;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;

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
		
		StringBuilder sb = new StringBuilder();
		for (Entry<String, List<String>> k : response.getHeaders().entrySet()) {
			if (! "Category".equals(k.getKey()))
				continue;
			//System.out.println("Main.main() " + k.getKey() + ": " + k.getValue());
			for (String v : k.getValue()) {
				sb.append(k.getKey()).append(": ").append(v).append('\n');
			}
		}
		System.out.println("Main.main() : " + sb.toString());
		System.out.println("response = " +
				OcciParser.getParser(sb.toString()).category().toString());
		
        server.start();
    }
}
