package mayasage.product_service_assignment.repositories.projections;

import mayasage.product_service_assignment.models.composite_keys.ProductServiceBundleId;

public class ProductServiceProjection {

        public interface ListProductServiceBundle {

                Long getProductId();

                String getProductName();

                Long getServiceId();

                String getServiceName();
        }

        public interface ListEnrolledProductServiceBundle {

                Long getProductId();

                String getProductName();

                Long getServiceId();

                String getServiceName();

                Boolean getIsEnrolled();
        }

        public interface GenericEnrolledProductServiceBundle {

                ProductServiceBundleId getProductServiceBundleId();

                Boolean getIsEnrolled();
        }
}
