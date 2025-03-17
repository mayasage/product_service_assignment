package mayasage.product_service_assignment;

import mayasage.product_service_assignment.models.Product;
import mayasage.product_service_assignment.models.Service;
import mayasage.product_service_assignment.repositories.ProductRepository;
import mayasage.product_service_assignment.repositories.ServiceRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ProductServiceAssignmentApplication {

        public static void main(String[] args) {
                SpringApplication.run(ProductServiceAssignmentApplication.class, args);
        }

        @Bean
        public ApplicationRunner addProductServicesAtStartup(
                ProductRepository productRepository,
                ServiceRepository serviceRepository
        ) {
                return args -> {
                        Product productPencil = new Product("Pencil");
                        Service serviceWriting = new Service("Writing");
                        Service serviceDrawing = new Service("Drawing");

                        productRepository.save(productPencil);
                        serviceRepository.save(serviceWriting);
                        serviceRepository.save(serviceDrawing);

                        productPencil.addService(serviceWriting);
                        productPencil.addService(serviceDrawing);

                        productRepository.save(productPencil);

                        Product productCar = new Product("Car");
                        Service serviceDriving = new Service("Driving");
                        Service serviceRacing = new Service("Racing");

                        productRepository.save(productCar);
                        serviceRepository.save(serviceDriving);
                        serviceRepository.save(serviceRacing);

                        productCar.addService(serviceDriving);
                        productCar.addService(serviceRacing);

                        productRepository.save(productCar);

                        Product productNightVisionGoggles = new Product("Night Vision Goggles");
                        Service serviceNightWatching = new Service("Night Watching");
                        Service serviceNightRaiding = new Service("Night Raiding");

                        productRepository.save(productNightVisionGoggles);
                        serviceRepository.save(serviceNightWatching);
                        serviceRepository.save(serviceNightRaiding);

                        productNightVisionGoggles.addService(serviceNightWatching);
                        productNightVisionGoggles.addService(serviceNightRaiding);

                        productRepository.save(productNightVisionGoggles);

                        Product productLamp = new Product("Lamp");
                        Service serviceLighting = new Service("Lighting");
                        Service serviceDecoration = new Service("Decoration");

                        productRepository.save(productLamp);
                        serviceRepository.save(serviceLighting);
                        serviceRepository.save(serviceDecoration);

                        productLamp.addService(serviceLighting);
                        productLamp.addService(serviceDecoration);

                        productRepository.save(productLamp);
                };
        }
}
