package mayasage.product_service_assignment.models.composite_keys;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@Immutable
public class ProductServiceBundleId implements Serializable {

        private Long productId;

        private Long serviceId;


        @Override
        public String toString() {
                return "productId: " + productId + ", serviceId: " + serviceId;
        }
}
