# aem-jcr-lib

[![Build Status](https://travis-ci.org/sangupta/aem-jcr-lib.svg?branch=master)](https://travis-ci.org/sangupta/aem-jcr-lib)
[![Coverage Status](https://coveralls.io/repos/github/sangupta/aem-jcr-lib/badge.svg?branch=master)](https://coveralls.io/github/sangupta/aem-jcr-lib?branch=master)
[![Maven Version](https://maven-badges.herokuapp.com/maven-central/com.sangupta/aem-jcr-lib/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.sangupta/aem-jcr-lib)

Java library to write JCR/Vault compliant files to disk so that they can be imported directly into Adobe Experience Manager.

## Tested On

The library has been tested on the following platforms:

* Adobe Experience Manager 6.2
* Adobe Experience Manager 6.1
* Adobe Experience Manager 6.0

## Usage

The following creates a pageComponent with a basic-template, and then attaches the `dummy` template to this component. It also creates a ZIP called
`sangupta.zip` that can be imported directly into AEM via Package Manager. 

```java
JcrRepository repository = new FileJcrRepository("test-repo", "sangupta", "my-packages", "c:/test");
		
// make sure the requisite file structure is in place
repository.initialize();

// create a page component		
JcrNode componentNode = repository.createComponent("/apps/sangupta/components/page/pageTemplate", "Page template", "foundation/components/page", "sangupta-components");

// copy a basic template into this component
componentNode.saveFile("pageTemplate.jsp", Utils.getDiskResource("basic-template.jsp"));

// create a template and attach it to previously created component
repository.createTemplate("/apps/sangupta/templates/dummy", "Dummy template", 10, "/apps/sangupta/components/page/pageTemplate");

// update the repository paths to the ones that have some content in it
repository.updateFilters(); 

// create a ZIP package out of it
AEMPackager.createPackage(repository, "c:/aem-work/sangupta.zip");
```

## Versioning

For transparency and insight into our release cycle, and for striving to maintain backward compatibility, 
`aem-jcr-lib` will be maintained under the Semantic Versioning guidelines as much as possible.

Releases will be numbered with the follow format:

`<major>.<minor>.<patch>`

And constructed with the following guidelines:

* Breaking backward compatibility bumps the major
* New additions without breaking backward compatibility bumps the minor
* Bug fixes and misc changes bump the patch

For more information on SemVer, please visit http://semver.org/.

## License

```
aem-jcr-lib: Java SDK to write AEM JCR compatible packages
Copyright (c) 2016, Sandeep Gupta

http://sangupta.com/projects/aem-jcr-lib

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

	http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```