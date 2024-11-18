package digit.web.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BenefitStatusCount {
    private String id;
    private String name;
    private LocalDate deadline;
    private Long approved;
    private Long rejected;
    private Long disbursalPending;
    private Long applicants;
}
