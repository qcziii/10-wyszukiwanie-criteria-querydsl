package pl.kurs.shop.search;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import pl.kurs.shop.model.Category;
import pl.kurs.shop.model.Review;
import pl.kurs.shop.model.Product;
import pl.kurs.shop.model.Tag;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/** Product search on the JPA Criteria API. */
@Repository
@Transactional(readOnly = true)
public class CriteriaSearch {

    private final EntityManager em;

    public CriteriaSearch(EntityManager em) {
        this.em = em;
    }

    public ResultPage<ProductDto> search(SearchCriteria k, Pageable pageable) {
        CriteriaBuilder cb = em.getCriteriaBuilder();

        // 1. The page of results. Category and manufacturer are fetched right away (fetch) to avoid N+1.
        CriteriaQuery<Product> query = cb.createQuery(Product.class);
        Root<Product> product = query.from(Product.class);
        Join<Product, Category> category = (Join<Product, Category>) product.<Product, Category>fetch("category", JoinType.LEFT);
        product.fetch("manufacturer", JoinType.LEFT);
        query.select(product)
                .where(conditions(cb, query, product, category, k).toArray(Predicate[]::new))
                .orderBy(ordering(cb, product, category, pageable.getSort()));
        List<ProductDto> results = em.createQuery(query)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList().stream()
                .map(ProductDto::from)
                .toList();

        // 2. Total number of results: a separate query with the same conditions, without fetch and sorting.
        CriteriaQuery<Long> count = cb.createQuery(Long.class);
        Root<Product> productCount = count.from(Product.class);
        Join<Product, Category> categoryCount = productCount.join("category", JoinType.LEFT);
        count.select(cb.count(productCount))
                .where(conditions(cb, count, productCount, categoryCount, k).toArray(Predicate[]::new));
        long total = em.createQuery(count).getSingleResult();

        return ResultPage.of(results, total, pageable);
    }

    private List<Predicate> conditions(CriteriaBuilder cb, CriteriaQuery<?> query, Root<Product> product,
                                    Join<Product, Category> category, SearchCriteria k) {
        List<Predicate> conditions = new ArrayList<>();
        if (StringUtils.hasText(k.name())) {
            conditions.add(cb.like(cb.lower(product.<String>get("name")), LikePatterns.contains(k.name()), LikePatterns.ESCAPE));
        }
        if (StringUtils.hasText(k.category())) {
            conditions.add(cb.equal(cb.lower(category.<String>get("name")), LikePatterns.lowerCase(k.category())));
        }
        if (k.priceFrom() != null) {
            conditions.add(cb.greaterThanOrEqualTo(product.<BigDecimal>get("grossPrice"), k.priceFrom()));
        }
        if (k.priceTo() != null) {
            conditions.add(cb.lessThanOrEqualTo(product.<BigDecimal>get("grossPrice"), k.priceTo()));
        }
        if (k.addedFrom() != null) {
            conditions.add(cb.greaterThanOrEqualTo(product.<LocalDateTime>get("addedAt"), k.addedFrom().atStartOfDay()));
        }
        if (k.addedTo() != null) {
            // the whole last day: everything before midnight of the next day
            conditions.add(cb.lessThan(product.<LocalDateTime>get("addedAt"), k.addedTo().plusDays(1).atStartOfDay()));
        }
        if (k.minRating() != null) {
            // EXISTS instead of JOIN: a product with several matching reviews is still one row
            Subquery<Long> reviews = query.subquery(Long.class);
            Root<Review> review = reviews.from(Review.class);
            reviews.select(review.<Long>get("id"))
                    .where(cb.equal(review.get("product"), product),
                            cb.greaterThanOrEqualTo(review.<Integer>get("rating"), k.minRating()));
            conditions.add(cb.exists(reviews));
        }
        if (k.tags() != null) {
            // the product must have EVERY one of the tags: a separate EXISTS for each tag
            for (String tagName : k.tags()) {
                if (!StringUtils.hasText(tagName)) {
                    continue;
                }
                Subquery<Long> withTag = query.subquery(Long.class);
                Root<Product> p = withTag.correlate(product);
                Join<Product, Tag> tag = p.join("tags");
                withTag.select(tag.<Long>get("id"))
                        .where(cb.equal(cb.lower(tag.<String>get("name")), LikePatterns.lowerCase(tagName)));
                conditions.add(cb.exists(withTag));
            }
        }
        return conditions;
    }

    private List<Order> ordering(CriteriaBuilder cb, Root<Product> product,
                                   Join<Product, Category> category, Sort sort) {
        List<Order> orders = new ArrayList<>();
        for (Sort.Order o : sort) {
            Expression<?> field = switch (o.getProperty()) {
                case "name" -> product.get("name");
                case "price" -> product.<BigDecimal>get("grossPrice");
                case "addedAt" -> product.<LocalDateTime>get("addedAt");
                case "category" -> category.get("name");
                default -> throw new DisallowedSortException(o.getProperty());
            };
            orders.add(o.isAscending() ? cb.asc(field) : cb.desc(field));
        }
        // id last: a stable order of products with the same price/name across pages
        orders.add(cb.asc(product.get("id")));
        return orders;
    }
}
