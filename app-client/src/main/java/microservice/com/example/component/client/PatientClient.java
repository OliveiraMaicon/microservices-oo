package microservice.com.example.component.client;

import microservice.com.example.entity.Patient;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Collection;

/**
 * Created by maiconoliveira on 18/08/16.
 */
@FeignClient(name = "patients-service")
public interface PatientClient {

    @RequestMapping(value = "/patients", method = RequestMethod.GET)
    Collection<Patient> getPatients();

    @RequestMapping(value = "/message", method = RequestMethod.GET)
    String getMessage();
}
