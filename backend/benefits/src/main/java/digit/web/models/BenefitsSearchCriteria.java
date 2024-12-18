package digit.web.models;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.Builder;

/**
 * BenefitsSearchCriteria
 */
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-10-23T15:21:03.651111+05:30[Asia/Calcutta]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BenefitsSearchCriteria   {
        @JsonProperty("status")

                private String status = null;

        @JsonProperty("benefitName")

                private List<String> benefitName = null;

        @JsonProperty("benefitProviderIds")

                private List<String> benefitProviderIds = null;

        @JsonProperty("applicationNumber")

        @Size(min=2,max=64)         private String applicationNumber = null;


        public BenefitsSearchCriteria addBenefitNameItem(String benefitNameItem) {
            if (this.benefitName == null) {
            this.benefitName = new ArrayList<>();
            }
        this.benefitName.add(benefitNameItem);
        return this;
        }

        public BenefitsSearchCriteria addBenefitProviderIdsItem(String benefitProviderIdsItem) {
            if (this.benefitProviderIds == null) {
            this.benefitProviderIds = new ArrayList<>();
            }
        this.benefitProviderIds.add(benefitProviderIdsItem);
        return this;
        }

}
