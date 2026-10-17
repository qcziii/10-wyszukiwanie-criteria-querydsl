package pl.kurs.sklep.wyszukiwanie;

import java.math.BigDecimal;

/** Lekka wersja produktu do listy podpowiedzi (JavaBean: konstruktor bez argumentów i settery). */
public class ProduktSkrot {

    private Long id;
    private String nazwa;
    private BigDecimal cena;
    private String kategoria;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    public BigDecimal getCena() {
        return cena;
    }

    public void setCena(BigDecimal cena) {
        this.cena = cena;
    }

    public String getKategoria() {
        return kategoria;
    }

    public void setKategoria(String kategoria) {
        this.kategoria = kategoria;
    }
}
