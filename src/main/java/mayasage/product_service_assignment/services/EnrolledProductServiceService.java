package mayasage.product_service_assignment.services;

import mayasage.product_service_assignment.dtos.EnrolledProductServiceDto;
import mayasage.product_service_assignment.exceptions.ProductServiceBundleNotFoundException;
import mayasage.product_service_assignment.models.ProductServiceBundle;
import mayasage.product_service_assignment.models.composite_keys.ProductServiceBundleId;
import mayasage.product_service_assignment.repositories.EnrolledProductServiceBundleRepository;
import mayasage.product_service_assignment.repositories.ProductServiceBundleRepository;
import mayasage.product_service_assignment.repositories.projections.ProductServiceProjection;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EnrolledProductServiceService {

        private final ProductServiceBundleRepository productServiceBundleRepository;
        private final EnrolledProductServiceBundleRepository enrolledProductServiceBundleRepository;

        public List<ProductServiceProjection.ListProductServiceBundle> findAllProductServices() {
                return productServiceBundleRepository.findAllProductServices();
        }

        public List<ProductServiceProjection.ListProductServiceBundle> findProductServiceByProductId(@NotNull Long productId) {
                return productServiceBundleRepository.findProductServiceBundleByProductId(productId);
        }

        public List<ProductServiceProjection.ListEnrolledProductServiceBundle> findAllEnrolledProductServiceBundles() {
                return productServiceBundleRepository.findAllEnrolledProductServiceBundles();
        }

        public List<ProductServiceProjection.ListEnrolledProductServiceBundle> findEnrolledProductServiceByProductId(@NotNull Long productId) {
                return productServiceBundleRepository.findEnrolledProductServiceBundleByProductId(productId);
        }

        @Transactional
        public void modifyEnrolledProductService(
                @NotNull
                List<EnrolledProductServiceDto.ModifyOneEnrolledProductServiceBundle>
                        modifyEnrolledProductServiceBundlesRequest
        ) {
                modifyEnrolledProductServiceBundlesRequest.forEach(
                        modifyOneEnrolledProductServiceBundle -> {
                                ProductServiceBundle productServiceBundle =
                                        productServiceBundleRepository
                                                .findById(
                                                        new ProductServiceBundleId(
                                                                modifyOneEnrolledProductServiceBundle.getProductId(),
                                                                modifyOneEnrolledProductServiceBundle.getServiceId()
                                                        )
                                                )
                                                .orElseThrow(ProductServiceBundleNotFoundException::new);

                                boolean enrollmentStatusRequested =
                                        modifyOneEnrolledProductServiceBundle.getIsEnrolled();

                                boolean isEnrolled =
                                        productServiceBundle.getEnrolledProductServiceBundle() != null;

                                if (enrollmentStatusRequested && !isEnrolled) {
                                        productServiceBundle.enrollProductServiceBundle();
                                } else if (!enrollmentStatusRequested && isEnrolled) {
                                        productServiceBundle.removeEnrolledProductServiceBundle();
                                }
                        }
                );

                /*

                        CASES:

                        - frontend: true                backend: true                   do nothing
                        - frontend: false               backend: false                  do nothing
                        - frontend: true                backend: false                  create new entry
                        - frontend: false               backend: true                   delete entry
                        - frontend: true/false          backend: doesn't exist          doesn't exist mean false
                        - frontend: doesn't exist       backend: true/false             Not possible (search id comes from frontend)

                */
        }

}
