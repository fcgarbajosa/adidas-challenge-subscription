package es.adidas.challenge.subscription.network;

import es.adidas.challenge.subscription.business.SubscriptionService;
import es.adidas.challenge.subscription.business.repositories.entities.Subscription;
import es.adidas.challenge.subscription.network.security.authControllerRequests.SignInRequest;
import es.adidas.challenge.subscription.network.security.authControllerResponses.JwtResponse;
import es.adidas.challenge.subscription.network.security.services.UserService;
import es.adidas.challenge.subscription.network.subscriptionControllerRequests.AdminOrderValidationRequest;
import es.adidas.challenge.subscription.network.subscriptionControllerRequests.NewSubscriptionRequest;
import es.adidas.challenge.subscription.network.subscriptionControllerRequests.UserEmailRequest;
import es.adidas.challenge.subscription.network.subscriptionControllerRequests.UserOrderValidationRequest;
import es.adidas.challenge.subscription.network.subscriptionControllerResponses.AllSubscriptionsActionResponse;
import es.adidas.challenge.subscription.network.subscriptionControllerResponses.SubscriptionActionResponse;
import es.adidas.challenge.subscription.network.subscriptionControllerResponses.SubscriptionResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/subscription/service")
public class SubscriptionController {

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private UserService userService;

    @Value("${adminUser}")
    private String adminUser;

    @Value("${adminPassword}")
    private String adminPassword;

    @Value("${urlEmailCsrf}")
    private String urlEmailCsrf;

    @Value("${urlEmailSignin}")
    private String urlEmailSignin;

    @Value("${urlEmailSend}")
    private String urlEmailSend;

    private static final Logger log = LoggerFactory.getLogger(SubscriptionController.class);

    @Autowired
    PasswordEncoder encoder;

    @PostMapping("/subscription")
    ResponseEntity<SubscriptionActionResponse> create(@Valid @RequestBody NewSubscriptionRequest newSubscriptionRequest, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(new SubscriptionActionResponse("Errors on the request"), HttpStatus.BAD_REQUEST);
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

            String sentEmail = sendEmail(storedSubscription.getEmail());
            log.info(sentEmail);
            return new ResponseEntity<>(new SubscriptionActionResponse("Subscription created", subscriptionResponse), HttpStatus.CREATED);
        } catch (Exception exception) {
            return new ResponseEntity<>(new SubscriptionActionResponse("Error. Mandatory parameter not sent"), HttpStatus.BAD_REQUEST);
        }
    }

    private String sendEmail(String emailAddress) {

        // Get MySubscription from subscription service

        RestTemplate restTemplate = new RestTemplate();

        // Set headers for restTemplate call

        HttpHeaders requestEmailHeaders = setHeaders();

        // Send email

        UserEmailRequest userEmailRequest = new UserEmailRequest(emailAddress);
        HttpEntity<UserEmailRequest> httpEmailEntity = new HttpEntity<>(userEmailRequest, requestEmailHeaders);

        HttpEntity<String> responseEmailIdentification = restTemplate.exchange(urlEmailSend,
                HttpMethod.POST,
                httpEmailEntity,
                String.class);
        return responseEmailIdentification.getBody();
    }

    private HttpHeaders setHeaders() {

        // Get csrf cookie first

        String csrfToken = getCsrfToken();

        // Get Authentification token

        String jwebToken = getAuthentificationToken(csrfToken);

        HttpHeaders requestEmailHeaders = new HttpHeaders();

        requestEmailHeaders.setContentType(MediaType.APPLICATION_JSON);
        requestEmailHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        requestEmailHeaders.set("Authorization", "Bearer " + jwebToken);
        requestEmailHeaders.set("X-XSRF-TOKEN", csrfToken);
        requestEmailHeaders.set("Cookie", "XSRF-TOKEN=" + csrfToken);

        return requestEmailHeaders;
    }

    private String getCsrfToken() {

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders requestCsrfHeaders = new HttpHeaders();
        requestCsrfHeaders.setContentType(MediaType.APPLICATION_JSON);
        requestCsrfHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<String> httpCsrfEntity = new HttpEntity<>(requestCsrfHeaders);
        ResponseEntity<String> response = restTemplate.exchange(urlEmailCsrf, HttpMethod.GET, httpCsrfEntity, String.class);

        String cookieValueCsrf = Optional.ofNullable(response.getHeaders().getFirst(HttpHeaders.SET_COOKIE)).orElse("");

        cookieValueCsrf = cookieValueCsrf.replace("XSRF-TOKEN=", "");
        return cookieValueCsrf.split(";")[0];
    }

    private String getAuthentificationToken(String csrfToken) {

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders requestAuthHeaders = new HttpHeaders();
        requestAuthHeaders.setContentType(MediaType.APPLICATION_JSON);
        requestAuthHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        requestAuthHeaders.set("X-XSRF-TOKEN", csrfToken);
        requestAuthHeaders.set("Cookie", "XSRF-TOKEN=" + csrfToken);

        SignInRequest signInRequest = new SignInRequest(adminUser, adminPassword);

        HttpEntity<SignInRequest> httpAuthEntity = new HttpEntity<>(signInRequest, requestAuthHeaders);
        HttpEntity<JwtResponse> responseAdminIdentification = restTemplate.exchange(urlEmailSignin, HttpMethod.POST, httpAuthEntity, JwtResponse.class);

        return Objects.requireNonNull(responseAdminIdentification.getBody()).getAccessToken();
    }


    @DeleteMapping("/cancel")
    ResponseEntity<SubscriptionActionResponse> cancel(@Valid @RequestBody UserOrderValidationRequest userOrderValidationRequest, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(new SubscriptionActionResponse("Errors on the data request"), HttpStatus.BAD_REQUEST);
        }

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
    ResponseEntity<SubscriptionActionResponse> getByEmailAndPassword(@Valid @RequestBody UserOrderValidationRequest userOrderValidationRequest, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(new SubscriptionActionResponse("Errors on the request"), HttpStatus.BAD_REQUEST);
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
    ResponseEntity<AllSubscriptionsActionResponse> findAll(@Valid @RequestBody AdminOrderValidationRequest adminOrderValidationRequest, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(new AllSubscriptionsActionResponse("Errors on the request"), HttpStatus.BAD_REQUEST);
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
