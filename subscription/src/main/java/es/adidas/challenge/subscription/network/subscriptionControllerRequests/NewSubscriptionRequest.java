package es.adidas.challenge.subscription.network.subscriptionControllerRequests;

import javax.validation.constraints.*;
import java.util.Date;

public class NewSubscriptionRequest {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;

    private String firstName;
    private String gender;

    @NotNull
    private Date dateOfBirth;

    @NotBlank
    private String flagOfConsent;

    @NotNull
    private Long idCampaign;

    public NewSubscriptionRequest(@Email @NotBlank String email, @NotBlank String password, String firstName, String gender, @NotNull Date dateOfBirth, @NotBlank String flagOfConsent, @NotBlank Long idCampaign) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.flagOfConsent = flagOfConsent;
        this.idCampaign = idCampaign;
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getFlagOfConsent() {
        return flagOfConsent;
    }

    public void setFlagOfConsent(String flagOfConsent) {
        this.flagOfConsent = flagOfConsent;
    }

    public Long getIdCampaign() {
        return idCampaign;
    }

    public void setIdCampaign(Long idCampaign) {
        this.idCampaign = idCampaign;
    }

}
