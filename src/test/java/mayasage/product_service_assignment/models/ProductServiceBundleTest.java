package mayasage.product_service_assignment.models;

import mayasage.product_service_assignment.repositories.ProductRepository;
import mayasage.product_service_assignment.repositories.ServiceRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

@SpringBootTest
@Transactional
public class ProductServiceBundleTest {

        @Autowired
        private EntityManagerFactory entityManagerFactory;

        @Autowired
        private ProductRepository productRepository;

        @Autowired
        private ServiceRepository serviceRepository;

        @Test
        public void addDeleteProduct() {
                EntityManager entityManager = entityManagerFactory.createEntityManager();

                entityManager.getTransaction().begin();
                Product productP1 = new Product("p1");
                entityManager.persist(productP1);
                entityManager.getTransaction().commit();

                entityManager.getTransaction().begin();
                entityManager.remove(productP1);
                entityManager.getTransaction().commit();

                Product product = productRepository.findProductByProductName("p1");
                Assertions.assertNull(product);
        }

        @Test
        public void addDeleteService() {
                EntityManager entityManager = entityManagerFactory.createEntityManager();

                entityManager.getTransaction().begin();
                Service service1 = new Service("s1");
                entityManager.persist(service1);
                entityManager.getTransaction().commit();

                entityManager.getTransaction().begin();
                entityManager.remove(service1);
                entityManager.getTransaction().commit();

                Service service = serviceRepository.findServiceByServiceName("s1");
                Assertions.assertNull(service);
        }

        @Test
        public void createProductServiceBundleFromProduct() {
                EntityManager entityManager = entityManagerFactory.createEntityManager();

                entityManager.getTransaction().begin();
                Service serviceS1 = new Service("s1");
                Service serviceS2 = new Service("s2");
                Product productP1 = new Product("p1");

                entityManager.persist(serviceS1);
                entityManager.persist(serviceS2);
                entityManager.persist(productP1);

                productP1.addService(serviceS1);
                productP1.addService(serviceS2);
                entityManager.getTransaction().commit();

                Product product = productRepository.findProductByProductName("p1");
                Assertions.assertNotNull(product);
                Set<ProductServiceBundle> productServiceBundles = product.getProductServiceBundles();
                Assertions.assertEquals(2, productServiceBundles.size());

                entityManager.getTransaction().begin();
                entityManager.remove(serviceS1);
                entityManager.remove(serviceS2);
                entityManager.remove(productP1);
                entityManager.getTransaction().commit();
        }

        @Test
        public void createProductServiceBundleFromService() {
                EntityManager entityManager = entityManagerFactory.createEntityManager();

                entityManager.getTransaction().begin();
                Service serviceS1 = new Service("s1");
                Service serviceS2 = new Service("s2");
                Product productP1 = new Product("p1");

                entityManager.persist(serviceS1);
                entityManager.persist(serviceS2);
                entityManager.persist(productP1);

                productP1.addService(serviceS1);
                productP1.addService(serviceS2);
                entityManager.getTransaction().commit();

                Product product = productRepository.findProductByProductName("p1");
                Assertions.assertNotNull(product);
                Set<ProductServiceBundle> productServiceBundles = product.getProductServiceBundles();
                Assertions.assertEquals(2, productServiceBundles.size());

                entityManager.getTransaction().begin();
                entityManager.remove(serviceS1);
                entityManager.remove(serviceS2);
                entityManager.remove(productP1);
                entityManager.getTransaction().commit();
        }

        @Test
        public void addTwoRemoveOneProductServiceBundleFromProduct() {
                EntityManager entityManager = entityManagerFactory.createEntityManager();

                entityManager.getTransaction().begin();
                Service serviceS1 = new Service("s1");
                Service serviceS2 = new Service("s2");
                Product productP1 = new Product("p1");

                entityManager.persist(serviceS1);
                entityManager.persist(serviceS2);
                entityManager.persist(productP1);

                productP1.addService(serviceS1);
                productP1.addService(serviceS2);
                entityManager.getTransaction().commit();

                entityManager.refresh(productP1);
                Assertions.assertEquals(2, productP1.getProductServiceBundles().size());

                entityManager.getTransaction().begin();
                productP1.removeService(serviceS1);
                entityManager.getTransaction().commit();

                entityManager.refresh(productP1);
                Assertions.assertEquals(1, productP1.getProductServiceBundles().size());

                entityManager.getTransaction().begin();
                entityManager.remove(serviceS1);
                entityManager.remove(serviceS2);
                entityManager.remove(productP1);
                entityManager.getTransaction().commit();
        }

        @Test
        public void cascadeFromProduct() {
                EntityManager entityManager = entityManagerFactory.createEntityManager();

                entityManager.getTransaction().begin();
                Product productP1 = new Product("p1");
                Service serviceS1 = new Service("s1");

                entityManager.persist(productP1);
                entityManager.persist(serviceS1);

                serviceS1.addProduct(productP1);
                entityManager.getTransaction().commit();

                entityManager.getTransaction().begin();
                entityManager.remove(productP1);
                entityManager.getTransaction().commit();

                Product deletedProduct = entityManager.find(Product.class, productP1.getProductId());
                Assertions.assertNull(deletedProduct);

                Service service = entityManager.find(Service.class, serviceS1.getServiceId());
                Assertions.assertNotNull(service);
                Assertions.assertEquals(0, service.getProductServiceBundles().size());

                entityManager.getTransaction().begin();
                entityManager.remove(serviceS1);
                entityManager.getTransaction().commit();
        }

        @Test
        public void cascadeFromService() {
                EntityManager entityManager = entityManagerFactory.createEntityManager();

                entityManager.getTransaction().begin();
                Product productP1 = new Product("p1");
                Service serviceS1 = new Service("s1");

                entityManager.persist(productP1);
                entityManager.persist(serviceS1);

                serviceS1.addProduct(productP1);
                entityManager.getTransaction().commit();

                entityManager.getTransaction().begin();
                entityManager.remove(serviceS1);
                entityManager.getTransaction().commit();

                Service deletedService = entityManager.find(Service.class, serviceS1.getServiceId());
                Assertions.assertNull(deletedService);

                Product product = entityManager.find(Product.class, productP1.getProductId());
                Assertions.assertNotNull(product);
                Assertions.assertEquals(0, product.getProductServiceBundles().size());

                entityManager.getTransaction().begin();
                entityManager.remove(productP1);
                entityManager.getTransaction().commit();
        }
}
