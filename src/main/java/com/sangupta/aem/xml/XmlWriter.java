package com.sangupta.aem.xml;

import java.io.StringWriter;
import java.util.Map.Entry;

import org.apache.commons.lang.StringEscapeUtils;

import com.sangupta.jerry.util.AssertUtils;
import com.sangupta.jerry.util.StringUtils;

public class XmlWriter {
	
	public static String write(XmlNode node) {
		if(node == null) {
			return null;
		}
		
		StringWriter writer = new StringWriter(100 * 1000); // 100KB
		
		writer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		writer.append(StringUtils.SYSTEM_NEW_LINE);
		writeNode(node, writer);
		
		return writer.toString();
	}

	private static void writeNode(XmlNode node, StringWriter writer) {
		if(node == null) {
			return;
		}
		
		writer.append('<');
		writer.append(node.name);
		
		writer.append(' ');
		if(AssertUtils.isNotEmpty(node.attributes)) {
			for(Entry<String, String> entry : node.attributes.entrySet()) {
				writer.append(entry.getKey());
				writer.append("=\"");
				writer.append(StringEscapeUtils.escapeXml(entry.getValue()));
				writer.append("\" ");
			}
		}
		
		if(AssertUtils.isEmpty(node.childNodes)) {
			// self close
			writer.append(" />");
			writer.append(StringUtils.SYSTEM_NEW_LINE);
			return;
		}
		
		writer.append('>');
		writer.append(StringUtils.SYSTEM_NEW_LINE);
		
		// children
		if(AssertUtils.isNotEmpty(node.childNodes)) {
			for(XmlNode child : node.childNodes) {
				writeNode(child, writer);
			}
		}
		
		// close
		writer.append("</");
		writer.append(node.name);
		writer.append('>');
		writer.append(StringUtils.SYSTEM_NEW_LINE);
	}

}
