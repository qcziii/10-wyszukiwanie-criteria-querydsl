package pl.kurs.shop.search;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
class SummaryController {

    private final ProductSummaries list;

    SummaryController(ProductSummaries list) {
        this.list = list;
    }

    /** All products in a lightweight form, for suggestions in the search box. */
    @GetMapping("/api/products/list")
    List<ProductSummary> list() {
        return list.all();
    }
}
