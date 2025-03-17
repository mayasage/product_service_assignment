package mayasage.product_service_assignment.dtos;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

public class EnrolledProductServiceDto {

        @Getter
        @Setter
        @AllArgsConstructor
        @EqualsAndHashCode(onlyExplicitlyIncluded = true)
        public static class ModifyOneEnrolledProductServiceBundle {
                @EqualsAndHashCode.Include
                private Long productId;

                @EqualsAndHashCode.Include
                private Long serviceId;

                private Boolean isEnrolled;
        }
}
