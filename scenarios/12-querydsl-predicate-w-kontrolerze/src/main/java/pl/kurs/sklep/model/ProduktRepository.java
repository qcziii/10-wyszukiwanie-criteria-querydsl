package pl.kurs.sklep.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface ProduktRepository extends JpaRepository<Produkt, Long>, QuerydslPredicateExecutor<Produkt> {
}
