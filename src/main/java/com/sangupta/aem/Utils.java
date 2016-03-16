package com.sangupta.aem;

import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

public class Utils {
	
	private static Map<String, String> TEMPLATE_CACHE = new HashMap<>();

	/**
	 * Get the disk resource with given name.
	 * 
	 * @param name
	 * @return
	 */
	public static String getDiskResource(String name) {
		String template = TEMPLATE_CACHE.get(name);
		if(template != null) {
			return template;
		}
		
		URL url = Resources.getResource(name);
		try {
			template = Resources.toString(url, Charsets.UTF_8);
			
			if(template != null) {
				TEMPLATE_CACHE.put(name, template);
			}
			
			return template;
		} catch (IOException e) {
			//TODO: LOGGER.error("Velocity template cannot be read from disk", e);
		}
		
		return null;
	}
	
	public static String mergeTemplate(String templateName, Map<String, Object> attributes) {
		String xml = Utils.getDiskResource(templateName);
		StringWriter writer = new StringWriter();
		VelocityContext context = new VelocityContext(attributes);
		Velocity.evaluate(context, writer, "velocity-merge", xml);
		
		return writer.toString();
	}
	
}
