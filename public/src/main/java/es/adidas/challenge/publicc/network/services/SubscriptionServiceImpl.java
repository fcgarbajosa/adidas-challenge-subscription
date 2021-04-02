package es.adidas.challenge.publicc.network.services;

import es.adidas.challenge.publicc.network.publicControllerRequests.AdminOrderValidationRequest;
import es.adidas.challenge.publicc.network.publicControllerRequests.NewSubscriptionRequest;
import es.adidas.challenge.publicc.network.publicControllerRequests.UserOrderValidationRequest;
import es.adidas.challenge.publicc.network.publicControllerResponses.AllSubscriptionsActionResponse;
import es.adidas.challenge.publicc.network.publicControllerResponses.SubscriptionActionResponse;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Optional;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {

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

    public Optional<SubscriptionActionResponse> create(NewSubscriptionRequest newSubscriptionRequest) {

        RestTemplate restTemplate = new RestTemplate();

        // Set headers for restTemplate call

        HttpHeaders requestSubscriptionHeaders = setHeaders();

        // Create Subscription from subscription service

        HttpEntity<NewSubscriptionRequest> httpSubscriptionEntity = new HttpEntity<>(newSubscriptionRequest, requestSubscriptionHeaders);

        Optional<SubscriptionActionResponse> optionalSubscriptionActionResponse;
        try {
            ResponseEntity<SubscriptionActionResponse> responseSubscription = restTemplate.exchange(
                    urlSubscriptionCreate,
                    HttpMethod.POST,
                    httpSubscriptionEntity,
                    SubscriptionActionResponse.class);

            optionalSubscriptionActionResponse = Optional.ofNullable(responseSubscription.getBody());

        } catch (HttpClientErrorException httpClientErrorException) {
            optionalSubscriptionActionResponse = Optional.empty();
        }

        return optionalSubscriptionActionResponse;
    }

    public Optional<SubscriptionActionResponse> cancel(UserOrderValidationRequest userOrderValidationRequest) {

        RestTemplate restTemplate = new RestTemplate();

        // Set headers for restTemplate call

        HttpHeaders requestSubscriptionHeaders = setHeaders();

        // cancel subscription

        HttpEntity<UserOrderValidationRequest> httpSubscriptionEntity = new HttpEntity<>(userOrderValidationRequest, requestSubscriptionHeaders);

        Optional<SubscriptionActionResponse> optionalSubscriptionActionResponse;
        try {
            ResponseEntity<SubscriptionActionResponse> responseSubscription = restTemplate.exchange(
                    urlSubscriptionCancel,
                    HttpMethod.DELETE,
                    httpSubscriptionEntity,
                    SubscriptionActionResponse.class);

            optionalSubscriptionActionResponse = Optional.ofNullable(responseSubscription.getBody());

        } catch (HttpClientErrorException httpClientErrorException) {
            optionalSubscriptionActionResponse = Optional.empty();
        }
        return optionalSubscriptionActionResponse;
    }

    public Optional<SubscriptionActionResponse> mySubscription(UserOrderValidationRequest userOrderValidationRequest) {

        RestTemplate restTemplate = new RestTemplate();

        // Set headers for restTemplate call

        HttpHeaders requestSubscriptionHeaders = setHeaders();

        // Get MySubscription from subscription service

        HttpEntity<UserOrderValidationRequest> httpSubscriptionEntity = new HttpEntity<>(userOrderValidationRequest, requestSubscriptionHeaders);

        Optional<SubscriptionActionResponse> subscriptionActionResponse;

        try {
            ResponseEntity<SubscriptionActionResponse> responseSubscription = restTemplate.exchange(
                    urlSubscriptionMySubscription,
                    HttpMethod.POST,
                    httpSubscriptionEntity,
                    SubscriptionActionResponse.class);

            subscriptionActionResponse = Optional.ofNullable(responseSubscription.getBody());

        } catch (HttpClientErrorException httpClientErrorException) {
            subscriptionActionResponse = Optional.empty();
        }

        return subscriptionActionResponse;
    }

    public AllSubscriptionsActionResponse allSubscriptions(AdminOrderValidationRequest adminOrderValidationRequest) {

        // Get AllSubscription from subscription service

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

        return responseSubscription.getBody();

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
