package com.sangupta.aem.xml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.apache.commons.lang3.time.FastDateFormat;

import com.sangupta.jerry.util.AssertUtils;

public class XmlNode {
	
	public static final FastDateFormat DATE_FORMATTER = FastDateFormat.getInstance("yyyy-MM-dd'T'hh:mm:ss.SSS", TimeZone.getTimeZone("UTC"));
	
	public String name;
	
	public final Map<String, String> attributes = new HashMap<String, String>();
	
	public final List<XmlNode> childNodes = new ArrayList<XmlNode>();
	
	public XmlNode(String name) {
		this.name = name;
	}

	public void addAttribute(String name, String value) {
		if(AssertUtils.isEmpty(name)) {
			return;
		}
		
		this.attributes.put(name, value);
	}
	
	public void addChild(XmlNode child) {
		if(child == null) {
			return;
		}
		
		this.childNodes.add(child);
	}
	
	public void addChildren(List<XmlNode> nodes) {
		if(AssertUtils.isEmpty(nodes)) {
			return;
		}
		
		for(XmlNode child : nodes) {
			this.addChild(child);
		}
	}
	
	public XmlNode createChild(String name) {
		XmlNode child = new XmlNode(name);
		this.childNodes.add(child);
		return child;
	}

	public void addDateAttribute(String key, long currentTimeMillis) {
		this.addAttribute(key, "{Date}" + DATE_FORMATTER.format(currentTimeMillis));
	}

	public void addLongAttribute(String name, long value) {
		this.attributes.put(name, "{Long}" + value);
	}
	
	public void addMultiAttribute(String name, String value) {
		this.attributes.put(name, "[" + value + "]");
	}
}
