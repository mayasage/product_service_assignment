package mayasage.product_service_assignment.repositories;

import mayasage.product_service_assignment.models.ProductServiceBundle;
import mayasage.product_service_assignment.models.composite_keys.ProductServiceBundleId;
import mayasage.product_service_assignment.repositories.projections.ProductServiceProjection;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductServiceBundleRepository extends JpaRepository<ProductServiceBundle, ProductServiceBundleId> {

        @Query("""
            SELECT          p.productId     AS productId,
                            p.productName   AS productName,
                            s.serviceId     AS serviceId,
                            s.serviceName   AS serviceName
            FROM            Product p
            INNER JOIN      ProductServiceBundle psb
            ON              psb.productServiceBundleId.productId = p.productId
            INNER JOIN      Service s
            ON              s.serviceId = psb.productServiceBundleId.serviceId
            ORDER BY        psb.productServiceBundleId.productId, psb.productServiceBundleId.serviceId
        """)
        List<ProductServiceProjection.ListProductServiceBundle> findAllProductServices();

        @Query("""
            SELECT          p.productId     AS productId,
                            p.productName   AS productName,
                            s.serviceId     AS serviceId,
                            s.serviceName   AS serviceName
            FROM            Product p
            INNER JOIN      ProductServiceBundle psb
            ON              psb.productServiceBundleId.productId = p.productId
            INNER JOIN      Service s
            ON              s.serviceId = psb.productServiceBundleId.serviceId
            WHERE           p.productId = :productId
            ORDER BY        psb.productServiceBundleId.productId, psb.productServiceBundleId.serviceId
        """)
        List<ProductServiceProjection.ListProductServiceBundle> findProductServiceBundleByProductId(@Param("productId") @NotNull Long productId);

        @Query("""
            SELECT          p.productId                                             AS productId,
                            p.productName                                           AS productName,
                            s.serviceId                                             AS serviceId,
                            s.serviceName                                           AS serviceName,
                            CASE
                            WHEN esb.productServiceBundleId.productId IS NULL
                            THEN false
                            ELSE true
                            END                                                     AS isEnrolled
            FROM            Product p
            INNER JOIN      ProductServiceBundle psb
            ON              psb.productServiceBundleId.productId = p.productId
            INNER JOIN      Service s
            ON              s.serviceId = psb.productServiceBundleId.serviceId
            LEFT JOIN       EnrolledProductServiceBundle esb
            ON              esb.productServiceBundleId = psb.productServiceBundleId
            ORDER BY        psb.productServiceBundleId.productId, psb.productServiceBundleId.serviceId
        """)
        List<ProductServiceProjection.ListEnrolledProductServiceBundle> findAllEnrolledProductServiceBundles();

        @Query("""
            SELECT          p.productId                                             AS productId,
                            p.productName                                           AS productName,
                            s.serviceId                                             AS serviceId,
                            s.serviceName                                           AS serviceName,
                            CASE
                            WHEN esb.productServiceBundleId.productId IS NULL
                            THEN false
                            ELSE true
                            END                                                     AS isEnrolled
            FROM            Product p
            INNER JOIN      ProductServiceBundle psb
            ON              psb.productServiceBundleId.productId = p.productId
            INNER JOIN      Service s
            ON              s.serviceId = psb.productServiceBundleId.serviceId
            LEFT JOIN       EnrolledProductServiceBundle esb
            ON              esb.productServiceBundleId = psb.productServiceBundleId
            WHERE           p.productId = :productId
            ORDER BY        psb.productServiceBundleId.productId, psb.productServiceBundleId.serviceId
        """)
        List<ProductServiceProjection.ListEnrolledProductServiceBundle> findEnrolledProductServiceBundleByProductId(@Param("productId") @NotNull Long productId);
}
