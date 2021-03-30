package es.adidas.challenge.subscription.network.subscriptionControllerResponses;

import es.adidas.challenge.subscription.business.repositories.entities.Subscription;

public class SubscriptionActionResponse {

    String message;
    SubscriptionResponse subscription;

    public SubscriptionActionResponse(String message, SubscriptionResponse subscription) {
        this.message = message;
        this.subscription = subscription;
    }
    public SubscriptionActionResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public SubscriptionResponse getSubscription() {
        return subscription;
    }

    public void setSubscription(SubscriptionResponse subscription) {
        this.subscription = subscription;
    }
}
