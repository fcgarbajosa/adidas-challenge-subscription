package es.adidas.challenge.subscription.business.repositories.entities;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "SUBSCRIPTION")
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @Column(unique=true, nullable = false)
    @NotBlank
    private String email;

    @NotBlank
    @Column(nullable = false)
    private String password;

    private String firstName;
    private String gender;

    @NotBlank
    @Column(nullable = false)
    private String dateOfBirth;

    @NotBlank
    @Column(nullable = false)
    private String flagOfConsent;

    @NotBlank
    @Column(nullable = false)
    private Long idCampaign;

    public Subscription(@NotBlank @NotNull(message = "Email cannot be null") String email, @NotBlank @NotNull(message = "Password cannot be null") String password, String firstName, String gender, @NotBlank @NotNull(message = "dateOfBirth cannot be null") String dateOfBirth, @NotBlank @NotNull(message = "flagOfConsent cannot be null") String flagOfConsent, @NotBlank @NotNull(message = "idCampaign cannot be null") Long idCampaign) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.flagOfConsent = flagOfConsent;
        this.idCampaign = idCampaign;
    }

    public Subscription(){
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
