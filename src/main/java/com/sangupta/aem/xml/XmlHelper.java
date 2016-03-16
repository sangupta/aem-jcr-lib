package com.sangupta.aem.xml;

import java.util.Map;
import java.util.Map.Entry;

public class XmlHelper {

	public static final String XML_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
	
	public static String createTag(String tagName, Map<String, String> properties) {
		StringBuilder builder = new StringBuilder(1024); // 1 kb be default
		
		builder.append('<');
		builder.append(tagName);
		
		// output each property
		for(Entry<String, String> entry : properties.entrySet()) {
			builder.append(' ');
			builder.append(entry.getKey());
			builder.append("=\"");
			builder.append(entry.getValue());
			builder.append('"');
		}
		
		// close the tag
		builder.append(" />");
		
		return builder.toString();
	}
	
}
