package pl.kurs.sklep;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.context.WebApplicationContext;
import pl.kurs.sklep.model.Produkt;
import pl.kurs.sklep.model.ProduktRepository;
import pl.kurs.sklep.wyszukiwanie.ProduktDto;
import pl.kurs.sklep.wyszukiwanie.Strona;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.json.JsonMapper;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;

/** Wspólne narzędzia testów sprawdzających scenariusz. Nie zmieniaj tego pliku. */
@SpringBootTest(properties = {
        "spring.jpa.properties.hibernate.session_factory.statement_inspector=pl.kurs.sklep.SqlPodglad",
        "logging.level.org.hibernate.SQL=warn",
        "logging.level.org.hibernate.session.metrics=info"
})
abstract class TestWyszukiwarki {

    @Autowired
    private WebApplicationContext context;
    @Autowired
    private ProduktRepository produkty;
    @Autowired
    private TransactionTemplate transakcja;
    @Autowired
    private JsonMapper json;

    private MockMvc mvc;

    /** Odpowiedź z aplikacji i zapytania SQL, które wysłała do bazy podczas jej obsługi. */
    record Odpowiedz(int status, String body, List<String> sql) {

        boolean ok() {
            return status == 200;
        }

        String opis() {
            return "HTTP " + status + (body.isBlank() ? "" : ": " + body.substring(0, Math.min(400, body.length())));
        }
    }

    @BeforeEach
    void przygotujMockMvc() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    Odpowiedz get(String url) {
        SqlPodglad.wyczysc();
        try {
            MvcResult wynik = mvc.perform(MockMvcRequestBuilders.get(java.net.URI.create(url))).andReturn();
            return new Odpowiedz(wynik.getResponse().getStatus(),
                    wynik.getResponse().getContentAsString(StandardCharsets.UTF_8), SqlPodglad.zapytania());
        } catch (Exception e) {
            // wyjątek, którego nikt nie obsłużył, w działającej aplikacji kończy się odpowiedzią 500
            Throwable przyczyna = e;
            while (przyczyna.getCause() != null) {
                przyczyna = przyczyna.getCause();
            }
            return new Odpowiedz(500, przyczyna.getClass().getSimpleName() + ": " + przyczyna.getMessage(),
                    SqlPodglad.zapytania());
        }
    }

    Strona<ProduktDto> strona(Odpowiedz odpowiedz) {
        return json.readValue(odpowiedz.body(), new TypeReference<Strona<ProduktDto>>() {
        });
    }

    static List<Long> idki(Strona<ProduktDto> strona) {
        return strona.content().stream().map(ProduktDto::id).toList();
    }

    /** Odpowiedź będąca listą JSON zamieniona na listę obiektów. */
    <T> List<T> lista(Odpowiedz odpowiedz, Class<T> typ) {
        return json.readValue(odpowiedz.body(), json.getTypeFactory().constructCollectionType(List.class, typ));
    }

    /** Dla każdego produktu z bazy (po kolei według id) wartość policzona w Javie. */
    <T> List<T> zKazdegoProduktu(Function<Produkt, T> mapowanie) {
        return transakcja.execute(s -> produkty.findAll(Sort.by("id")).stream().map(mapowanie).toList());
    }

    /** Oczekiwane id produktów policzone w Javie na wszystkich produktach z bazy, posortowane rosnąco. */
    List<Long> oczekiwane(Predicate<Produkt> warunek) {
        return transakcja.execute(s -> produkty.findAll().stream()
                .filter(warunek)
                .map(Produkt::getId)
                .sorted()
                .toList());
    }

    /**
     * Sprawdza, że wyszukiwarka pod adresem url zwraca dokładnie te produkty, które spełniają warunek:
     * bez braków, bez nadmiarowych, bez powtórzeń i z poprawnym totalElements.
     */
    Strona<ProduktDto> wynikiZgodne(String url, Predicate<Produkt> warunek) {
        Odpowiedz odpowiedz = get(wszystkie(url));
        assertThat(odpowiedz.ok()).as("GET %s zwraca %s", url, odpowiedz.opis()).isTrue();
        Strona<ProduktDto> strona = strona(odpowiedz);
        List<Long> oczekiwane = oczekiwane(warunek);
        assertThat(idki(strona)).as("GET %s: produkty na liście się powtarzają", url).doesNotHaveDuplicates();
        assertThat(idki(strona).stream().sorted().toList()).as("GET %s: id znalezionych produktów", url)
                .isEqualTo(oczekiwane);
        assertThat(strona.totalElements()).as("GET %s: totalElements", url).isEqualTo(oczekiwane.size());
        return strona;
    }

    /** Wyniki wszystkich stron zebrane razem (size=100 mieści wszystkie produkty). */
    static String wszystkie(String url) {
        return url + (url.contains("?") ? "&" : "?") + "size=100";
    }
}
