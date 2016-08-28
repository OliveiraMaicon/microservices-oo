package microservice.com.example.component;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import microservice.com.example.component.client.PatientClient;
import microservice.com.example.entity.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

/**
 * Created by maiconoliveira on 18/08/16.
 */

@Component
public class PatientIntegration {

    @Autowired
    private PatientClient patientClient;

    public Collection<String> getPatientNamesFallback() {
        return Collections.emptyList();
    }

    @HystrixCommand(fallbackMethod = "getPatientNamesFallback")
    public Collection<String> getPatientsNames() {
        return patientClient.getPatients()
                .stream()
                .map(Patient::getName)
                .collect(Collectors.toList());
    }

    public String getMessageFallback(){
        return "OFF";
    }

    @HystrixCommand(fallbackMethod = "getMessageFallback")
    public String getMessage() {
        return patientClient.getMessage();
    }

}
