package es.adidas.challenge.publicc.network.publicControllerRequests;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class UserOrderValidationRequest {

    @NotBlank
    @Email
    String email;

    @NotBlank
    String password;

    public UserOrderValidationRequest(@NotBlank @Email String email, @NotBlank String password) {
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
