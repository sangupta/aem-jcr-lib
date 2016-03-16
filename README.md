# aem-jcr-lib

Java library to write JCR/Vault compliant files to disk so that they can be imported directly into Adobe Experience Manager.

## Usage

The following creates a pageComponent with a basic-template, and then attaches the `dummy` template to this component. It also creates a ZIP called
`sangupta.zip` that can be imported directly into AEM via Package Manager. 

```java
JcrRepository repository = new JcrRepository("test-repo", "sangupta", "my-packages", "c:/test");
		
// make sure the requisite file structure is in place
repository.initialize();

// create a page component		
JcrNode componentNode = repository.createComponent("/apps/sangupta/components/page/pageTemplate", "Page template", "foundation/components/page");

// copy a basic template into this component
componentNode.saveFile("pageTemplate.jsp", Utils.getDiskResource("basic-template.jsp"));

// create a template and attach it to previously created component
repository.createTemplate("/apps/sangupta/templates/dummy", "Dummy template", 10, "/apps/sangupta/components/page/pageTemplate");

// create a ZIP package out of it
AEMPackager.createPackage(repository, "c:/aem-work/sangupta.zip");
```

## License

Apache Public License Version 2.0

