package pl.kurs.shop.search;

import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.kurs.shop.model.Product;

/**
 * Filter for the admin panel: URL parameters are entity field names,
 * e.g. {@code /api/products/filter?name=lapt&category.name=dom}.
 */
@RestController
class FilterController {

    private final ProductFilter filter;

    FilterController(ProductFilter filter) {
        this.filter = filter;
    }

    @GetMapping("/api/products/filter")
    ResultPage<ProductDto> filter(@QuerydslPredicate(root = Product.class) Predicate condition, Pageable pageable) {
        return filter.filter(condition, pageable);
    }
}
