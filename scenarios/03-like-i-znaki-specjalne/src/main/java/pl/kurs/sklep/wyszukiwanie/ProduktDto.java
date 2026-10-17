package pl.kurs.sklep.wyszukiwanie;

import pl.kurs.sklep.model.Produkt;
import pl.kurs.sklep.model.Tag;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record ProduktDto(Long id, String nazwa, BigDecimal cena, LocalDateTime dodany,
                         String kategoria, String producent, List<String> tagi) {

    public static ProduktDto z(Produkt p) {
        return new ProduktDto(
                p.getId(),
                p.getNazwa(),
                p.getCena(),
                p.getDodany(),
                p.getKategoria() == null ? null : p.getKategoria().getNazwa(),
                p.getProducent().getNazwa(),
                p.getTagi().stream().map(Tag::getNazwa).sorted().toList());
    }
}
