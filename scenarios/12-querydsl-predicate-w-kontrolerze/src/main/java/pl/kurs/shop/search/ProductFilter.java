package pl.kurs.shop.search;

import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kurs.shop.model.Product;
import pl.kurs.shop.model.ProductRepository;

/** A simple filter: a QueryDSL condition built by Spring Data from the URL parameters. */
@Service
@Transactional(readOnly = true)
public class ProductFilter {

    private final ProductRepository products;

    public ProductFilter(ProductRepository products) {
        this.products = products;
    }

    public ResultPage<ProductDto> filter(Predicate condition, Pageable pageable) {
        Page<Product> result = products.findAll(condition, pageable);
        return ResultPage.of(result.getContent().stream().map(ProductDto::from).toList(), result.getTotalElements(), pageable);
    }
}
