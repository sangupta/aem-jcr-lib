package com.sangupta.aem.jcr;

import java.io.IOException;

import com.sangupta.aem.AEMPackager;
import com.sangupta.aem.Utils;

public class TestJcrApis {
	
	public static void main(String[] args) throws IOException {
		JcrRepository repository = new JcrRepository("test-repo", "sangupta", "my-packages", "c:/test");
		repository.initialize();
		
		// make sure the requisite file structure is in place
		
		JcrNode componentNode = repository.createComponent("/apps/sangupta/components/page/pageTemplate", "Page template", "foundation/components/page", "aem-test");
		componentNode.saveFile("pageTemplate.jsp", Utils.getDiskResource("basic-template.jsp"));
		repository.createTemplate("/apps/sangupta/templates/dummy", "Dummy template", 10, "/apps/sangupta/components/page/pageTemplate");
		
		repository.updateFilters();
		
		AEMPackager.createPackage(repository, "c:/aem-work/sangupta.zip");
	}

}
