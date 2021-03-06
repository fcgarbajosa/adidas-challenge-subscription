package es.adidas.challenge.publicc.network.publicControllerResponses;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class SubscriptionResponse {

    @NotBlank
    private Long id;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;

    private String firstName;
    private String gender;

    @NotBlank
    private String dateOfBirth;

    @NotBlank
    private String flagOfConsent;

    @NotBlank
    private Long idCampaign;

    public SubscriptionResponse(@NotBlank Long id, @NotBlank String email, @NotBlank String password, String firstName, String gender, @NotBlank String dateOfBirth, @NotBlank String flagOfConsent, @NotBlank Long idCampaign) {
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

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
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
