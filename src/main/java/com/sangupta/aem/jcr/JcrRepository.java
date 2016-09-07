package com.sangupta.aem.jcr;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public interface JcrRepository {

	/**
	 * Return a node at the given path.
	 * 
	 * @param path
	 * @return
	 */
	public JcrNode getNode(String path);

	/**
	 * 
	 * @param path
	 *            the path of the component in the JCR - like
	 *            /apps/sangupta/components/page/footerComponent
	 * 
	 * @param title
	 *            the title of the component
	 * 
	 * @param parentComponent
	 *            the parent component for this component - is usually parbase
	 * 
	 * @param componentGroup
	 *            the parent group for the component - the group name under
	 *            which it will appear in editor.html or content-finder
	 * 
	 * @return the {@link JcrNode} that is created as part of this component
	 * 
	 * @throws IOException
	 *             if something fails
	 */
	public JcrNode createComponent(String path, String title, String parentComponent, String componentGroup);

	public JcrNode createTemplate(String path, String title, long ranking, String baseComponent);
	
	public JcrNode createPage(String path, String title, String template, String slingResourceType, Map<String, String> properties);

	/**
	 * Create a new DAM asset in the given folder. All DAM assets are created
	 * under <code>/content/dam</code> folder.
	 * 
	 * @param string
	 * @param bytes
	 * @throws IOException 
	 */
	public void createDAMAsset(String path, String fileName, byte[] bytes) throws IOException;

	public File getRootFolder();

	/**
	 * Update filter.xml and other files within the repository to current contents
	 * on disk.
	 * @throws IOException 
	 * 
	 */
	public void updateFilters() throws IOException;

}
