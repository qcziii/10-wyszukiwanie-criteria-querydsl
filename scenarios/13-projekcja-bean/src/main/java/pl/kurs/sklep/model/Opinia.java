package pl.kurs.sklep.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "opinia")
public class Opinia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Produkt produkt;

    /** Ocena w gwiazdkach, od 1 do 5. */
    private int ocena;

    private String autor;

    protected Opinia() {
    }

    public Long getId() {
        return id;
    }

    public Produkt getProdukt() {
        return produkt;
    }

    public int getOcena() {
        return ocena;
    }

    public String getAutor() {
        return autor;
    }
}
