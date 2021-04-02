package es.adidas.challenge.subscription.network.services;

import es.adidas.challenge.subscription.network.subscriptionControllerRequests.UserEmailRequest;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.Collections;

@Service
public class EmailServiceImpl implements EmailService {

    @Value("${adminUser}")
    private String adminUser;

    @Value("${adminPassword}")
    private String adminPassword;

    @Value("${urlEmailSend}")
    private String urlEmailSend;

    public String sendEmail(String emailAddress) {
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
