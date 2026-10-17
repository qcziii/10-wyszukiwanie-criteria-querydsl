package pl.kurs.sklep.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProduktRepository extends JpaRepository<Produkt, Long>, JpaSpecificationExecutor<Produkt> {
}
