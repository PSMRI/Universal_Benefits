package digit.web.models.BenefitNewResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProvidingEntity {
    private String type;
    private String name;
    private Address address;
    private String department;
    private ContactInfo contactInfo;
}
