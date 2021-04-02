package es.adidas.challenge.subscription.network.subscriptionControllerResponses;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

public class SubscriptionResponse {

    @NotBlank
    private Long id;

    @NotBlank
    private String email;

    @NotBlank
    @Column(nullable = false)
    private String password;

    private String firstName;
    private String gender;

    @NotNull
    @Column(nullable = false)
    private Date dateOfBirth;

    @NotBlank
    @Column(nullable = false)
    private String flagOfConsent;

    @NotBlank
    @Column(nullable = false)
    private Long idCampaign;

    public SubscriptionResponse(@NotBlank Long id, @NotBlank String email, @NotBlank String password, String firstName, String gender, @NotNull Date dateOfBirth, @NotBlank String flagOfConsent, @NotBlank Long idCampaign) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.flagOfConsent = flagOfConsent;
        this.idCampaign = idCampaign;
    }

    public SubscriptionResponse() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getIdCampaign() { return idCampaign; }

    public void setIdCampaign(Long idCampaign) {
        this.idCampaign = idCampaign;
    }

}
