package mayasage.product_service_assignment.models;

import mayasage.product_service_assignment.models.composite_keys.ProductServiceBundleId;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Immutable;

import java.time.ZonedDateTime;

@Entity
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "enrolled_service_bundles")
@Immutable
@Getter
public class EnrolledProductServiceBundle {

        @EqualsAndHashCode.Include
        @EmbeddedId
        private ProductServiceBundleId productServiceBundleId;

        @OneToOne(fetch = FetchType.LAZY, optional = false)
        @MapsId
        @JoinColumns({
                @JoinColumn(
                        name = "enrolled_service_bundle_product_id",
                        referencedColumnName = "product_service_bundle_product_id"
                ),
                @JoinColumn(
                        name = "enrolled_service_bundle_service_id",
                        referencedColumnName = "product_service_bundle_service_id"
                )
        })
        private ProductServiceBundle productServiceBundle;

        @CreationTimestamp
        @Column(updatable = false, nullable = false)
        private ZonedDateTime productServiceBundleEnrolledAt;

        EnrolledProductServiceBundle(@NotNull ProductServiceBundle productServiceBundle) {
                this.productServiceBundle = productServiceBundle;
                this.productServiceBundleId = productServiceBundle.getProductServiceBundleId();
        }
}
