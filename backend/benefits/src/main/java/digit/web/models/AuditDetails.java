package digit.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuditDetails {
    @JsonProperty("createdBy")
    private String createdBy = null;
    @JsonProperty("lastModifiedBy")
    private String lastModifiedBy = null;
    @JsonProperty("createdTime")
    private Long createdTime = null;
    @JsonProperty("lastModifiedTime")
    private Long lastModifiedTime = null;

    public String toString() {
        String var10000 = this.getCreatedBy();
        return "AuditDetails(createdBy=" + var10000 + ", lastModifiedBy=" + this.getLastModifiedBy() + ", createdTime=" + this.getCreatedTime() + ", lastModifiedTime=" + this.getLastModifiedTime() + ")";
    }
}
