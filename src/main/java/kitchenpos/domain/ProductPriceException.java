package kitchenpos.domain;

public class ProductPriceException extends IllegalArgumentException {
    public ProductPriceException(final String message) {
        super(message);
    }
}
