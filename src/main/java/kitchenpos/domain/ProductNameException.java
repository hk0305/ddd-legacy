package kitchenpos.domain;

public class ProductNameException extends IllegalArgumentException {
    public ProductNameException(final String message) {
        super(message);
    }
}
