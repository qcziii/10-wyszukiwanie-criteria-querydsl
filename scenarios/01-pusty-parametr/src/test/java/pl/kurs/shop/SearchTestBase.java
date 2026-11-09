package pl.kurs.shop;

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
import pl.kurs.shop.model.Product;
import pl.kurs.shop.model.ProductRepository;
import pl.kurs.shop.search.ProductDto;
import pl.kurs.shop.search.ResultPage;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.json.JsonMapper;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;

/** Shared tools for the tests that check the scenario. Do not change this file. */
@SpringBootTest(properties = {
        "spring.jpa.properties.hibernate.session_factory.statement_inspector=pl.kurs.shop.SqlRecorder",
        "logging.level.org.hibernate.SQL=warn",
        "logging.level.org.hibernate.session.metrics=info"
})
abstract class SearchTestBase {

    @Autowired
    private WebApplicationContext context;
    @Autowired
    private ProductRepository products;
    @Autowired
    private TransactionTemplate transaction;
    @Autowired
    private JsonMapper json;

    private MockMvc mvc;

    /** The application response and the SQL queries it sent to the database while handling it. */
    record Response(int status, String body, List<String> sql) {

        boolean ok() {
            return status == 200;
        }

        String description() {
            return "HTTP " + status + (body.isBlank() ? "" : ": " + body.substring(0, Math.min(400, body.length())));
        }
    }

    @BeforeEach
    void setUpMockMvc() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    Response get(String url) {
        SqlRecorder.clear();
        try {
            MvcResult result = mvc.perform(MockMvcRequestBuilders.get(java.net.URI.create(url))).andReturn();
            return new Response(result.getResponse().getStatus(),
                    result.getResponse().getContentAsString(StandardCharsets.UTF_8), SqlRecorder.queries());
        } catch (Exception e) {
            // an unhandled exception ends with a 500 response in a running application
            Throwable cause = e;
            while (cause.getCause() != null) {
                cause = cause.getCause();
            }
            return new Response(500, cause.getClass().getSimpleName() + ": " + cause.getMessage(),
                    SqlRecorder.queries());
        }
    }

    ResultPage<ProductDto> page(Response response) {
        return json.readValue(response.body(), new TypeReference<ResultPage<ProductDto>>() {
        });
    }

    static List<Long> ids(ResultPage<ProductDto> page) {
        return page.content().stream().map(ProductDto::id).toList();
    }

    /** A response that is a JSON list, converted into a list of objects. */
    <T> List<T> list(Response response, Class<T> type) {
        return json.readValue(response.body(), json.getTypeFactory().constructCollectionType(List.class, type));
    }

    /** For every product in the database (ordered by id) a value computed in Java. */
    <T> List<T> fromEachProduct(Function<Product, T> mapping) {
        return transaction.execute(s -> products.findAll(Sort.by("id")).stream().map(mapping).toList());
    }

    /** Expected product ids computed in Java over all products in the database, sorted ascending. */
    List<Long> expected(Predicate<Product> condition) {
        return transaction.execute(s -> products.findAll().stream()
                .filter(condition)
                .map(Product::getId)
                .sorted()
                .toList());
    }

    /**
     * Checks that the search at url returns exactly the products matching the condition:
     * none missing, none extra, no duplicates and a correct totalElements.
     */
    ResultPage<ProductDto> resultsMatch(String url, Predicate<Product> condition) {
        Response response = get(all(url));
        assertThat(response.ok()).as("GET %s zwraca %s", url, response.description()).isTrue();
        ResultPage<ProductDto> page = page(response);
        List<Long> expected = expected(condition);
        assertThat(ids(page)).as("GET %s: produkty na liście się powtarzają", url).doesNotHaveDuplicates();
        assertThat(ids(page).stream().sorted().toList()).as("GET %s: id znalezionych produktów", url)
                .isEqualTo(expected);
        assertThat(page.totalElements()).as("GET %s: totalElements", url).isEqualTo(expected.size());
        return page;
    }

    /** Results of all pages together (size=100 holds all products). */
    static String all(String url) {
        return url + (url.contains("?") ? "&" : "?") + "size=100";
    }
}
