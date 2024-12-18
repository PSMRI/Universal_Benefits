package digit.application.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BenefitsRequest {
    @JsonProperty("RequestInfo")
    private RequestInfo requestInfo;

    @JsonProperty("MdmsCriteria")
    private MdmsCriteria mdmsCriteria;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public void setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
    }

    public MdmsCriteria getMdmsCriteria() {
        return mdmsCriteria;
    }

    public void setMdmsCriteria(MdmsCriteria mdmsCriteria) {
        this.mdmsCriteria = mdmsCriteria;
    }

    public static class RequestInfo {
        @JsonProperty("apiId")
        private String apiId;
        @JsonProperty("ver")
        private String ver;
        @JsonProperty("action")
        private String action;
        @JsonProperty("authToken")
        private String authToken;
        @JsonProperty("userInfo")
        private UserInfo userInfo;

        // Getters and Setters

        public String getApiId() {
            return apiId;
        }

        public void setApiId(String apiId) {
            this.apiId = apiId;
        }

        public String getVer() {
            return ver;
        }

        public void setVer(String ver) {
            this.ver = ver;
        }

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }

        public String getAuthToken() {
            return authToken;
        }

        public void setAuthToken(String authToken) {
            this.authToken = authToken;
        }

        public UserInfo getUserInfo() {
            return userInfo;
        }

        public void setUserInfo(UserInfo userInfo) {
            this.userInfo = userInfo;
        }

        public static class UserInfo {
            @JsonProperty("id")
            private String id;
            @JsonProperty("uuid")
            private String uuid;

            // Getters and Setters
            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getUuid() {
                return uuid;
            }

            public void setUuid(String uuid) {
                this.uuid = uuid;
            }
        }
    }

    public static class MdmsCriteria {
        @JsonProperty("tenantId")
        private String tenantId;

        @JsonProperty("moduleDetails")
        private ModuleDetail[] moduleDetails;

        // Getters and Setters

        public String getTenantId() {
            return tenantId;
        }

        public void setTenantId(String tenantId) {
            this.tenantId = tenantId;
        }

        public ModuleDetail[] getModuleDetails() {
            return moduleDetails;
        }

        public void setModuleDetails(ModuleDetail[] moduleDetails) {
            this.moduleDetails = moduleDetails;
        }

        public static class ModuleDetail {
            @JsonProperty("moduleName")
            private String moduleName;

            @JsonProperty("masterDetails")
            private MasterDetail[] masterDetails;

            // Getters and Setters

            public String getModuleName() {
                return moduleName;
            }

            public void setModuleName(String moduleName) {
                this.moduleName = moduleName;
            }

            public MasterDetail[] getMasterDetails() {
                return masterDetails;
            }

            public void setMasterDetails(MasterDetail[] masterDetails) {
                this.masterDetails = masterDetails;
            }

            public static class MasterDetail {
                @JsonProperty("name")
                private String name;

                @JsonProperty("filter")
                private String filter;

                // Getters and Setters

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getFilter() {
                    return filter;
                }

                public void setFilter(String filter) {
                    this.filter = filter;
                }
            }
        }
    }
}
