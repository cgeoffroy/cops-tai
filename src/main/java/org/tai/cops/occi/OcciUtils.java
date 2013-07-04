package org.tai.cops.occi;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.ws.rs.core.HttpHeaders;

import org.antlr.runtime.RecognitionException;

import occi.lexpar.OcciParser;

public final class OcciUtils {

	private OcciUtils() {}
	
	public static Map<String, String> findOcciAttributes(HttpHeaders headers) throws RecognitionException, Exception {
        Map<String, String> possibleAttributes = new HashMap<>();
        /* we travel headers to find occi attr fields */
        for (String s : headers.getRequestHeader("X-OCCI-Attribute")) {
        	for (Entry<String, Object> z : OcciParser.getParser(s).attributes_attr().entrySet()) {
        		possibleAttributes.put(z.getKey(), (String) z.getValue());
        	}
        }
        return possibleAttributes;
	}

}
