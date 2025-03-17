package mayasage.product_service_assignment.controllers;

import mayasage.product_service_assignment.dtos.EnrolledProductServiceDto;
import mayasage.product_service_assignment.repositories.projections.ProductServiceProjection;
import mayasage.product_service_assignment.services.EnrolledProductServiceService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/product-service")
@RequiredArgsConstructor
public class EnrolledProductServiceController {

        private final EnrolledProductServiceService enrolledProductServiceService;

        @GetMapping({"", "/"})
        public List<ProductServiceProjection.ListProductServiceBundle> listAllProductServiceBundles(
                @RequestParam(required = false) Long productId,
                @RequestParam(required = false, defaultValue = "") String specialKey
        ) {
                if (specialKey.equals("hello")) {
                        return enrolledProductServiceService.findAllProductServices();
                }
                if (productId != null) {
                        return enrolledProductServiceService.findProductServiceByProductId(productId);
                }
                return new ArrayList<>();
        }

        @GetMapping("/enrolled")
        public List<ProductServiceProjection.ListEnrolledProductServiceBundle> listAllEnrolledProductServiceBundles(
                @RequestParam(required = false) Long productId,
                @RequestParam(required = false, defaultValue = "") String specialKey
        ) {
                if (specialKey.equals("hello")) {
                        return enrolledProductServiceService.findAllEnrolledProductServiceBundles();
                }
                if (productId != null) {
                        return enrolledProductServiceService.findEnrolledProductServiceByProductId(productId);
                }
                return new ArrayList<>();
        }

        @PostMapping("/enrolled")
        public void modifyEnrolledProductServiceBundle(
                @RequestBody
                @NotNull
                List<EnrolledProductServiceDto.ModifyOneEnrolledProductServiceBundle> modifyModifyOneEnrolledProductServiceBundleRequest
        ) {
                enrolledProductServiceService.modifyEnrolledProductService(modifyModifyOneEnrolledProductServiceBundleRequest);
        }
}
