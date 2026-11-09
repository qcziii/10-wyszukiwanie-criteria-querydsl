package pl.kurs.shop.search;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.kurs.shop.model.QCategory;
import pl.kurs.shop.model.QProduct;

import java.util.List;

/** List of suggestions: DTOs straight from the database (projection), without loading entities. */
@Repository
@Transactional(readOnly = true)
public class ProductSummaries {

    private static final QProduct product = QProduct.product;
    private static final QCategory category = QCategory.category;

    private final JPAQueryFactory queryFactory;

    public ProductSummaries(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    public List<ProductSummary> all() {
        return queryFactory
                .select(Projections.bean(ProductSummary.class,
                        product.id, product.name, product.price, category.name))
                .from(product)
                .leftJoin(product.category, category)
                .orderBy(product.id.asc())
                .fetch();
    }
}
