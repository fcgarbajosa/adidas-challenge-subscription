package es.adidas.challenge.publicc.network;

import es.adidas.challenge.publicc.network.PublicControllerRequests.*;
import es.adidas.challenge.publicc.network.PublicControllerResponses.AllSubscriptionsActionResponse;
import es.adidas.challenge.publicc.network.PublicControllerResponses.SubscriptionActionResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;


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
    ResponseEntity<SubscriptionActionResponse> create(@Valid @RequestBody NewSubscriptionRequest newSubscriptionRequest, BindingResult bindingResult) {

        // Create Subscription from subscription service

        RestTemplate restTemplate = new RestTemplate();

        // Set headers for restTemplate call

        HttpHeaders requestSubscriptionHeaders = setHeaders();

        // make new subscription

        HttpEntity<NewSubscriptionRequest> httpSubscriptionEntity = new HttpEntity<>(newSubscriptionRequest, requestSubscriptionHeaders);

        ResponseEntity<SubscriptionActionResponse> responseSubscription = restTemplate.exchange(
                urlSubscriptionCreate,
                HttpMethod.POST,
                httpSubscriptionEntity,
                SubscriptionActionResponse.class);

        SubscriptionActionResponse subscriptionActionResponse = responseSubscription.getBody();

        return new ResponseEntity<>(subscriptionActionResponse, responseSubscription.getStatusCode());
    }


    @DeleteMapping("/cancel")
    ResponseEntity<SubscriptionActionResponse> cancel(@RequestBody UserOrderValidationRequest userOrderValidationRequest) {

        // Cancel Subscription from subscription service

        RestTemplate restTemplate = new RestTemplate();

        // Set headers for restTemplate call

        HttpHeaders requestSubscriptionHeaders = setHeaders();

        // cancel subscription

        HttpEntity<UserOrderValidationRequest> httpSubscriptionEntity = new HttpEntity<>(userOrderValidationRequest, requestSubscriptionHeaders);

        ResponseEntity<SubscriptionActionResponse> responseSubscription = restTemplate.exchange(
                urlSubscriptionCancel,
                HttpMethod.DELETE,
                httpSubscriptionEntity,
                SubscriptionActionResponse.class);

        SubscriptionActionResponse subscriptionActionResponse = responseSubscription.getBody();

        return new ResponseEntity<>(subscriptionActionResponse, responseSubscription.getStatusCode());

    }

    @PostMapping("/mySubscription")
    ResponseEntity<SubscriptionActionResponse> getByEmailAndPassword(@RequestBody UserOrderValidationRequest userOrderValidationRequest) {

        // Get MySubscription from subscription service

        RestTemplate restTemplate = new RestTemplate();

        // Set headers for restTemplate call

        HttpHeaders requestSubscriptionHeaders = setHeaders();

        // make new subscription

        HttpEntity<UserOrderValidationRequest> httpSubscriptionEntity = new HttpEntity<>(userOrderValidationRequest, requestSubscriptionHeaders);

        ResponseEntity<SubscriptionActionResponse> responseSubscription = restTemplate.exchange(
                urlSubscriptionMySubscription,
                HttpMethod.POST,
                httpSubscriptionEntity,
                SubscriptionActionResponse.class);

        SubscriptionActionResponse subscriptionActionResponse = responseSubscription.getBody();

        return new ResponseEntity<>(subscriptionActionResponse, responseSubscription.getStatusCode());

    }

    @PostMapping("/allSubscriptions")
    ResponseEntity<AllSubscriptionsActionResponse> findAll(@RequestBody AdminOrderValidationRequest adminOrderValidationRequest) {

        // Get MySubscription from subscription service

        RestTemplate restTemplate = new RestTemplate();

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

        // Get csrf cookie first

        String csrfToken = getCsrfToken();

        // Get Authentification token

        String jwebToken = getAuthentificationToken(csrfToken);

        HttpHeaders requestSubscriptionHeaders = new HttpHeaders();

        requestSubscriptionHeaders.setContentType(MediaType.APPLICATION_JSON);
        requestSubscriptionHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        requestSubscriptionHeaders.set("Authorization", "Bearer " + jwebToken);
        requestSubscriptionHeaders.set("X-XSRF-TOKEN", csrfToken);
        requestSubscriptionHeaders.set("Cookie", "XSRF-TOKEN=" + csrfToken);

        return requestSubscriptionHeaders;
    }

    private String getCsrfToken() {

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders requestCsrfHeaders = new HttpHeaders();
        requestCsrfHeaders.setContentType(MediaType.APPLICATION_JSON);
        requestCsrfHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<String> httpCsrfEntity = new HttpEntity<>(requestCsrfHeaders);
        ResponseEntity<String> response = restTemplate.exchange(urlSubscriptionCsrf, HttpMethod.GET
                , httpCsrfEntity, String.class);

        String cookieValueCsrf =  Optional.ofNullable(response.getHeaders().getFirst(HttpHeaders.SET_COOKIE)).orElse("");

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
        HttpEntity<JwtResponse> responseAdminIdentification = restTemplate.exchange(urlSubscriptionSignin, HttpMethod.POST, httpAuthEntity, JwtResponse.class);


        return Objects.requireNonNull(responseAdminIdentification.getBody()).getAccessToken();
    }

}
