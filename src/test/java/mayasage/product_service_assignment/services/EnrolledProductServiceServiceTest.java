package mayasage.product_service_assignment.services;

import mayasage.product_service_assignment.dtos.EnrolledProductServiceDto;
import mayasage.product_service_assignment.exceptions.UnexpectedException;
import mayasage.product_service_assignment.models.Product;
import mayasage.product_service_assignment.models.Service;
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

import java.util.List;

@SpringBootTest
@Transactional
@Commit
public class EnrolledProductServiceServiceTest {

        @Autowired
        private ProductRepository productRepository;

        @Autowired
        private ServiceRepository serviceRepository;

        @Autowired
        private EnrolledProductServiceService enrolledProductServiceService;

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
        public void modifyEnrolledProductService() {
                Product productP1 = productRepository.findProductByProductName("p1");
                Service serviceS1 = serviceRepository.findServiceByServiceName("s1");
                Service serviceS2 = serviceRepository.findServiceByServiceName("s2");

                Assertions.assertEquals(2, productP1.getProductServiceBundles().size());

                productP1.getProductServiceBundles().forEach(
                        productServiceBundle ->
                                Assertions.assertNull(productServiceBundle.getEnrolledProductServiceBundle())
                );

                enrolledProductServiceService.modifyEnrolledProductService(
                        List.of(
                                new EnrolledProductServiceDto.ModifyOneEnrolledProductServiceBundle(
                                        productP1.getProductId(),
                                        serviceS1.getServiceId(),
                                        true
                                ),
                                new EnrolledProductServiceDto.ModifyOneEnrolledProductServiceBundle(
                                        productP1.getProductId(),
                                        serviceS2.getServiceId(),
                                        false
                                )
                        )
                );

                productP1.getProductServiceBundles().forEach(
                        productServiceBundle -> {
                                Long serviceId = productServiceBundle.getProductServiceBundleId().getServiceId();
                                if (serviceId.equals(serviceS1.getServiceId())) {
                                        Assertions.assertNotNull(productServiceBundle.getEnrolledProductServiceBundle());
                                } else if (serviceId.equals(serviceS2.getServiceId())) {
                                        Assertions.assertNull(productServiceBundle.getEnrolledProductServiceBundle());
                                } else {
                                        throw new UnexpectedException();
                                }
                        }
                );

                /*

                        CURRENT_ASSOCIATION:

                        productP1 -> product_service_bundle_1 (serviceS1) -> enrolled_product_service_bundle_1 (1:1)
                                  -> product_service_bundle_2 (serviceS2) -> enrolled_product_service_bundle_2 (1:1)

                 */

        }

}
