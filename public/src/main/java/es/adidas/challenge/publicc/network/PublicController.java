package es.adidas.challenge.publicc.network;

import es.adidas.challenge.publicc.network.publicControllerRequests.AdminOrderValidationRequest;
import es.adidas.challenge.publicc.network.publicControllerRequests.NewSubscriptionRequest;
import es.adidas.challenge.publicc.network.publicControllerRequests.UserOrderValidationRequest;
import es.adidas.challenge.publicc.network.publicControllerResponses.AllSubscriptionsActionResponse;
import es.adidas.challenge.publicc.network.publicControllerResponses.SubscriptionActionResponse;
import es.adidas.challenge.publicc.network.services.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Optional;


@RestController
@RequestMapping("/public/service")
public class PublicController {

    @Autowired
    private SubscriptionService subscriptionService;

    @PostMapping("/subscription")
    ResponseEntity<SubscriptionActionResponse> create(@RequestBody NewSubscriptionRequest newSubscriptionRequest) {

        // Validate parameters

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        if (!validator.validate(newSubscriptionRequest).isEmpty()) {
            return new ResponseEntity<>(new SubscriptionActionResponse("Bad parameters"), HttpStatus.BAD_REQUEST);
        }

        Optional<SubscriptionActionResponse> optionalSubscriptionActionResponse = subscriptionService.create(newSubscriptionRequest);

        return optionalSubscriptionActionResponse.map(subscriptionActionResponse -> new ResponseEntity<>(subscriptionActionResponse, HttpStatus.CREATED))
                .orElseGet(() -> new ResponseEntity<>(new SubscriptionActionResponse("Email address already registered-"), HttpStatus.BAD_REQUEST));
    }

    @DeleteMapping("/cancel")
    ResponseEntity<SubscriptionActionResponse> cancel(@RequestBody UserOrderValidationRequest userOrderValidationRequest) {

        // Validate parameters

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        if (!validator.validate(userOrderValidationRequest).isEmpty()) {
            return new ResponseEntity<>(new SubscriptionActionResponse("Bad parameters"), HttpStatus.BAD_REQUEST);
        }

        // Cancel Subscription from subscription service

        Optional<SubscriptionActionResponse> optionalSubscriptionActionResponse = subscriptionService.cancel(userOrderValidationRequest);

        return optionalSubscriptionActionResponse.map(subscriptionActionResponse -> new ResponseEntity<>(subscriptionActionResponse, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(new SubscriptionActionResponse("Email address not registered"), HttpStatus.BAD_REQUEST));
    }

    @PostMapping("/mySubscription")
    ResponseEntity<SubscriptionActionResponse> mySubscription(@RequestBody UserOrderValidationRequest userOrderValidationRequest) {

        // Validate parameters

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        if (!validator.validate(userOrderValidationRequest).isEmpty()) {
            return new ResponseEntity<>(new SubscriptionActionResponse("Bad parameters"), HttpStatus.BAD_REQUEST);
        }

        Optional<SubscriptionActionResponse> optionalSubscriptionActionResponse = subscriptionService.mySubscription(userOrderValidationRequest);

        return optionalSubscriptionActionResponse.map(subscriptionActionResponse -> new ResponseEntity<>(subscriptionActionResponse, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(new SubscriptionActionResponse("Email address not registered"), HttpStatus.BAD_REQUEST));
    }

    @PostMapping("/allSubscriptions")
    ResponseEntity<AllSubscriptionsActionResponse> allSubscriptions(@RequestBody AdminOrderValidationRequest adminOrderValidationRequest) {

        // Validate parameters

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        if (!validator.validate(adminOrderValidationRequest).isEmpty()) {
            return new ResponseEntity<>(new AllSubscriptionsActionResponse("Bad parameters"), HttpStatus.BAD_REQUEST);
        }

        AllSubscriptionsActionResponse allSubscriptionsActionResponse = subscriptionService.allSubscriptions(adminOrderValidationRequest);

        return new ResponseEntity<>(allSubscriptionsActionResponse, HttpStatus.OK);
    }
}
