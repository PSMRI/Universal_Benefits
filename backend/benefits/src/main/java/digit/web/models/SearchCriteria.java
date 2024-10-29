package digit.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchCriteria {
    @JsonProperty("name")
    private String name;

    @JsonProperty("valid_till")
    private Date validTill;

    @JsonProperty("created_start")
    private Date createdStart;

    @JsonProperty("created_end")
    private Date createdEnd;

    @JsonProperty("status")
    private String status;

    @JsonProperty("page_no")
    private int pageNo = 0;  // default to 0

    @JsonProperty("page_size")
    private int pageSize = 10;  // default to 10

    @JsonProperty("sort_by")
    private String sortBy = "benefit_name";  // default to "benefit_name"
}
