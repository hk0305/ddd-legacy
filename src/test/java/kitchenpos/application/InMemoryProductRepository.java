package kitchenpos.application;

import kitchenpos.domain.Product;
import kitchenpos.domain.ProductRepository;

import java.util.*;
import java.util.stream.Collectors;

public class InMemoryProductRepository implements ProductRepository {

    private Map<UUID, Product> products = new HashMap<>();

    public List<Product> findAllByIdIn(List<UUID> ids) {
        return products.values()
                .stream()
                .filter(it -> ids.contains(it.getId()))
                .collect(Collectors.toList());
    }

    public Product save(Product product) {
        return products.put(product.getId(), product);
    }

    public Optional<Product> findById(UUID id) {
        return Optional.ofNullable(products.get(id));
    }

    public List<Product> findAll() {
        return new ArrayList<>(products.values());
    }

}
