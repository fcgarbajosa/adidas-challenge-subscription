package es.adidas.challenge.publicc.network.PublicControllerRequests;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class UserOrderValidationRequest {

    @Email
    @NotBlank
    String email;

    @Email
    @NotBlank
    String password;

    public UserOrderValidationRequest(String email, String password) {
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
