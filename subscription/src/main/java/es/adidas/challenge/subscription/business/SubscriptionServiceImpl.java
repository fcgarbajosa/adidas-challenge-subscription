package es.adidas.challenge.subscription.business;

import es.adidas.challenge.subscription.business.repositories.SubscriptionRepository;
import es.adidas.challenge.subscription.business.repositories.entities.Subscription;
import es.adidas.challenge.subscription.network.subscriptionControllerRequests.UserOrderValidationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SubscriptionServiceImpl implements SubscriptionService {

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    public Subscription create(Subscription subscription) {

        return subscriptionRepository.save(subscription);
    }

    public Optional<Subscription> getByEmailAndPassword(UserOrderValidationRequest userOrderValidationRequest) {

        return subscriptionRepository.findByEmailAndPassword(userOrderValidationRequest.getEmail(), userOrderValidationRequest.getPassword());
    }

    public List<Subscription> findAll() {

        return subscriptionRepository.findAll();
    }

    public Optional<Subscription> cancel(UserOrderValidationRequest userOrderValidationRequest) {

        Optional<Subscription> canceledSubscription = subscriptionRepository.findByEmailAndPassword(userOrderValidationRequest.getEmail(), userOrderValidationRequest.getPassword());
        canceledSubscription.ifPresent(subscription -> subscriptionRepository.deleteById(subscription.getId()));
        return canceledSubscription;
    }

    public boolean existsByEmail(String email) {

        return subscriptionRepository.existsByEmail(email);
    }
}
