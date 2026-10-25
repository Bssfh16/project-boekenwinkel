package be.ehb.backend.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Reservatie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "gebruiker_id")
    private Gebruiker gebruiker;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Boek> gereserveerdeBoeken = new ArrayList<>();

    private LocalDate reservatieDatum;

    private LocalDate afhaalDatum;

    private boolean bevestigd;

    public Reservatie() {
    }

    public Reservatie(Gebruiker gebruiker, List<Boek> gereserveerdeBoeken, LocalDate reservatieDatum, LocalDate afhaalDatum, boolean bevestigd) {
        this.gebruiker = gebruiker;
        this.gereserveerdeBoeken = gereserveerdeBoeken;
        this.reservatieDatum = reservatieDatum;
        this.afhaalDatum = afhaalDatum;
        this.bevestigd = bevestigd;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Gebruiker getGebruiker() {
        return gebruiker;
    }

    public void setGebruiker(Gebruiker gebruiker) {
        this.gebruiker = gebruiker;
    }

    public List<Boek> getGereserveerdeBoeken() {
        return gereserveerdeBoeken;
    }

    public void setGereserveerdeBoeken(List<Boek> gereserveerdeBoeken) {
        this.gereserveerdeBoeken = gereserveerdeBoeken;
    }

    public LocalDate getReservatieDatum() {
        return reservatieDatum;
    }

    public void setReservatieDatum(LocalDate reservatieDatum) {
        this.reservatieDatum = reservatieDatum;
    }

    public LocalDate getAfhaalDatum() {
        return afhaalDatum;
    }

    public void setAfhaalDatum(LocalDate afhaalDatum) {
        this.afhaalDatum = afhaalDatum;
    }

    public boolean isBevestigd() {
        return bevestigd;
    }

    public void setBevestigd(boolean bevestigd) {
        this.bevestigd = bevestigd;
    }
}
