package digit.application.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class BenefitsResponse {
    @JsonProperty("MdmsRes")
    private MdmsRes mdmsRes;

    public MdmsRes getMdmsRes() {
        return mdmsRes;
    }

    public void setMdmsRes(MdmsRes mdmsRes) {
        this.mdmsRes = mdmsRes;
    }

    public static class MdmsRes {
        @JsonProperty("Benefits")
        private Benefits benefits;

        public Benefits getBenefits() {
            return benefits;
        }

        public void setBenefits(Benefits benefits) {
            this.benefits = benefits;
        }
    }

    public static class Benefits {
        @JsonProperty("BenefitsTable")
        private List<Benefit> benefitsTable;

        public List<Benefit> getBenefitsTable() {
            return benefitsTable;
        }

        public void setBenefitsTable(List<Benefit> benefitsTable) {
            this.benefitsTable = benefitsTable;
        }
    }
}
