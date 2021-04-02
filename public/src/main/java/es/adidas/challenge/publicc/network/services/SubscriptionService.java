package es.adidas.challenge.publicc.network.services;

import es.adidas.challenge.publicc.network.publicControllerRequests.AdminOrderValidationRequest;
import es.adidas.challenge.publicc.network.publicControllerRequests.NewSubscriptionRequest;
import es.adidas.challenge.publicc.network.publicControllerRequests.UserOrderValidationRequest;
import es.adidas.challenge.publicc.network.publicControllerResponses.AllSubscriptionsActionResponse;
import es.adidas.challenge.publicc.network.publicControllerResponses.SubscriptionActionResponse;

import java.util.Optional;

public interface SubscriptionService {

    public Optional<SubscriptionActionResponse> create(NewSubscriptionRequest newSubscriptionRequest);

    public Optional<SubscriptionActionResponse> cancel(UserOrderValidationRequest userOrderValidationRequest);

    public Optional<SubscriptionActionResponse> mySubscription(UserOrderValidationRequest userOrderValidationRequest);

    public AllSubscriptionsActionResponse allSubscriptions(AdminOrderValidationRequest adminOrderValidationRequest);
}
