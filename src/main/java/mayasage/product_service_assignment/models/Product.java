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
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.NaturalId;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "products")
//@Immutable
public class Product {

        @Getter(value = AccessLevel.NONE)
        @OneToMany(
                mappedBy = "product",
                cascade = CascadeType.ALL,
                orphanRemoval = true,
                fetch = FetchType.LAZY
        )
        private final Set<ProductServiceBundle> productServiceBundles = new HashSet<>();

        @Id
        @SequenceGenerator(
                name = "product_id_sequence_generator",
                sequenceName = "product_id_sequence"
        )
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_id_sequence_generator")
        @Column(updatable = false, nullable = false)
        private Long productId;

        @EqualsAndHashCode.Include
        @NotBlank
        @NaturalId
        @Column(nullable = false, updatable = false, unique = true)
        private String productName;

        @CreationTimestamp
        @Column(nullable = false, updatable = false)
        private ZonedDateTime productCreatedAt;

        public Product(String productName) {
                this.productName = productName;
        }

        public Set<ProductServiceBundle> getProductServiceBundles() {
                return Collections.unmodifiableSet(productServiceBundles);
        }

        Set<ProductServiceBundle> _getProductServiceBundles() {
                return productServiceBundles;
        }

        public void addService(@NotNull Service service) {
                if (this.productId == null) throw new IllegalStateException("Product has not been created");
                ProductServiceBundle productServiceBundle = new ProductServiceBundle(this, service);
                productServiceBundles.add(productServiceBundle);
                service._getProductServiceBundles().add(productServiceBundle);
        }

        public void removeService(@NotNull Service service) {
                productServiceBundles.removeIf(productServiceBundle -> productServiceBundle.getService().equals(service));
        }

        @PreRemove
        public void preRemove() {
                for (ProductServiceBundle productServiceBundle : productServiceBundles) {
                        productServiceBundle.getService()._getProductServiceBundles().remove(productServiceBundle);
                }
                productServiceBundles.clear();
        }
}
