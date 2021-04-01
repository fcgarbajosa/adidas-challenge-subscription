package es.adidas.challenge.publicc.network;

import es.adidas.challenge.publicc.network.PublicControllerRequests.AdminOrderValidationRequest;
import es.adidas.challenge.publicc.network.PublicControllerRequests.NewSubscriptionRequest;
import es.adidas.challenge.publicc.network.PublicControllerRequests.UserOrderValidationRequest;
import es.adidas.challenge.publicc.network.PublicControllerResponses.AllSubscriptionsActionResponse;
import es.adidas.challenge.publicc.network.PublicControllerResponses.SubscriptionActionResponse;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;


@RestController
@RequestMapping("/public/service")
public class PublicController {

    // admin user and password

    @Value("${adminUser}")
    private String adminUser;

    @Value("${adminPassword}")
    private String adminPassword;

    // Urls subscription service

    @Value("${urlSubscriptionCreate}")
    private String urlSubscriptionCreate;

    @Value("${urlSubscriptionCancel}")
    private String urlSubscriptionCancel;

    @Value("${urlSubscriptionMySubscription}")
    private String urlSubscriptionMySubscription;

    @Value("${urlSubscriptionAllSubscriptions}")
    private String urlSubscriptionAllSubscriptions;

    @Value("${urlSubscriptionCsrf}")
    private String urlSubscriptionCsrf;

    @Value("${urlSubscriptionSignin}")
    private String urlSubscriptionSignin;

    @PostMapping("/subscription")
    ResponseEntity<SubscriptionActionResponse> create(@RequestBody NewSubscriptionRequest newSubscriptionRequest) {

        // Validate parameters

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<NewSubscriptionRequest>> validated = validator.validate(newSubscriptionRequest);
        if (!validator.validate(newSubscriptionRequest).isEmpty()) {
            return new ResponseEntity<>(new SubscriptionActionResponse("Bad parameters"), HttpStatus.BAD_REQUEST);
        }

        // Create Subscription from subscription service

        RestTemplate restTemplate = new RestTemplate();

        // Set headers for restTemplate call

        HttpHeaders requestSubscriptionHeaders = setHeaders();

        // make new subscription

        HttpEntity<NewSubscriptionRequest> httpSubscriptionEntity = new HttpEntity<>(newSubscriptionRequest, requestSubscriptionHeaders);

        try {
            ResponseEntity<SubscriptionActionResponse> responseSubscription = restTemplate.exchange(
                    urlSubscriptionCreate,
                    HttpMethod.POST,
                    httpSubscriptionEntity,
                    SubscriptionActionResponse.class);

            SubscriptionActionResponse subscriptionActionResponse = responseSubscription.getBody();

            return new ResponseEntity<>(subscriptionActionResponse, responseSubscription.getStatusCode());
        }
        catch(HttpClientErrorException httpClientErrorException){
            return new ResponseEntity<>(new SubscriptionActionResponse("Email address already registered"), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/cancel")
    ResponseEntity<SubscriptionActionResponse> cancel(@RequestBody UserOrderValidationRequest userOrderValidationRequest) {

        // Validate parameters

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<UserOrderValidationRequest>> validated = validator.validate(userOrderValidationRequest);
        if (!validator.validate(userOrderValidationRequest).isEmpty()) {
            return new ResponseEntity<>(new SubscriptionActionResponse("Bad parameters"), HttpStatus.BAD_REQUEST);
        }

        // Cancel Subscription from subscription service

        RestTemplate restTemplate = new RestTemplate();

        // Set headers for restTemplate call

        HttpHeaders requestSubscriptionHeaders = setHeaders();

        // cancel subscription

        HttpEntity<UserOrderValidationRequest> httpSubscriptionEntity = new HttpEntity<>(userOrderValidationRequest, requestSubscriptionHeaders);

        try {
            ResponseEntity<SubscriptionActionResponse> responseSubscription = restTemplate.exchange(
                    urlSubscriptionCancel,
                    HttpMethod.DELETE,
                    httpSubscriptionEntity,
                    SubscriptionActionResponse.class);

            SubscriptionActionResponse subscriptionActionResponse = responseSubscription.getBody();

            return new ResponseEntity<>(subscriptionActionResponse, responseSubscription.getStatusCode());
        }
        catch(HttpClientErrorException httpClientErrorException){
            return new ResponseEntity<>(new SubscriptionActionResponse("Email address not registered"), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/mySubscription")
    ResponseEntity<SubscriptionActionResponse> getByEmailAndPassword(@RequestBody UserOrderValidationRequest userOrderValidationRequest) {

        // Validate parameters

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<UserOrderValidationRequest>> validated = validator.validate(userOrderValidationRequest);
        if (!validator.validate(userOrderValidationRequest).isEmpty()) {
            return new ResponseEntity<>(new SubscriptionActionResponse("Bad parameters"), HttpStatus.BAD_REQUEST);
        }

        // Get MySubscription from subscription service

        RestTemplate restTemplate = new RestTemplate();

        // Set headers for restTemplate call

        HttpHeaders requestSubscriptionHeaders = setHeaders();

        // make new subscription

        HttpEntity<UserOrderValidationRequest> httpSubscriptionEntity = new HttpEntity<>(userOrderValidationRequest, requestSubscriptionHeaders);

        try {
            ResponseEntity<SubscriptionActionResponse> responseSubscription = restTemplate.exchange(
                    urlSubscriptionMySubscription,
                    HttpMethod.POST,
                    httpSubscriptionEntity,
                    SubscriptionActionResponse.class);

            SubscriptionActionResponse subscriptionActionResponse = responseSubscription.getBody();

            return new ResponseEntity<>(subscriptionActionResponse, responseSubscription.getStatusCode());
        }
        catch(HttpClientErrorException httpClientErrorException){
            return new ResponseEntity<>(new SubscriptionActionResponse("Email address not registered"), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/allSubscriptions")
    ResponseEntity<AllSubscriptionsActionResponse> findAll(@RequestBody AdminOrderValidationRequest adminOrderValidationRequest) {

        // Validate parameters

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<AdminOrderValidationRequest>> validated = validator.validate(adminOrderValidationRequest);
        if (!validator.validate(adminOrderValidationRequest).isEmpty()) {
            return new ResponseEntity<>(new AllSubscriptionsActionResponse("Bad parameters"), HttpStatus.BAD_REQUEST);
        }

        // Get MySubscription from subscription service

        RestTemplate restTemplate = new RestTemplate();

        // Set headers for restTemplate call

        // Set headers for restTemplate call

        HttpHeaders requestSubscriptionHeaders = setHeaders();

        // Create new subscription

        HttpEntity<AdminOrderValidationRequest> httpSubscriptionEntity = new HttpEntity<>(adminOrderValidationRequest, requestSubscriptionHeaders);

        ResponseEntity<AllSubscriptionsActionResponse> responseSubscription = restTemplate.exchange(
                urlSubscriptionAllSubscriptions,
                HttpMethod.POST,
                httpSubscriptionEntity,
                AllSubscriptionsActionResponse.class);

        AllSubscriptionsActionResponse allSubscriptionActionResponse = responseSubscription.getBody();

        return new ResponseEntity<>(allSubscriptionActionResponse, responseSubscription.getStatusCode());

    }

    private HttpHeaders setHeaders() {

        String auth = adminUser + ":" + adminPassword;
        String encodedAuth = new String(Base64.encodeBase64(
                auth.getBytes(Charset.forName("US-ASCII"))));

        HttpHeaders requestSubscriptionHeaders = new HttpHeaders();

        requestSubscriptionHeaders.setContentType(MediaType.APPLICATION_JSON);
        requestSubscriptionHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        requestSubscriptionHeaders.set("Authorization", "Basic " + encodedAuth);

        return requestSubscriptionHeaders;
    }
}
