package pl.kurs.sklep.wyszukiwanie;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
class ListaController {

    private final ListaProduktow lista;

    ListaController(ListaProduktow lista) {
        this.lista = lista;
    }

    /** Wszystkie produkty w lekkiej postaci, do podpowiedzi w polu wyszukiwania. */
    @GetMapping("/api/produkty/lista")
    List<ProduktSkrot> lista() {
        return lista.wszystkie();
    }
}
