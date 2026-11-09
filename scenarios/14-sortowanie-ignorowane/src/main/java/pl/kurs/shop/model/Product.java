package pl.kurs.shop.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import org.hibernate.annotations.BatchSize;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    /** Gross selling price. */
    private BigDecimal price;

    /** Purchase price from the manufacturer. Confidential: must not leak through the API. */
    private BigDecimal purchasePrice;

    private boolean available;

    private LocalDateTime addedAt;

    /** The category is optional: some products (e.g. freshly imported ones) do not have one yet. */
    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Manufacturer manufacturer;

    @ManyToMany
    @JoinTable(name = "product_tag")
    @BatchSize(size = 50)
    private Set<Tag> tags = new HashSet<>();

    @OneToMany(mappedBy = "product")
    private Set<Review> reviews = new HashSet<>();

    protected Product() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public BigDecimal getPurchasePrice() {
        return purchasePrice;
    }

    public boolean isAvailable() {
        return available;
    }

    public LocalDateTime getAddedAt() {
        return addedAt;
    }

    public Category getCategory() {
        return category;
    }

    public Manufacturer getManufacturer() {
        return manufacturer;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public Set<Review> getReviews() {
        return reviews;
    }
}
