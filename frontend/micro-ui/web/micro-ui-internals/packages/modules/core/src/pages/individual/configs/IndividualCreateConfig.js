export const newConfig = [
  {
    head: "Applicant Details",
    body: [
      {
        inline: true,
        label: "Applicant Name",
        isMandatory: true,
        key: "applicantname",
        type: "text",
        disable: false,
        populators: { name: "applicantname", error: "Required", validation: { pattern: /^[A-Za-z]+$/i } },
      },
      {
        inline: true,
        label: "Date of Birth",
        isMandatory: false,
        key: "dob",
        type: "date",
        disable: false,
        populators: { name: "dob", error: "Required" },
      },
      {
        isMandatory: false,
        key: "genders",
        type: "dropdown",
        label: "Enter Gender",
        disable: false,
        populators: {
          name: "genders",
          optionsKey: "name",
          error: "required ",
          mdmsConfig: {
            masterName: "GenderType",
            moduleName: "common-masters",
            localePrefix: "COMMON_GENDER",
          },
        },
      },

      {
        label: "Phone number",
        isMandatory: true,
        key: "mobileNumber",
        type: "number",
        disable: false,
        populators: { name: "mobileNumber", error: "Required", validation: { min: 0, max: 9999999999 } },
      },
      {
        label: "Aadhaar number",
        isMandatory: true,
        key: "aadhaarNumber",
        type: "number",
        disable: false,
        populators: { name: "aadhaarNumber", error: "Required", validation: { min: 100000000000, max: 999999999999 } },
      },
    ],
  },
  {
    head: "Residential Details",
    body: [
      {
        inline: true,
        label: "City",
        isMandatory: true,
        key: "city",
        type: "text",
        disable: false,
        populators: { name: "city", error: " Required ", validation: { pattern: /^[A-Za-z]+$/i } },
      },

      {
        inline: true,
        label: "Landmark",
        isMandatory: false,
        key: "landmark",
        type: "text",
        disable: false,
        populators: { name: "landmark", error: " Required", validation: { pattern: /^[A-Za-z]+$/i } },
      },
    ],
  },
  {
    head: "DOCUMENT_UPLOAD",
    body: [
      {
        isMandatory: false,
        key: "documents",
        type: "component", // for custom component
        component: "CustomUploadFileComposer", // name of the component as per component registry
        withoutLabel: true,
        disable: false,
        customProps: {},
        module: "Application",
        populators: {
          name: "documents",
          required: true,
          localePrefix: "APPLICATION_UPLOAD_",
        },
      }
    ],
  },
  {
    head: "Bank Account Details",
    body: [
      {
        inline: true,
        label: "Account Holder Name",
        isMandatory: true,
        key: "accountHolderName",
        type: "text",
        disable: false,
        populators: { 
          name: "accountHolderName", 
          error: " Required ", 
          validation:  { pattern: "^[a-zA-Z0-9 .\\-_@\\']*$", minlength : 2, maxlength: 64 } 
        },
      },
      {
        inline: true,
        label: "Account Number",
        isMandatory: true,
        key: "accountNumber",
        type: "text",
        disable: false,
        populators: { 
          name: "accountNumber", 
          error: " Required ", 
          validation: {pattern: "^[0-9]{9,18}$", minlength : 9, maxlength: 18} 
        },
      },
      {
        isMandatory: true,
        key: "accountType",
        type: "dropdown",
        label: "Enter Account Type",
        disable: false,
        populators: {
          name: "accountType",
          optionsKey: "name",
          error: "required ",
          mdmsConfig: {
            masterName: "BankAccType",
            moduleName: "ubp",
            localePrefix: "MASTERS",
          },
        },
      },
      {
        inline: true,
        label: "IFSC Code",
        isMandatory: true,
        key: "ifscCode",
        type: "text",
        disable: false,
        populators: { name: "ifscCode", error: " Required", validation: { pattern: /^[a-zA-Z0-9_]+$/i } },
      },
    ],
  },
];
