package pl.kurs.shop.search;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.kurs.shop.model.Product;
import pl.kurs.shop.model.ProductRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Product search on the JPA Criteria API, through a Specification from Spring Data JPA.
 * The conditions are in {@link ProductSpecifications}, paging and the count query are done by Spring Data.
 */
@Repository
@Transactional(readOnly = true)
public class CriteriaSearch {

    private final ProductRepository products;

    public CriteriaSearch(ProductRepository products) {
        this.products = products;
    }

    public ResultPage<ProductDto> search(SearchCriteria k, Pageable pageable) {
        PageRequest request = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                ordering(pageable.getSort()));
        Page<Product> result = products.findAll(ProductSpecifications.matching(k), request);
        return ResultPage.of(result.getContent().stream().map(ProductDto::from).toList(), result.getTotalElements(), pageable);
    }

    private static Sort ordering(Sort sort) {
        List<Sort.Order> orders = new ArrayList<>();
        for (Sort.Order o : sort) {
            String field = switch (o.getProperty()) {
                case "name", "price", "addedAt" -> o.getProperty();
                case "category" -> "category.name";
                default -> throw new DisallowedSortException(o.getProperty());
            };
            orders.add(new Sort.Order(o.getDirection(), field));
        }
        // id last: a stable order of products with the same price/name across pages
        orders.add(Sort.Order.asc("id"));
        return Sort.by(orders);
    }
}
