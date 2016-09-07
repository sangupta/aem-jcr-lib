package com.sangupta.aem.xml;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringEscapeUtils;

import com.sangupta.jerry.util.AssertUtils;

public class XmlHelper {

	public static final String XML_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
	
	public static String createTag(String tagName, Map<String, String> properties) {
		return createTag(tagName, properties, null);
	}
	
	public static String createTag(String tagName, Map<String, String> properties, String contents) {
		StringBuilder builder = new StringBuilder(1024); // 1 kb be default
		
		builder.append('<');
		builder.append(tagName);
		
		// output each property
		for(Entry<String, String> entry : properties.entrySet()) {
			builder.append(' ');
			builder.append(entry.getKey());
			builder.append("=\"");
			builder.append(StringEscapeUtils.escapeXml11(entry.getValue()));
			builder.append('"');
		}
		
		// append content
		if(AssertUtils.isNotEmpty(contents)) {
			builder.append(" >");
			builder.append('\n');
			
			builder.append(contents);
			
			builder.append('\n');
			
			builder.append("</");
			builder.append(tagName);
			builder.append('>');
		} else {
			// close the tag
			builder.append(" />");
		}
		
		return builder.toString();
	}
	
}
