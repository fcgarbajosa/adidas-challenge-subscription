package es.adidas.challenge.publicc.network.PublicControllerRequests;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class UserMailRequest {
    @Email
    @NotBlank
    String emailAddress;

    public UserMailRequest(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

}
