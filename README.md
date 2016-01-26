Students CRUD Service
======================

Build Instructions
======================
1. Copy all files from src/main/resources/lib/sqlite4java-392 into your Java extensions library folder.
	* On Mac, this is /Library/Java/Extensions (or for a local version, ~/Library/Java/Extensions can be created)
	* i.e., from the root directory of the Students project,   
	`mkdir -p ~/Library/Java/Extensions`  
	`cp -R src/main/resources/lib/sqlite4java-392/* ~/Library/Java/Extensions`
	
	
2. Update the file src/main/webapp/WEB-INF/environment.properties and change sqlite.db.path to an 
	existing folder where a small database can reside.
	
Overview
======================

This project uses the domain-driven design paradigm. Although with such few requirements in business logic this fails
    to shine, this is still an important method of development. 
    
The repository layer functions as the deep, tightly coupled
    code which necessitates certain libraries or API's. This is the layer you hope you never have to touch. In practice,
    code at this layer is infrequently heavily modified after development. Updates come upstream, and with good practice
    there are roadmaps ahead of time. This separation prevents it from becoming a monolith, and slows its descent into legacy code.
    
The services layer is where the business logic lives. Decoupled from its tangled roots in the repository, and client
    requests and error handling from the controller, it is free to impose rules on the logic.
    
The controller is a standard controller, and deals with understanding what the client wants to hear back - i.e. certain
    headers, response codes, or modifying the result of a query into something slightly more readable.
	
Notes
======================
1. The request payload includes the 'seven digit number'. The requirements include that this is auto incremented.
    The two of these do not play nice together, so although this api accepts the number, it will not use it for creation
    or updating. The requirement that it be displayed as a seven digit string is a topic I would have debated on with a 
    product owner - if this was truly a requirement, then I stick by the idea that requirements should not dictate the technology.
    To this end, I store the number as an autoincrementing integer, and upon retrieval modify it to be viewed solely as a 
    seven digit string.
    
2. The API payload contains enrolled as well as isEnrolled. If need be, this conflict can be resolved, but at the cost of
    extra logic, and added complexity in the API (I wouldn't want to write that documentation...), so I would require
    sound reasoning (backwards compatibility comes to mind) before implementing this as written.
    
3. The phone number has no validation attached to the requirements, but this is something that should be discussed with
    the product owner. As there are many ways to validate phonenumbers, I have decided to go with allowing characters 
    shown in the example (a dash), spaces and the plus sign (+).
    
4. The requirement of using specifically a v4 uuid as part of the request uuid is odd. Generally, the client should not 
	impose its will on the location of an object. After creating the object, a Location header should be sent back 
	(and in this case, a v4 UUID would make sense as a requirement). The Location header is currently sent back but the 
	id section is required to be not null. With minor changes in the app, this can be easily changed to autogenerating the UUID.
    
    
Testing
======================

1. Unit Testing is done with JUnit and functional testing with Cucumber

2. The database library I used chose to make all their classes final. Since you can't mock final classes, this poses 
    a challenge to unit testing these pieces of code. With more time, we could wrap the methods and make a real DAO object,
    but you create additional code for the sole purpose of testing. We have JUnit tests for this class, but calling
    it a unit test is incorrect, as it calls the db and reads from it (actually using its own class to do so). At best,
    this would be an integration test.
    
3. Code coverage for unit tests can be provided either by the IDE's built in functions or by cobertura by running
    `mvn cobertura:cobertura`
    However, due to what seems like an error with Java 8 and Cobertura, cobertura outside the maven plugin is unable to
    instrument any classes - i.e.
    `~/Downloads/cobertura-2.1.1/cobertura-instrument.sh ~/Google\ Drive/Students/target/Students-1.0-SNAPSHOT/WEB-INF/`
    outputs:
    `[INFO] Cobertura: Loaded information on 0 classes.
     [INFO] Cobertura: Saved information on 0 classes.
     [INFO] Cobertura: Saved information on 0 classes.`
     
     which appears to be a mismatch in one of its JARs and java8, according to stack overflow.
     
     Since intelliJ provides nicely formatted test coverage, I have opted to use the built in code coverage tool for 
     functional tests, although with more time this could be worked out - specifically, ant seems to be the tool of choice
     for cobertura, and setting up an ant project for one test seemed a bit like overkill when an alternative existed.
     
