package pl.kurs.shop.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class QuerydslConfig {

    /**
     * The injected EntityManager is Spring's shared proxy: in every transaction it
     * delegates to that transaction's EntityManager, so one factory is enough for the whole application.
     */
    @Bean
    JPAQueryFactory jpaQueryFactory(EntityManager entityManager) {
        return new JPAQueryFactory(entityManager);
    }
}
