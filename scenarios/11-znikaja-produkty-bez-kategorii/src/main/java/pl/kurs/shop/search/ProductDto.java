package pl.kurs.shop.search;

import pl.kurs.shop.model.Product;
import pl.kurs.shop.model.Tag;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record ProductDto(Long id, String name, BigDecimal price, LocalDateTime addedAt,
                         String category, String manufacturer, List<String> tags) {

    public static ProductDto from(Product p) {
        return new ProductDto(
                p.getId(),
                p.getName(),
                p.getPrice(),
                p.getAddedAt(),
                p.getCategory() == null ? null : p.getCategory().getName(),
                p.getManufacturer().getName(),
                p.getTags().stream().map(Tag::getName).sorted().toList());
    }
}
