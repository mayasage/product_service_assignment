package mayasage.product_service_assignment.models;

import mayasage.product_service_assignment.exceptions.NoEnrolledProductServiceFoundException;
import mayasage.product_service_assignment.exceptions.ProductServiceBundleAlreadyEnrolledException;
import mayasage.product_service_assignment.models.composite_keys.ProductServiceBundleId;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.ZonedDateTime;

@Entity
@NoArgsConstructor
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "product_service_bundles")
public class ProductServiceBundle {

        @EqualsAndHashCode.Include
        @EmbeddedId
        private ProductServiceBundleId productServiceBundleId;

        @ManyToOne(fetch = FetchType.LAZY, optional = false)
        @MapsId(value = "productId")
        @JoinColumn(name = "product_service_bundle_product_id")
        private Product product;

        @ManyToOne(fetch = FetchType.LAZY, optional = false)
        @MapsId(value = "serviceId")
        @JoinColumn(name = "product_service_bundle_service_id")
        private Service service;

        @Setter(AccessLevel.PACKAGE)
        @OneToOne(
                mappedBy = "productServiceBundle",
                cascade = CascadeType.ALL,
                orphanRemoval = true
        )
        private EnrolledProductServiceBundle enrolledProductServiceBundle;

        @CreationTimestamp
        @Column(nullable = false, updatable = false)
        private ZonedDateTime productServiceBundleCreatedAt;

        ProductServiceBundle(Product product, Service service) {
                this.product = product;
                this.service = service;
                this.productServiceBundleId = new ProductServiceBundleId(product.getProductId(), service.getServiceId());
        }

        public void removeEnrolledProductServiceBundle() {
                if (enrolledProductServiceBundle == null) throw new NoEnrolledProductServiceFoundException();
                enrolledProductServiceBundle = null;
        }

        public void enrollProductServiceBundle() {
                if (enrolledProductServiceBundle != null) throw new ProductServiceBundleAlreadyEnrolledException();
                enrolledProductServiceBundle = new EnrolledProductServiceBundle(this);
        }
}
