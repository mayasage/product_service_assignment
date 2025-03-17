package mayasage.product_service_assignment.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PreRemove;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.NaturalIdCache;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NaturalIdCache
@Table(name = "services")
//@Immutable
public class Service {

        @Id
        @SequenceGenerator(
                name = "service_id_sequence_generator",
                sequenceName = "service_id_sequence"
        )
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "service_id_sequence_generator")
        private Long serviceId;

        @EqualsAndHashCode.Include
        @Setter
        @NotBlank
        @NaturalId
        @Column(nullable = false, updatable = false, unique = true)
        private String serviceName;

        @Getter(value = AccessLevel.NONE)
        @OneToMany(
                mappedBy = "service",
                cascade = CascadeType.ALL,
                orphanRemoval = true,
                fetch = FetchType.LAZY
        )
        private final Set<ProductServiceBundle> productServiceBundles = new HashSet<>();

        @CreationTimestamp
        @Column(nullable = false, updatable = false)
        private ZonedDateTime serviceCreatedAt;

        public Service(String serviceName) {
                this.serviceName = serviceName;
        }

        public Set<ProductServiceBundle> getProductServiceBundles() {
                return Collections.unmodifiableSet(productServiceBundles);
        }

        Set<ProductServiceBundle> _getProductServiceBundles() {
                return productServiceBundles;
        }

        public void addProduct(@NotNull Product product) {
                if (this.serviceId == null) throw new IllegalStateException("Service has not been created");
                ProductServiceBundle productServiceBundle = new ProductServiceBundle(product, this);
                productServiceBundles.add(productServiceBundle);
                product._getProductServiceBundles().add(productServiceBundle);
        }

        @PreRemove
        public void preRemove() {
                for (ProductServiceBundle productServiceBundle : productServiceBundles) {
                        productServiceBundle.getProduct()._getProductServiceBundles().remove(productServiceBundle);
                }
                productServiceBundles.clear();
        }
}
