package pl.kurs.sklep.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import org.hibernate.annotations.BatchSize;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "produkt")
public class Produkt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nazwa;

    private String opis;

    /** Cena sprzedaży brutto. */
    private BigDecimal cena;

    /** Cena zakupu od producenta. Poufna: nie może wyciekać przez API. */
    private BigDecimal cenaZakupu;

    private boolean dostepny;

    private LocalDateTime dodany;

    /** Kategoria jest opcjonalna: część produktów (np. świeżo zaimportowane) jeszcze jej nie ma. */
    @ManyToOne(fetch = FetchType.LAZY)
    private Kategoria kategoria;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Producent producent;

    @ManyToMany
    @JoinTable(name = "produkt_tag")
    @BatchSize(size = 50)
    private Set<Tag> tagi = new HashSet<>();

    @OneToMany(mappedBy = "produkt")
    private Set<Opinia> opinie = new HashSet<>();

    protected Produkt() {
    }

    public Long getId() {
        return id;
    }

    public String getNazwa() {
        return nazwa;
    }

    public String getOpis() {
        return opis;
    }

    public BigDecimal getCena() {
        return cena;
    }

    public BigDecimal getCenaZakupu() {
        return cenaZakupu;
    }

    public boolean isDostepny() {
        return dostepny;
    }

    public LocalDateTime getDodany() {
        return dodany;
    }

    public Kategoria getKategoria() {
        return kategoria;
    }

    public Producent getProducent() {
        return producent;
    }

    public Set<Tag> getTagi() {
        return tagi;
    }

    public Set<Opinia> getOpinie() {
        return opinie;
    }
}
