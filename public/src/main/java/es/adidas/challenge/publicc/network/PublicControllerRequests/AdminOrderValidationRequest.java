package es.adidas.challenge.publicc.network.PublicControllerRequests;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class AdminOrderValidationRequest {

    @NotBlank
    String username;

    @NotBlank
    String password;

    public AdminOrderValidationRequest(@NotBlank String username, @NotBlank String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
