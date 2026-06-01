# BTX8004: HAPI FHIR example.
This is an example class for [HAPI FHIR client](https://hapifhir.io/hapi-fhir/docs/client/introduction.html) in the BTX8004 module.

## How to start
Edit the HapiExample class under [src/main/java/ch/bfh/fhir](/src/main/java/ch/bfh/fhir/HapiExample.java) and save the file.

Run the following command to build:
```bash
mvn compile
```

After that, you can run your program with:
```bash
mvn exec:java -Dexec.mainClass="ch.bfh.fhir.HapiExample"
```
