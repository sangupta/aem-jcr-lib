package com.sangupta.aem.jcr;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.sangupta.aem.xml.XmlHelper;
import com.sangupta.jerry.util.AssertUtils;

/**
 * Represents a specific node in the {@link FileJcrRepository}.
 * 
 * @author sangupta
 *
 */
public class JcrNode {
	
	public static final Charset UTF_8 = Charset.forName("UTF-8");
	
	private final String path;

	private final File nodePath;
	
	private final File xmlFile;
	
	private final boolean isRoot;
	
	private final JcrRepository repository;
	
	private final Map<String, String> properties = new HashMap<String, String>();
	
	JcrNode(File nodePath, String path, boolean isRootNode, JcrRepository repository) {
		this.nodePath = nodePath;
		this.path = path;
		this.xmlFile = new File(this.nodePath, ".content.xml");
		this.isRoot = isRootNode;
		this.repository = repository;
		
		// read the xml file if it already exists
		this.readProperties();
	}
	
	public JcrNode getChildNode(String nodePath) {
		return this.repository.getNode(this.path + "/" + nodePath);
	}
	
	/**
	 * Check if the node already exists on disk or not.
	 * 
	 * @return
	 */
	public boolean exists() {
		return this.nodePath.exists() && this.xmlFile.exists();
	}
	
	/**
	 * Initialize this node with basic properties. If node is already
	 * initialized, this call does nothing.
	 * 
	 * @return
	 * @throws IOException 
	 */
	public boolean initialize() throws IOException {
		return this.initialize(null);
	}
	
	public boolean initialize(Map<String, String> properties) throws IOException {
		if(this.xmlFile.exists()) {
			return false;
		}
		
		// add basic properties
		this.addProperty("xmlns:sling", "http://sling.apache.org/jcr/sling/1.0");
		this.addProperty("xmlns:jcr", "http://www.jcp.org/jcr/1.0");
		this.addProperty("xmlns:rep", "internal");
		this.addProperty("xmlns:cq", "http://www.day.com/jcr/cq/1.0");
		
		if(this.isRepositoryRoot()) {
			this.addProperty("jcr:mixinTypes", "[rep:AccessControllable,rep:RepoAccessControllable]");
			this.addProperty("jcr:primaryType", "rep:root");
			this.addProperty("sling:resourceType", "sling:redirect");
			this.addProperty("sling:target", "/index.html");
		}
		
		// add all properties from incoming
		if(AssertUtils.isNotEmpty(properties)) {
			this.properties.putAll(properties);
		}
		
		// save properties back to disk
		this.saveProperties();
		
		return true;
	}
	
	public boolean initializeWithContents(String contentXml) throws IOException {
		if(this.xmlFile.exists()) {
			return false;
		}
		
		FileUtils.writeStringToFile(this.xmlFile, contentXml);
		return true;
	}
	
	/**
	 * Check if this node represents the repository root or not.
	 * 
	 * @return
	 */
	public boolean isRepositoryRoot() {
		return this.isRoot;
	}

	/**
	 * Add property to this node.
	 * 
	 * @param name
	 * @param value
	 * @return
	 */
	public void addProperty(String name, String value) {
		this.properties.put(name, value);
	}
	
	public void addLongProperty(String name, long value) {
		this.properties.put(name, "{Long}" + value);
	}
	
	public void addMultiProperty(String name, String value) {
		this.properties.put(name, "[" + value + "]");
	}
	
	public boolean existsFile(String fileName) {
		File file = new File(this.nodePath, fileName);
		return file.exists();
	}
	
	public void saveFile(String fileName, String contents) throws IOException {
		File file = new File(this.nodePath, fileName);
		FileUtils.writeStringToFile(file, contents, UTF_8);
	}
	
	public void saveFile(String fileName, byte[] contents) throws IOException {
		File file = new File(this.nodePath, fileName);
		FileUtils.writeByteArrayToFile(file, contents);
	}
	
	public void copyFile(File file) throws IOException {
		String name = file.getName();
		File nodeFile = new File(this.nodePath, name);
		String contents = FileUtils.readFileToString(file);
		FileUtils.writeStringToFile(nodeFile, contents, UTF_8);
	}
	
	// --------------------------------- PRIVATE METHODS
	
	/**
	 * Read the properties of this node from disk.
	 * 
	 */
	private void readProperties() {
		if(!this.xmlFile.exists()) {
			// no file exists, just quit
			return;
		}
	}

	/**
	 * Save properties back to disk
	 * @throws IOException 
	 * 
	 */
	private void saveProperties() throws IOException {
		FileUtils.writeStringToFile(this.xmlFile, XmlHelper.XML_HEADER);
		FileUtils.writeStringToFile(this.xmlFile, XmlHelper.createTag("jcr:root", this.properties), true);
	}

}
