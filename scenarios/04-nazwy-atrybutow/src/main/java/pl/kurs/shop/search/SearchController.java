package pl.kurs.shop.search;

import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The same search in two versions. Both must return exactly the same results.
 * Paging and sorting: {@code ?page=0&size=20&sort=price,desc}.
 */
@RestController
@RequestMapping("/api/products")
class SearchController {

    private final CriteriaSearch criteria;
    private final QuerydslSearch querydsl;

    SearchController(CriteriaSearch criteria, QuerydslSearch querydsl) {
        this.criteria = criteria;
        this.querydsl = querydsl;
    }

    /** JPA Criteria API version. */
    @GetMapping("/search")
    ResultPage<ProductDto> search(SearchCriteria searchCriteria, Pageable pageable) {
        return criteria.search(searchCriteria, pageable);
    }

    /** QueryDSL version. */
    @GetMapping("/search-qdsl")
    ResultPage<ProductDto> searchQuerydsl(SearchCriteria searchCriteria, Pageable pageable) {
        return querydsl.search(searchCriteria, pageable);
    }
}
