package pl.kurs.shop.search;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;
import pl.kurs.shop.model.Category;
import pl.kurs.shop.model.Category_;
import pl.kurs.shop.model.Review;
import pl.kurs.shop.model.Review_;
import pl.kurs.shop.model.Product;
import pl.kurs.shop.model.Product_;
import pl.kurs.shop.model.Tag;
import pl.kurs.shop.model.Tag_;

import java.util.ArrayList;
import java.util.List;

/** Search conditions as a Specification. Spring Data uses it both for the page of results and for the count query. */
final class ProductSpecifications {

    private ProductSpecifications() {
    }

    static Specification<Product> matching(SearchCriteria k) {
        return (product, query, cb) -> {
            // category and manufacturer are fetched right away (fetch) to avoid N+1
            @SuppressWarnings("unchecked")
            Join<Product, Category> category = (Join<Product, Category>) product.fetch(Product_.category, JoinType.LEFT);
            product.fetch(Product_.manufacturer, JoinType.LEFT);
            List<Predicate> conditions = new ArrayList<>();
            if (StringUtils.hasText(k.name())) {
                conditions.add(cb.like(cb.lower(product.get(Product_.name)), LikePatterns.contains(k.name()), LikePatterns.ESCAPE));
            }
            if (StringUtils.hasText(k.category())) {
                conditions.add(cb.equal(cb.lower(category.get(Category_.name)), LikePatterns.lowerCase(k.category())));
            }
            if (k.priceFrom() != null) {
                conditions.add(cb.greaterThanOrEqualTo(product.get(Product_.price), k.priceFrom()));
            }
            if (k.priceTo() != null) {
                conditions.add(cb.lessThanOrEqualTo(product.get(Product_.price), k.priceTo()));
            }
            if (k.addedFrom() != null) {
                conditions.add(cb.greaterThanOrEqualTo(product.get(Product_.addedAt), k.addedFrom().atStartOfDay()));
            }
            if (k.addedTo() != null) {
                // the whole last day: everything before midnight of the next day
                conditions.add(cb.lessThan(product.get(Product_.addedAt), k.addedTo().plusDays(1).atStartOfDay()));
            }
            if (k.minRating() != null) {
                Subquery<Long> reviews = query.subquery(Long.class);
                Root<Review> review = reviews.from(Review.class);
                reviews.select(review.get(Review_.id))
                        .where(cb.equal(review.get(Review_.product), product),
                                cb.greaterThanOrEqualTo(review.get(Review_.rating), k.minRating()));
                conditions.add(cb.exists(reviews));
            }
            if (k.tags() != null) {
                for (String tagName : k.tags()) {
                    if (!StringUtils.hasText(tagName)) {
                        continue;
                    }
                    Subquery<Long> withTag = query.subquery(Long.class);
                    Root<Product> p = withTag.correlate(product);
                    Join<Product, Tag> tag = p.join(Product_.tags);
                    withTag.select(tag.get(Tag_.id))
                            .where(cb.equal(cb.lower(tag.get(Tag_.name)), LikePatterns.lowerCase(tagName)));
                    conditions.add(cb.exists(withTag));
                }
            }
            return cb.and(conditions.toArray(Predicate[]::new));
        };
    }
}
