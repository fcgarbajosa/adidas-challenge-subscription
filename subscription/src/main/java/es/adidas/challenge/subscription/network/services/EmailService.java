package es.adidas.challenge.subscription.network.services;

import es.adidas.challenge.subscription.network.subscriptionControllerRequests.UserEmailRequest;

public interface EmailService {

    String sendEmail(String emailAddress);
}
