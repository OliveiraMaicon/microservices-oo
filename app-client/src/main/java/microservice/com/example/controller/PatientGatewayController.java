package microservice.com.example.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import microservice.com.example.component.PatientIntegration;
import microservice.com.example.entity.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

/**
 * Created by maiconoliveira on 14/08/16.
 */
@RestController
@RequestMapping("/patients")
public class PatientGatewayController {

    @Autowired
    @LoadBalanced
    public RestTemplate restTemplate;

    @Output(Source.OUTPUT)
    @Autowired
    private MessageChannel messageChannel;


    @Autowired
    public PatientIntegration patientIntegration;



    /* FEIGN EXAMPLE */

    @RequestMapping("/names")
    public Collection<String> getPatientNames(){
        return patientIntegration.getPatientsNames();
    }


    @RequestMapping("/message")
    public String getMessage(){
        return patientIntegration.getMessage();
    }


    /*
        examplo message chanel
    *
    *   rabbitmq-server start
    */

    @RequestMapping(method = RequestMethod.POST)
    public void send(@RequestBody Patient p){
        System.out.println("p = [" + p.toString() + "]");
        messageChannel.send(MessageBuilder.withPayload(p.getName()).build());

    }
}
