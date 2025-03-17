package mayasage.product_service_assignment.repositories;

import mayasage.product_service_assignment.models.Product;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

        Product findProductByProductName(@NotBlank String productName);

        void deleteByProductName(@NotBlank String productName);

        void removeProductByProductName(@NotBlank String productName);
}
