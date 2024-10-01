export const transformCreateData = (programData, formData) => {
  let documents = []
  if (formData?.undefined && Object.keys(formData?.undefined).length) {
    Object.keys(formData.undefined).forEach(function(key) {

      console.log(key, formData.undefined[key]);
      if (formData.undefined && formData.undefined[key]) {
        let docDetails = formData.undefined[key];
        formData.undefined[key].forEach((docMain) => {
          docMain.forEach((docObj) => {
            if(typeof(docObj) == 'object') {
              documents.push({
                'documentType': key,
                'fileStore': docObj.fileStoreId.fileStoreId,
              })
            }
          })
        })
      }
    });
  }
  let localityObj = null;
  if (formData?.locality?.code) {
    localityObj = {
      code: formData?.locality?.code || "SUN01",
    }
  }

  return {
    Individual: {
      tenantId: Digit.ULBService.getStateId(),
      name: {
        givenName: formData.applicantname,
      },
      dateOfBirth: formData?.dob ? formData?.dob.split("-").reverse().join("/"): null,
      gender: formData?.genders?.code,
      mobileNumber: formData.mobileNumber,
      address: [
        {
          tenantId: Digit.ULBService.getStateId(),
          pincode: formData.pincode,
          city: formData.city,
          street: formData.street,
          doorNo: formData.doorno,
          locality: localityObj,
          landmark: formData.landmark,
          type: "PERMANENT",
        },
      ],
      identifiers: [
        {
          "identifierType": "AADHAAR",
          "identifierId": formData.aadhaarNumber
        }
      ],
      // skills: [
      //   {
      //     type: "DRIVING",
      //     level: "UNSKILLED",
      //   },
      // ],
      photograph: null,
      isSystemUser: false,
      additionalFields: {
        // fields: [
        //   {
        //     key: "EMPLOYER",
        //     value: "ULB",
        //   },
        // ],
      },
      // userDetails: {
      //   username: formData.mobileNumber,
      //   tenantId: Digit.ULBService.getStateId(),
      //   roles: [
      //     {
      //       code: "SANITATION_WORKER",
      //       tenantId: Digit.ULBService.getStateId(),
      //     },
      //   ],
      //   type: "CITIZEN",
      // },
    },
    Application: {
      "tenantId": Digit.ULBService.getStateId(),
      "individualId": null,
      "programCode": programData?.data?.id,
      "documents": documents
    },
    BankAccount: {
      "tenantId": Digit.ULBService.getStateId(),
      "serviceCode": "IND",
      "referenceId": null,
      "bankAccountDetails": [
            {
                "tenantId": Digit.ULBService.getStateId(),
                "accountHolderName": formData.accountHolderName,
                "accountNumber": formData.accountNumber,
                "accountType": formData?.accountType?.code,
                "isPrimary": true,
                "bankBranchIdentifier": {
                    "type": "IFSC",
                    "code": formData?.ifscCode,
                    "additionalDetails": {}
                },
                "isActive": true,
                "documents": [],
                "additionalFields": {}
            }
        ],
        "additionalFields": {}
    }
  };
};
