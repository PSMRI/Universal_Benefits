package digit.web.models;

import java.time.LocalDateTime;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.OffsetDateTime;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.Builder;

/**
 * Customer
 */
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-10-30T00:31:03.321266700+05:30[Asia/Calcutta]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Customer   {

    @JsonProperty("id")

    private String id = null;

        @JsonProperty("name")
          @NotNull

                private String name = null;

        @JsonProperty("email")
          @NotNull

                private String email = null;

        @JsonProperty("phone")
          @NotNull

        @Pattern(regexp="^[0-9]{10}$")         private String phone = null;

            /**
            * Gender of the customer
            */
            public enum GenderEnum {
                        MALE("Male"),

                        FEMALE("Female"),

                        OTHER("Other");

            private String value;

            GenderEnum(String value) {
            this.value = value;
            }

            @Override
            @JsonValue
            public String toString() {
            return String.valueOf(value);
            }

            @JsonCreator
            public static GenderEnum fromValue(String text) {
            for (GenderEnum b : GenderEnum.values()) {
            if (String.valueOf(b.value).equals(text)) {
            return b;
            }
            }
            return null;
            }
            }

            @JsonProperty("gender")
                private String gender = null;

        @JsonProperty("order_id")

                private String orderId = null;

        @JsonProperty("transaction_id")

                private String transactionId = null;

        @JsonProperty("created_at")

          @Valid
                private String createdAt = null;

        @JsonProperty("updated_at")

          @Valid
                private String updatedAt = null;

        @JsonProperty("submission_id")

                private String submissionId = null;

        @JsonProperty("content_id")

                private String contentId = null;


}


// tesating commit diff