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
import pl.kurs.shop.model.Category_;
import pl.kurs.shop.model.Review;
import pl.kurs.shop.model.Review_;
import pl.kurs.shop.model.Product;
import pl.kurs.shop.model.Product_;
import pl.kurs.shop.model.Tag;
import pl.kurs.shop.model.Tag_;

import java.util.ArrayList;
import java.util.List;

/** Product search on the JPA Criteria API (with the metamodel Product_, Category_, ...). */
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
        @SuppressWarnings("unchecked")
        Join<Product, Category> category = (Join<Product, Category>) product.fetch(Product_.category, JoinType.LEFT);
        product.fetch(Product_.manufacturer, JoinType.LEFT);
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
        Join<Product, Category> categoryCount = productCount.join(Product_.category, JoinType.LEFT);
        count.select(cb.count(productCount))
                .where(conditions(cb, count, productCount, categoryCount, k).toArray(Predicate[]::new));
        long total = em.createQuery(count).getSingleResult();

        return ResultPage.of(results, total, pageable);
    }

    private List<Predicate> conditions(CriteriaBuilder cb, CriteriaQuery<?> query, Root<Product> product,
                                    Join<Product, Category> category, SearchCriteria k) {
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
            // EXISTS instead of JOIN: a product with several matching reviews is still one row
            Subquery<Long> reviews = query.subquery(Long.class);
            Root<Review> review = reviews.from(Review.class);
            reviews.select(review.get(Review_.id))
                    .where(cb.equal(review.get(Review_.product), product),
                            cb.greaterThanOrEqualTo(review.get(Review_.rating), k.minRating()));
            conditions.add(cb.exists(reviews));
        }
        if (k.tags() != null && !k.tags().isEmpty()) {
            // products with the given tags; distinct, because a join over a collection repeats the product
            Join<Product, Tag> tag = product.join(Product_.tags);
            conditions.add(cb.lower(tag.get(Tag_.name)).in(k.tags().stream().map(LikePatterns::lowerCase).toList()));
            query.distinct(true);
        }
        return conditions;
    }

    private List<Order> ordering(CriteriaBuilder cb, Root<Product> product,
                                   Join<Product, Category> category, Sort sort) {
        List<Order> orders = new ArrayList<>();
        for (Sort.Order o : sort) {
            Expression<?> field = switch (o.getProperty()) {
                case "name" -> product.get(Product_.name);
                case "price" -> product.get(Product_.price);
                case "addedAt" -> product.get(Product_.addedAt);
                case "category" -> category.get(Category_.name);
                default -> throw new DisallowedSortException(o.getProperty());
            };
            orders.add(o.isAscending() ? cb.asc(field) : cb.desc(field));
        }
        // id last: a stable order of products with the same price/name across pages
        orders.add(cb.asc(product.get(Product_.id)));
        return orders;
    }
}
