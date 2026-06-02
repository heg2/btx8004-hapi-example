# BTX8004: HAPI FHIR example.
This is an example class for [HAPI FHIR client](https://hapifhir.io/hapi-fhir/docs/client/introduction.html) in the BTX8004 module.

## Setup
- Make sure you have JDK installed in a version >= 17. You can check this in the terminal with the command `java --version`.
  - [If you're missing the JDK, follow this link](https://www.javathinking.com/blog/how-to-install-jdk-17/)
- Verify that your JAVA_HOME variable is set. Check with `echo %JAVA_HOME%` (windows) or `echo $JAVA_HOME` (unix, macOS). 
  - [Otherwise follow the instructions here](https://www.baeldung.com/java-home-on-windows-mac-os-x-linux)
- You must have Apache Maven installed. Check with `mvn -version`
  - [Otherwise, you can install Maven from here](https://maven.apache.org/install.html)
- Check out this repository using `git clone https://github.com/heg2/btx8004-hapi-example` or just downloading the [code as zip archive](https://github.com/heg2/btx8004-hapi-example/archive/refs/heads/main.zip)

## How to start
After completing the set up above: 
Edit the HapiExample class under [src/main/java/ch/bfh/fhir](/src/main/java/ch/bfh/fhir/HapiExample.java) and save the file.

Run the following command to build:
```bash
mvn compile
```

After that, you can run your program with:
```bash
mvn exec:java -Dexec.mainClass="ch.bfh.fhir.HapiExample"
```

## HAPI FHIR Documentation
- [HAPI FHIR Client documentation](https://hapifhir.io/hapi-fhir/docs/client/generic_client.html)
- [HAPI FHIR API documentation](https://hapifhir.io/hapi-fhir/apidocs/hapi-fhir-structures-r4/org/hl7/fhir/r4/model/package-summary.html)