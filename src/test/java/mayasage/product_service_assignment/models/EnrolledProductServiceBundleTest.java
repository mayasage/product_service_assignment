package mayasage.product_service_assignment.models;

import mayasage.product_service_assignment.repositories.ProductRepository;
import mayasage.product_service_assignment.repositories.ServiceRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

@SpringBootTest
@Transactional
@Commit
public class EnrolledProductServiceBundleTest {

        @Autowired
        private ProductRepository productRepository;

        @Autowired
        private ServiceRepository serviceRepository;

        @BeforeEach
        public void createEntriesBeforeEach() {
                Product productP1 = new Product("p1");
                Service serviceS1 = new Service("s1");
                Service serviceS2 = new Service("s2");

                productRepository.save(productP1);
                serviceRepository.save(serviceS1);
                serviceRepository.save(serviceS2);

                productP1.addService(serviceS1);
                productP1.addService(serviceS2);
        }

        @AfterEach
        public void deleteEntriesAfterEach() {
                productRepository.deleteByProductName("p1");
                serviceRepository.deleteByServiceName("s1");
                serviceRepository.deleteByServiceName("s2");
        }

        @Test
        public void testForNonUniqueObjectException1() {
                Product productP1 = productRepository.findProductByProductName("p1");
                Service serviceS1 = serviceRepository.findServiceByServiceName("s1");
                Service serviceS2 = serviceRepository.findServiceByServiceName("s2");

                Assertions.assertAll(
                        () -> Assertions.assertNotNull(productP1),
                        () -> Assertions.assertNotNull(serviceS1),
                        () -> Assertions.assertNotNull(serviceS2)
                );

                Assertions.assertEquals(2, productP1.getProductServiceBundles().size());

                productP1.getProductServiceBundles().forEach(
                        productServiceBundle ->
                                Assertions.assertNull(productServiceBundle.getEnrolledProductServiceBundle())
                );
        }

        @Test
        public void simplyCreatedSuccessfully() {
                Product productP1 = productRepository.findProductByProductName("p1");
                Service serviceS1 = serviceRepository.findServiceByServiceName("s1");

                productP1.getProductServiceBundles().forEach(
                        productServiceBundle -> {
                                Assertions.assertNull(productServiceBundle.getEnrolledProductServiceBundle());
                                if (
                                        productServiceBundle
                                                .getProductServiceBundleId()
                                                .getServiceId()
                                                .equals(serviceS1.getServiceId())
                                ) {
                                        productServiceBundle.enrollProductServiceBundle();
                                        Assertions.assertNotNull(productServiceBundle.getEnrolledProductServiceBundle());
                                }
                        }
                );
        }

        @Test
        public void cascadeFromProduct() {
                Product productP1 = productRepository.findProductByProductName("p1");
                Service serviceS1 = serviceRepository.findServiceByServiceName("s1");

                Assertions.assertEquals(1, serviceS1.getProductServiceBundles().size());

                productP1
                        .getProductServiceBundles()
                        .stream()
                        .filter(
                                productServiceBundle ->
                                        productServiceBundle
                                                .getProductServiceBundleId()
                                                .getServiceId()
                                                .equals(serviceS1.getServiceId())
                        )
                        .forEach(ProductServiceBundle::enrollProductServiceBundle);

                productRepository.removeProductByProductName("p1");

                Assertions.assertEquals(0, productP1.getProductServiceBundles().size());
        }

        @Test
        public void cascadeFromService() {
                Product productP1 = productRepository.findProductByProductName("p1");
                Service serviceS1 = serviceRepository.findServiceByServiceName("s1");

                Assertions.assertEquals(2, productP1.getProductServiceBundles().size());

                serviceS1
                        .getProductServiceBundles()
                        .forEach(ProductServiceBundle::enrollProductServiceBundle);

                serviceRepository.deleteServicesByServiceName("s1");

                Assertions.assertEquals(1, productP1.getProductServiceBundles().size());
        }
}
