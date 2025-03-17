package mayasage.product_service_assignment.repositories;

import mayasage.product_service_assignment.models.EnrolledProductServiceBundle;
import mayasage.product_service_assignment.models.composite_keys.ProductServiceBundleId;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnrolledProductServiceBundleRepository extends JpaRepository<EnrolledProductServiceBundle, ProductServiceBundleId> {

        @Query("""
            SELECT DISTINCT epsb.productServiceBundle.productServiceBundleId
            FROM EnrolledProductServiceBundle epsb
            WHERE epsb.productServiceBundle.productServiceBundleId.productId = :productId
        """)
        List<EnrolledProductServiceBundle> findByProductId(@Param("productId") @NotNull Long productId);
}
