package com.sangupta.aem;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeroturnaround.zip.ZipUtil;

import com.sangupta.aem.jcr.FileJcrRepository;
import com.sangupta.aem.jcr.JcrRepository;

/**
 * Class that is responsible for generating a ZIP package out
 * of the given JCR repository.
 * 
 * @author sangupta
 *
 */
public class AEMPackager {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AEMPackager.class);
	
	public static File createPackage(JcrRepository repository, String zipFileToCreate) {
		if(repository == null) {
			throw new IllegalArgumentException("Repository cannot be null");
		}
		
		return createPackage(repository, new File(zipFileToCreate));
	}

	/**
	 * Wrap the {@link FileJcrRepository} into a ZIP file as a package so that it can be imported
	 * back into the AEM.
	 * 
	 * @param repository
	 * @return
	 */
	public static File createPackage(JcrRepository repository, File zipFileToCreate) {
		if(zipFileToCreate == null) {
			throw new IllegalArgumentException("zipFile to be created cannot be null");
		}
		
		if(zipFileToCreate.exists() && !zipFileToCreate.isFile()) {
			throw new IllegalArgumentException("zipFile to be created does not represent a valid file");
		}

		// this helps in making sure that all content will be imported into AEM 
		try {
			repository.updateFilters();
		} catch (IOException e) {
			LOGGER.error("Unable to update filter information in repository", e);
		}
		
		// pack now
		ZipUtil.pack(repository.getRootFolder(), zipFileToCreate);
		
		// return the file that was created
		return zipFileToCreate;
	}
	
}
