package ch.bfh.fhir;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.ValidationResult;

import java.text.SimpleDateFormat;

import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Quantity;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.model.Bundle.BundleType;
import org.hl7.fhir.r4.model.Enumerations.AdministrativeGender;
import org.hl7.fhir.r4.model.Observation.ObservationStatus;

public class HapiExample {
      // Create a context with FHIR R4
      static FhirContext ctx = FhirContext.forR4();

      // set up the REST client
      static IGenericClient client = ctx.newRestfulGenericClient("https://fhir.medinflab.ti.bfh.ch/fhir");

      /**
       * This is the Java main method, which gets executed
       */
      public static void main(String[] args) throws Exception {
            Integer temporaryID = 1;

            /*
             * 0 Create a Patient with at least:
             *   - a first and family name
             *   - gender
             *   - birth date
             *   - a temporary id
             * 
             * Then print it to the console using the printResource() method.
             * 
             * @see https://r4.fhir.space/patient.html for FHIR specification
             * 
             * @see https://hapifhir.io/hapi-fhir/docs/model/working_with_resources.html for
             * HAPI FHIR documentation
             */

            Patient myPatient = new Patient();

            myPatient.addName()
                  .addGiven("Donald")
                  .setFamily("Duck");

            myPatient.setGender(AdministrativeGender.MALE);

            myPatient.setBirthDate(
                        new SimpleDateFormat("yyyy-MM-dd")
                                    .parse("1949-03-13"));

            myPatient.setId(Integer.toString(temporaryID++));

            printResource("Patient", myPatient);

            /*
             * 1 Create an Observation resource for a body weight observation of your
             * patient.
             *   - set the Code of the observation to a Codeable Concept for
             *   - Body weight is code "29463-7" in system "http://loinc.org"
             *   - set the value to a Quantity, with code "kg" in system
             *     "http://unitsofmeasure.org"
             *   - set the subject of the Observation to a reference to your Patient (you can
             *     use myPatient.getIdElement().getValue() for the correct reference value)
             *   - don't forget to set a status of your Observation resource, as it is a
             *     mandatory field
             * 
             * Then print it to the console using the printResource() method.
             * 
             * @see https://r4.fhir.space/observation.html for FHIR specification
             * 
             * @see https://hapifhir.io/hapi-fhir/docs/model/working_with_resources.html for
             * HAPI FHIR documentation
             */

            Observation myObservation = new Observation();

            myObservation.setStatus(ObservationStatus.FINAL);
            myObservation.setId(Integer.toString(temporaryID++));

            myObservation.setCode(
                  new CodeableConcept()
                        .addCoding(
                              new Coding()
                                    .setCode("29463-7")
                                    .setSystem("http://loinc.org")
                                    .setDisplay("Body weight")
                        )
            );

            myObservation.setValue(
                  new Quantity()
                        .setValue(67)
                        .setSystem("http://unitsofmeasure.org")
                        .setCode("kg")
            );

            myObservation.setSubject(
                  new Reference("Patient/" + myPatient.getIdElement().getValue())
            );

            // printResource("Observation", myObservation);

            /*
             * 2 Create a Bundle and add the resources created above
             *   - set the type of the Bundle to transaction, as we are using it to send
             *     multiple resources that are semantically connected
             *   - add an Entry to the Bundle for each of your Resources
             *     - set the fullUrl (e.g. "Patient/123")
             *     - set the HTTP Request for every Entry
             *       - method: HTTPVerb.POST 
             *       - url: the Resource Type
             * 
             * Then print it to the console using the printResource() method.
             * 
             * @see https://r4.fhir.space/bundle.html for FHIR specification
             * 
             * @see https://hapifhir.io/hapi-fhir/docs/model/working_with_resources.html for
             * HAPI FHIR documentation
             */

            Bundle myBundle = new Bundle();
            myBundle.setType(BundleType.TRANSACTION);
            myBundle.addEntry()
                  .setResource(myPatient)
                  .setFullUrl("Patient/" + myPatient.getIdElement().getValue())
                  .getRequest()
                  .setUrl("Patient")
                  .setMethod(Bundle.HTTPVerb.POST);
            myBundle.addEntry()
                  .setResource(myObservation)
                  .setFullUrl("Observation/" + myObservation.getIdElement().getValue())
                  .getRequest()
                  .setUrl("Observation")
                  .setMethod(Bundle.HTTPVerb.POST);

            printResource("Bundle", myBundle);

            /*
             * 3 Upload the bundle to the medinf lab FHIR Server using the generic restful
             * client
             *   - use endpoint https://fhir.medinflab.ti.bfh.ch/fhir
             *   - Response should be a Bundle resource
             * 
             * And print the resulting Bundle using printResource() method
             * 
             * Bonus: Try to upload the Observation resource alone. Why does it fail?
             * 
             * @see https://hapifhir.io/hapi-fhir/docs/client/examples.html for HAPI FHIR
             * documentation
             */

            // upload as bundle
            // Bundle responseBundle = client.transaction().withBundle(myBundle).execute();
            // printResource("Response Bundle", responseBundle);

            // // // or upload as single resource
            // MethodOutcome outcome = client.create().resource(myObservation).execute();
            // System.out.println("Created Observation with ID: " + outcome.getId());

            /*
             * 4 Of course, we can also read and search with the HAPI FHIR library
             *
             * ⚠️ please uncomment your create transactions above
             * ⚠️ so you don't spam the server with duplicate resources!
             * 
             *   - Read the Patient resource with the id 155 from the server
             *   - What do you expect as a response?
             *   - print the resulting Resource using printResource() method
             * 
             *   - Search for all Body weight Observations where
             *   - Observation.subject id is the ID of your Patient above (if you don't have
             *     the ID anymore, use 155)
             *   - Observation.code is "29463-7" in system "http://loinc.org" for Body weight
             *   - What do you expect as a response?
             *   - print the resulting Resource using printResource() method
             * 
             * ⚠️ please uncomment your create transactions above
             * ⚠️ so you don't spam the server with duplicate resources!
             * 
             * @see https://hapifhir.io/hapi-fhir/docs/client/examples.html#fetch-all-pages-of-a-bundle 
             * for HAPI FHIR examples
             */
            Patient patient = client.read()
                  .resource(Patient.class)
                  .withId("155")
                  .execute();

            printResource("Fetched patient", patient);

            Bundle searchResult = client.search()
                  .forResource("Observation")
                  .where(Observation.SUBJECT.hasId("155"))
                  .where(Observation.CODE.exactly()
                              .systemAndCode("http://loinc.org", "29463-7"))
                  .returnBundle(Bundle.class)
                  .execute();

            printResource("Search result", searchResult);

            /*
             * 5 With HAPI FHIR, we can also validate the resources on our machine
             * - use the validator below to validate your resources
             * - print the stringified result of the validation
             * 
             * The following info message is expected and should not concern you: 
             *    [main] INFO ca.uhn.fhir.validation.FhirValidator - Ph-schematron library
             *    not found on classpath, will not attempt to perform schematron validation
             */

            FhirValidator validator = ctx.newValidator();
            ValidationResult result = validator.validateWithResult(myBundle);

            if (result.isSuccessful()) {
                  System.out.println("🎉 Your resource is valid FHIR!");
            } else {
                  System.out.println("❌ Validation failed with " + result.getMessages().size() + " errors: ");
                  result.getMessages().forEach(
                        m -> System.out.println(" - " + m.getMessage())
                  );
            }
      }

      /*
       * Just a helper method to log resources to the console
       */
      private static void printResource(String title, Resource resource) {
            String outputString = ctx.newJsonParser()
                  .setPrettyPrint(true)
                  .encodeResourceToString(resource);

            System.out.println("\n - - - - " + title + " - - - - - \n");
            System.out.println(outputString);
      }
}
