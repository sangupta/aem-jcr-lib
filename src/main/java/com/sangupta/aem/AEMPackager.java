package com.sangupta.aem;

import java.io.File;

import org.zeroturnaround.zip.ZipUtil;

import com.sangupta.aem.jcr.JcrRepository;

/**
 * Class that is responsible for generating a ZIP package out
 * of the given JCR repository.
 * 
 * @author sangupta
 *
 */
public class AEMPackager {
	
	public static boolean createPackage(JcrRepository repository, String zipFileToCreate) {
		return createPackage(repository, new File(zipFileToCreate));
	}

	/**
	 * Wrap the {@link JcrRepository} into a ZIP file as a package so that it can be imported
	 * back into the AEM.
	 * 
	 * @param repository
	 * @return
	 */
	public static boolean createPackage(JcrRepository repository, File zipFileToCreate) {
		if(zipFileToCreate == null) {
			throw new IllegalArgumentException("zipFile to be created cannot be null");
		}
		
		if(zipFileToCreate.exists() && !zipFileToCreate.isFile()) {
			throw new IllegalArgumentException("zipFile to be created does not represent a valid file");
		}

		ZipUtil.pack(repository.getRootFolder(), zipFileToCreate);
		return true;
	}
}
