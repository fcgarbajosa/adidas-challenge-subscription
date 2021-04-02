package es.adidas.challenge.subscription.network;

import es.adidas.challenge.subscription.business.SubscriptionService;
import es.adidas.challenge.subscription.business.repositories.entities.Subscription;
import es.adidas.challenge.subscription.network.services.EmailService;
import es.adidas.challenge.subscription.network.subscriptionControllerRequests.AdminOrderValidationRequest;
import es.adidas.challenge.subscription.network.subscriptionControllerRequests.NewSubscriptionRequest;
import es.adidas.challenge.subscription.network.subscriptionControllerRequests.UserEmailRequest;
import es.adidas.challenge.subscription.network.subscriptionControllerRequests.UserOrderValidationRequest;
import es.adidas.challenge.subscription.network.subscriptionControllerResponses.AllSubscriptionsActionResponse;
import es.adidas.challenge.subscription.network.subscriptionControllerResponses.SubscriptionActionResponse;
import es.adidas.challenge.subscription.network.subscriptionControllerResponses.SubscriptionResponse;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/subscription/service")
public class SubscriptionController {

    @Value("${adminUser}")
    private String adminUser;

    @Value("${adminPassword}")
    private String adminPassword;

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private EmailService emailService;

    private static final Logger log = LoggerFactory.getLogger(SubscriptionController.class);

    @PostMapping("/subscription")
    ResponseEntity<SubscriptionActionResponse> create(@RequestBody NewSubscriptionRequest newSubscriptionRequest) {

        // Validate parameters

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<NewSubscriptionRequest>> validated = validator.validate(newSubscriptionRequest);
        if (!validator.validate(newSubscriptionRequest).isEmpty()) {
            return new ResponseEntity<>(new SubscriptionActionResponse("Bad parameters"), HttpStatus.BAD_REQUEST);
        }

        if (subscriptionService.existsByEmail(newSubscriptionRequest.getEmail()))
            return new ResponseEntity<>(new SubscriptionActionResponse("Error. Email address already registered"), HttpStatus.BAD_REQUEST);

        Subscription subscription = new Subscription(newSubscriptionRequest.getEmail(),
                newSubscriptionRequest.getPassword(),
                newSubscriptionRequest.getFirstName(),
                newSubscriptionRequest.getGender(),
                newSubscriptionRequest.getDateOfBirth(),
                newSubscriptionRequest.getFlagOfConsent(),
                newSubscriptionRequest.getIdCampaign());

        try {

            Subscription storedSubscription = subscriptionService.create(subscription);

            // Create subscription

            SubscriptionResponse subscriptionResponse = new SubscriptionResponse(storedSubscription.getId(),
                    storedSubscription.getEmail(),
                    storedSubscription.getPassword(),
                    storedSubscription.getFirstName(),
                    storedSubscription.getGender(),
                    storedSubscription.getDateOfBirth(),
                    storedSubscription.getFlagOfConsent(),
                    storedSubscription.getIdCampaign());

            // Send email

            String sentEmail = emailService.sendEmail(storedSubscription.getEmail());
            log.info(sentEmail);
            return new ResponseEntity<>(new SubscriptionActionResponse("Subscription created", subscriptionResponse), HttpStatus.CREATED);
        } catch (Exception exception) {
            return new ResponseEntity<>(new SubscriptionActionResponse("Error. Mandatory parameter not sent"), HttpStatus.BAD_REQUEST);
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

        if (!subscriptionService.existsByEmail(userOrderValidationRequest.getEmail()))
            return new ResponseEntity<>(new SubscriptionActionResponse("Email is not registered"), HttpStatus.BAD_REQUEST);

        Optional<Subscription> subscription = subscriptionService.cancel(userOrderValidationRequest);
        return subscription.map(value -> new ResponseEntity<>(new SubscriptionActionResponse("Subscription canceled",
                new SubscriptionResponse(value.getId(),
                        value.getEmail(),
                        value.getPassword(),
                        value.getFirstName(),
                        value.getGender(),
                        value.getDateOfBirth(),
                        value.getFlagOfConsent(),
                        value.getIdCampaign())), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(new SubscriptionActionResponse("Error. Incorrect email/password or not registered"), HttpStatus.BAD_REQUEST));
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

        Optional<Subscription> subscription = subscriptionService.getByEmailAndPassword(userOrderValidationRequest);
        return subscription.map(value -> new ResponseEntity<>(new SubscriptionActionResponse("Ok",
                new SubscriptionResponse(value.getId(),
                        value.getEmail(),
                        value.getPassword(),
                        value.getFirstName(),
                        value.getGender(),
                        value.getDateOfBirth(),
                        value.getFlagOfConsent(),
                        value.getIdCampaign())), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(new SubscriptionActionResponse("Error. Incorrect email or password incorrect"), HttpStatus.BAD_REQUEST));
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


        if (adminUser.equals(adminOrderValidationRequest.getUsername()) && adminPassword.equals(adminOrderValidationRequest.getPassword())) {
            List<Subscription> subscriptions = subscriptionService.findAll();

            List<SubscriptionResponse> allSubscriptionsResponse =
                    subscriptions.stream().map(value ->
                            new SubscriptionResponse(value.getId(),
                                    value.getEmail(),
                                    value.getPassword(),
                                    value.getFirstName(),
                                    value.getGender(),
                                    value.getDateOfBirth(),
                                    value.getFlagOfConsent(),
                                    value.getIdCampaign())).collect(Collectors.toList());

            AllSubscriptionsActionResponse allSubscriptionsActionResponse = new AllSubscriptionsActionResponse("OK", allSubscriptionsResponse);
            return new ResponseEntity<>(allSubscriptionsActionResponse, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new AllSubscriptionsActionResponse("Error. Incorrect username or password"), HttpStatus.BAD_REQUEST);
        }
    }
}
