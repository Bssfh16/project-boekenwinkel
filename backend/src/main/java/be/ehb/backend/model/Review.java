package be.ehb.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

/**
 * Model voor een review, waarvan de score 1-5, van een gereserveerd boek door een gebruiker.
 */
@Entity
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "boek_isbn")
    private Boek boek;

    @ManyToOne
    @JoinColumn(name = "gebruiker_id")
    private Gebruiker gebruiker;

    @Min(value = 1, message = "Score moet minstens 1 zijn!")
    @Max(value = 5, message = "Score mag maximaal 5 zijn!")
    private int score;

    @NotBlank(message = "Review tekst mag niet leeg zijn!")
    @Column(length = 640)
    private String tekst;

    private LocalDate datum;

    public Review() {
    }

    /**
     * Een nieuwe review aanmaken met automatisch de datum van vandaag.
     */
    public Review(Boek boek, Gebruiker gebruiker, int score, String tekst) {
        this.boek = boek;
        this.gebruiker = gebruiker;
        this.score = score;
        this.tekst = tekst;
        this.datum = LocalDate.now();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Boek getBoek() {
        return boek;
    }

    public void setBoek(Boek boek) {
        this.boek = boek;
    }

    public Gebruiker getGebruiker() {
        return gebruiker;
    }

    public void setGebruiker(Gebruiker gebruiker) {
        this.gebruiker = gebruiker;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getTekst() {
        return tekst;
    }

    public void setTekst(String tekst) {
        this.tekst = tekst;
    }

    public LocalDate getDatum() {
        return datum;
    }

    public void setDatum(LocalDate datum) {
        this.datum = datum;
    }
}
