package es.adidas.challenge.subscription.network.subscriptionControllerRequests;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class UserOrderValidationRequest {

    @Email
    @NotBlank
    String email;

    @NotBlank
    String password;

    public UserOrderValidationRequest(@Email @NotBlank String email, @NotBlank String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
