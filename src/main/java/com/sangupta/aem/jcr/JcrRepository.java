package com.sangupta.aem.jcr;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.sangupta.aem.Utils;
import com.sangupta.aem.vault.VaultHelper;

public class JcrRepository {

	/**
	 * The root folder where the JCR repository will be created
	 */
	private final File rootFolder;
	
	/**
	 * The jcr_root folder inside the repo folder
	 */
	private final File jcrRoot;
	
	/**
	 * The META-INF folder inside the repo folder
	 */
	private final File metaRoot;

	/**
	 * The name of the repository to initialize with
	 */
	private final String repoName;
	
	/**
	 * The username who is creating the repository
	 */
	private final String userName;
	
	/**
	 * The group name of the package
	 */
	private final String groupName;
	
	/**
	 * Construct the repository at the given path
	 * 
	 * @param repoName
	 * @param userName
	 * @param groupName
	 * @param rootFolder
	 */
	public JcrRepository(String repoName, String userName, String groupName, String rootFolder) {
		this(repoName, userName, groupName, new File(rootFolder));
	}
	
	/**
	 * Construct the repository at the given folder
	 * 
	 * @param repoName
	 * @param userName
	 * @param groupName
	 * @param rootFolder
	 */
	public JcrRepository(String repoName, String userName, String groupName, File rootFolder) {
		this.repoName = repoName;
		this.userName = userName;
		this.groupName = groupName;
		
		this.rootFolder = rootFolder;
		
		this.jcrRoot = new File(this.rootFolder, "jcr_root");
		this.metaRoot = new File(this.rootFolder, "META-INF");
		
		if(!this.jcrRoot.exists()) {
			this.jcrRoot.mkdirs();
		}
		
		if(!this.metaRoot.exists()) {
			this.metaRoot.mkdirs();
		}
	}
	
	/**
	 * Create the basic structure of a JCR repo and the required folders
	 * and files needed.
	 * 
	 * @return
	 * @throws IOException 
	 */
	public boolean initialize() throws IOException {
		// initialize the root node
		JcrNode node = this.getNode(null);
		node.initialize();
		
		// initialize the vault folder
		this.initializeVault();
		return true;
	}

	/**
	 * Check if a repository already exists at this point or not.
	 * 
	 * @return
	 */
	public boolean exists() {
		return this.rootFolder.exists();
	}
	
	/**
	 * Return a node at the given path.
	 * 
	 * @param path
	 * @return
	 */
	public JcrNode getNode(String path) {
		if(path == null || path.isEmpty()) {
			path = "/";
		}
		
		path = path.trim();
		
		if("/".equals(path)) {
			// this is a root node request
			return new JcrNode(this.jcrRoot, "/", true, this);
		}
		
		File nodePath = new File(this.jcrRoot, path);
		nodePath.mkdirs();
		return new JcrNode(nodePath, path, "/".equals(path), this);
	}
	
	public JcrNode createComponent(String path, String title, String parentComponent, String componentGroup) throws IOException {
		JcrNode node = getNode(path);
		
		Map<String, String> map = new HashMap<>();
		map.put("jcr:primaryType", "cq:Component");
		map.put("jcr:title", title);
		map.put("sling:resourceSuperType", parentComponent);
		map.put("componentGroup", componentGroup);
		
		node.initialize(map);
		
		return node;
	}
	
	public JcrNode createTemplate(String path, String title, long ranking, String baseComponent) throws IOException {
		Map<String, Object> map = new HashMap<>();
		map.put("title", title);
		map.put("ranking", ranking);
		map.put("baseComponent", baseComponent);
		String xml = Utils.mergeTemplate("create-template.xml", map);
		
		JcrNode node = getNode(path);
		node.initializeWithContents(xml);
		
		return node;
	}
	
	public File getRootFolder() {
		return this.rootFolder;
	}
	
	/**
	 * Initialize the meta-inf/vault folder
	 * @throws IOException 
	 */
	private void initializeVault() throws IOException {
		File vault = new File(this.metaRoot, "vault");
		if(vault.exists()) {
			return;
		}
		
		vault.mkdirs();
		
		// copy files into the vault directory
		FileUtils.writeStringToFile(new File(vault, "config.xml"), Utils.getDiskResource("vault/config.xml"));
		FileUtils.writeStringToFile(new File(vault, "nodetypes.cnd"), Utils.getDiskResource("vault/nodetypes.cnd"));
		
		Map<String, Object> properties = new HashMap<>();
		properties.put("name", this.repoName);
		properties.put("user", this.userName);
		properties.put("group", this.groupName);
		FileUtils.writeStringToFile(new File(vault, "properties.xml"), Utils.mergeTemplate("vault/properties.xml", properties));
		
		
		// write filter.xml file
		File filterXml = new File(vault, "filter.xml");
		FileUtils.writeStringToFile(filterXml, VaultHelper.createVaultFilterXml(new String[] { "/apps/sangupta" }));
		
		// definition xml
		File definition = new File(vault, "definition");
		definition.mkdirs();
		FileUtils.writeStringToFile(new File(definition, ".content.xml"), Utils.getDiskResource("vault/definition/.content.xml"));
	}

}
