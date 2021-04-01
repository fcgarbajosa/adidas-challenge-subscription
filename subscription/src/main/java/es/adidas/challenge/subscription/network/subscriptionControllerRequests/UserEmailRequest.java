package es.adidas.challenge.subscription.network.subscriptionControllerRequests;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class UserEmailRequest {

    @NotBlank
    @Email
    String emailAddress;

    public UserEmailRequest(@NotBlank String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
}
