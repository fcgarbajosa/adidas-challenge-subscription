package es.adidas.challenge.publicc.network.PublicControllerResponses;



public class SubscriptionActionResponse {

    String message;
    SubscriptionResponse subscriptionResponse;

    public SubscriptionActionResponse(String message, SubscriptionResponse subscription) {
        this.message = message;
        this.subscriptionResponse = subscription;
    }

    public SubscriptionActionResponse(){

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
        return subscriptionResponse;
    }

    public void setSubscription(SubscriptionResponse subscription) {
        this.subscriptionResponse = subscription;
    }
}
