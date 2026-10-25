package be.ehb.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

@Entity
public class Boek {

    @Id
    @NotBlank(message = "ISBN-nummer mag niet leeg zijn!")
    private String isbn;

    @NotBlank(message = "Titel mag niet leeg zijn!")
    private String titel;

    @NotBlank(message = "Auteurs mogen niet leeg zijn!")
    private String auteurs;

    @Column(length = 640)
    private String descriptie;

    @Positive(message = "Prijs moet groter zijn dan 0!")
    private double prijs;

    @Min(value = 0, message = "Stock kan niet negatief zijn!")
    private int stock;

    private String genre;

    public Boek() {
    }

    public Boek(String isbn, String titel, String auteurs, String descriptie, double prijs, int stock, String genre) {
        this.isbn = isbn;
        this.titel = titel;
        this.auteurs = auteurs;
        this.descriptie = descriptie;
        this.prijs = prijs;
        this.stock = stock;
        this.genre = genre;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public String getAuteurs() {
        return auteurs;
    }

    public void setAuteurs(String auteurs) {
        this.auteurs = auteurs;
    }

    public String getDescriptie() {
        return descriptie;
    }

    public void setDescriptie(String descriptie) {
        this.descriptie = descriptie;
    }

    public double getPrijs() {
        return prijs;
    }

    public void setPrijs(double prijs) {
        this.prijs = prijs;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
}
