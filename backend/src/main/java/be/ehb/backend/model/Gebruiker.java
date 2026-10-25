package be.ehb.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Gebruiker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "Gebruikersnaam mag niet leeg zijn!")
    @Column(unique = true)
    private String gebruikersnaam;

    @Email(message = "Ongeldig e-mailadres!")
    @NotBlank(message = "Email mag niet leeg zijn!")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "Wachtwoord mag niet leeg zijn!")
    @JsonIgnore
    private String wachtwoord;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Boek> gekochteBoeken = new ArrayList<>();

    public Gebruiker() {
    }

    public Gebruiker(String gebruikersnaam, String email, String wachtwoord) {
        this.gebruikersnaam = gebruikersnaam;
        this.email = email;
        this.wachtwoord = wachtwoord;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGebruikersnaam() {
        return gebruikersnaam;
    }

    public void setGebruikersnaam(String gebruikersnaam) {
        this.gebruikersnaam = gebruikersnaam;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWachtwoord() {
        return wachtwoord;
    }

    public void setWachtwoord(String wachtwoord) {
        this.wachtwoord = wachtwoord;
    }

    public List<Boek> getGekochteBoeken() {
        return gekochteBoeken;
    }

    public void setGekochteBoeken(List<Boek> gekochteBoeken) {
        this.gekochteBoeken = gekochteBoeken;
    }
}



