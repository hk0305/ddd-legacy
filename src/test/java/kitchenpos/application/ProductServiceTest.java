package kitchenpos.application;

import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.Product;
import kitchenpos.domain.ProductNameException;
import kitchenpos.domain.ProductRepository;
import kitchenpos.infra.ProfanityClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
//@SpringBootTest
//@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    //    @Mock
    private ProductRepository productRepository = new InMemoryProductRepository();

    //    @Mock
    private MenuRepository menuRepository = new InMemoryMenuRepository();

    //    @Mock
    private ProfanityClient purgomalumClient = new FakeProfanityClient();

    //    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp() throws Exception {
        productService = new ProductService(productRepository, menuRepository, purgomalumClient);
    }

    @DisplayName("상품을 생성한다.")
    @Test
    void create() {

        // given
//        given(productRepository.save(any())).willReturn(new Product());
        final Product product = new Product();
        product.setId(UUID.randomUUID());
        product.setName("후라이드 치킨");
        product.setPrice(BigDecimal.valueOf(18_000L));

        // when
        final Product actual = productService.create(product);

        // then
        // soft 테스트
        // 앞이 실패해도 뒤까지 모두 확인
        Assertions.assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getName()).isEqualTo("후라이드"),
                () -> assertThat(actual.getPrice()).isEqualTo(BigDecimal.valueOf(18_000L))
        );
    }

    // JUnit 플랫폼이 받는 인자를 변환해서 넣어준다! strings를 BigDecimal price로!
    @DisplayName("상품의 가격은 음수일 수 없다.")
    @ValueSource(ints = {-1, -100, -1000})
    @ParameterizedTest
    void create(final int price) {

        // given
        final Product product = createProductRequest(price);

        // when, then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> productService.create(product));
    }

    @DisplayName("상품의 이름에는 비속어가 포함될 수 없다.")
    @ValueSource(strings = {"욕설", "비속어가 포함된 단어"})
    @ParameterizedTest
    void create(final String name) {

        // given
        final Product product = new Product();
        product.setName(name);
        product.setPrice(BigDecimal.valueOf(18_000L));

        // Exception 위치가 궁금하다면 밖에서 빼서 실행해보자.
//        productService.create(product);

        // when, then. Exception 테스트 해보기
//        assertThatIllegalArgumentException()
//                .isThrownBy(() -> productService.create(product));

        // Exception class를 별도로 만들어서 테스트할수도 있다.
        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(ProductNameException.class);

    }

    @DisplayName("상품을 조회한다.")
    @Test
    void finalAll() {
        productRepository.save(createProduct("후라이드 치킨", 18_000));
        productRepository.save(createProduct("양념 치킨", 19_000));

        final List<Product> actual = productService.findAll();
        assertThat(actual).hasSize(2);
    }

    // 생성자 관심사에 따른 체이닝
    // 가격만 중요
    private Product createProductRequest(final int price) {
        return createProductRequest("후라이드", price);
    }

    private Product createProductRequest(final String name, final int price) {
        final Product product = new Product();
        product.setName(name);
        product.setPrice(BigDecimal.valueOf(price));
        return product;
    }

    public Product createProduct(final String name, final int price) {
        return createProduct(UUID.randomUUID(), name, BigDecimal.valueOf(price));
    }

    public Product createProduct(final UUID id, final String name, final BigDecimal price) {
        final Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setPrice(price);
        return product;
    }

}
