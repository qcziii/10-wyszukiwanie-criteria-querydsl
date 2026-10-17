package pl.kurs.sklep.konfiguracja;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class QuerydslKonfiguracja {

    /**
     * Wstrzyknięty EntityManager to współdzielony proxy Springa: w każdej transakcji
     * deleguje do EntityManagera tej transakcji, więc jedna fabryka wystarcza dla całej aplikacji.
     */
    @Bean
    JPAQueryFactory jpaQueryFactory(EntityManager entityManager) {
        return new JPAQueryFactory(entityManager);
    }
}
