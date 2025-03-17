package mayasage.product_service_assignment.repositories;

import mayasage.product_service_assignment.models.Service;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceRepository extends JpaRepository<Service, Long> {

        Service findServiceByServiceName(@NotBlank String serviceName);

        void deleteByServiceName(@NotNull String serviceName);

        void deleteServicesByServiceName(@NotBlank String serviceName);
}
