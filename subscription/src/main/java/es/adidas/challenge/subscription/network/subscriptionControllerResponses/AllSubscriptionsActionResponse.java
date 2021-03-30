package es.adidas.challenge.subscription.network.subscriptionControllerResponses;

import java.util.List;

public class AllSubscriptionsActionResponse {

    String message;
    List<SubscriptionResponse> subscription;

    public AllSubscriptionsActionResponse(String message, List<SubscriptionResponse> subscription) {
        this.message = message;
        this.subscription = subscription;
    }

    public AllSubscriptionsActionResponse(String message) {
        this.message = message;
    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<SubscriptionResponse> getSubscription() {
        return subscription;
    }

    public void setSubscription(List<SubscriptionResponse> subscription) {
        this.subscription = subscription;
    }
}
