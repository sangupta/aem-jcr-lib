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
import com.sangupta.jerry.util.AssertUtils;
import com.sangupta.jerry.util.UriUtils;

public class Utils {
	
	/**
	 * Cache to resources already read from disk.
	 * 
	 */
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

	/**
	 * Merge the given disk resource with given attributes using Velocity layout engine.
	 * 
	 * @param templateName
	 * @param attributes
	 * @return
	 */
	public static String mergeTemplate(String templateName, Map<String, Object> attributes) {
		String xml = Utils.getDiskResource(templateName);
		StringWriter writer = new StringWriter();
		VelocityContext context = new VelocityContext(attributes);
		Velocity.evaluate(context, writer, "velocity-merge", xml);
		
		return writer.toString();
	}

	public static String extractPath(String url) {
		if(AssertUtils.isEmpty(url)) {
			return url;
		}
		
		String path = UriUtils.extractPath(url);
		int index = path.lastIndexOf('/');
		if(index == -1) {
			return path;
		}
		
		return path.substring(0, index);
	}

	public static String removeExtension(String path) {
		int slash = path.lastIndexOf('/');
		int dot = path.lastIndexOf('.');
		if(dot < slash) {
			return path;
		}
		
		return path.substring(0, dot);
	}

	public static String escapeAttributeName(String key) {
		char[] array = key.toCharArray();
		for(int index = 0; index < array.length; index++) {
			if(invalidXmlAttributeChar(array[index])) {
				array[index] = '-';
			}
		}
		
		return new String(array);
	}

	public static boolean invalidXmlAttributeChar(char c) {
		if(c >= 'a' && c <= 'z') {
			return false;
		}
		
		if(c >= 'A' && c <= 'Z') {
			return false;
		}
	
		if(c >= '0' && c <= '9') {
			return false;
		}
		
		return true;
	}
	
}
