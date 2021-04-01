package es.adidas.challenge.email.network.emailControllerRequests;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class UserEmailRequest {

    @NotBlank
    @Email
    String emailAddress;

    public UserEmailRequest(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public UserEmailRequest() {

    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
}
