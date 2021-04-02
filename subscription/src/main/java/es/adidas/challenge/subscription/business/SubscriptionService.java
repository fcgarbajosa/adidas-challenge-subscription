package es.adidas.challenge.subscription.business;

import es.adidas.challenge.subscription.business.repositories.entities.Subscription;
import es.adidas.challenge.subscription.network.subscriptionControllerRequests.UserOrderValidationRequest;

import java.util.List;
import java.util.Optional;

public interface SubscriptionService {

    public Subscription create(Subscription subscription);

    public Optional<Subscription> cancel(UserOrderValidationRequest userOrderValidationRequest);

    public Optional<Subscription> getByEmailAndPassword(UserOrderValidationRequest userOrderValidationRequest);

    public List<Subscription> findAll();

    public boolean existsByEmail(String email);
}
