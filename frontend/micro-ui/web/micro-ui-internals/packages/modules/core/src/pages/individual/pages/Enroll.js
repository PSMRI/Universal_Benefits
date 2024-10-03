import React, { useEffect } from "react";
import { useTranslation } from "react-i18next";
import { useParams, useHistory } from "react-router-dom";
import { TextBlock } from "@egovernments/digit-ui-components";
import { FormComposerV2, Loader } from '@egovernments/digit-ui-react-components'; 
import { newConfig } from "../configs/IndividualCreateConfig";
import { transformCreateData } from "../utils/createUtils";
import ValidateOTP from "./ValidateOTP";
import { SCHEME } from "../configs/schemeConfigs";

const IndividualCreate = () => {
  const { id } = useParams();
  const tenantId = Digit.ULBService.getStateId();
  const { t } = useTranslation();
  const history = useHistory();
  const reqCreate = {
    url: `/uba-bff-service/benefit/_apply`,
    params: {},
    body: {},
    config: {
      enable: false,
    },
  };
  const [showPopup, setShowPopUp] = React.useState(false);
  const [formData, setFormData] = React.useState({});
  const [showLoader, setShowLoader] = React.useState(true);

  const tenant = Digit.ULBService.getStateId();
  const { isLoading: isDocConfigLoading, data:uploadConfig } = Digit.Hooks.useCustomMDMS(
      tenant,
      SCHEME.UBP_MDMS_MODULE,
      [
          {
              "name": "DocumentConfig",
              "filter": `[?(@.module=='Application')]`
          }
      ]
  );


  const reqCriteria = {
    url: "/mdms-v2/v2/_search",
    params: {},
    body: {
      MdmsCriteria: {
        tenantId: Digit.ULBService.getStateId(),
        schemaCode: SCHEME.SCHEMES_SCHEMA_CODE,
        uniqueIdentifiers: [id],
      },
    },
    config: {
      select: (data) => {
        return data?.mdms?.map((obj) => ({ ...obj?.data?.en, id: obj?.data?.id }));
      },
    },
  };
  const { isLoading:isProgramLoading, data:programData } = Digit.Hooks.useCustomAPIHook(reqCriteria);

  useEffect(() => {
    
    if (isProgramLoading == false && isDocConfigLoading == false) {
      const docConfig = uploadConfig?.[SCHEME.UBP_MDMS_MODULE]?.DocumentConfig?.[0]
      // process doc config and generate dynamic file upload configs
      console.log(docConfig)
      console.log(programData)
      console.log(newConfig)
      
      if (docConfig != null) {
        newConfig.forEach((config) => {
          if(config.head == 'DOCUMENT_UPLOAD')  {
            config.body[0].DocumentConfig = generateFileUploadConfiguration(programData, docConfig)
            console.log(config)
          }
        })
      }
      setShowLoader(false)
    }
  }, [isProgramLoading, isDocConfigLoading])
  // user-otp/v1/_send?tenantId=pg

  const generateFileUploadConfiguration = (programDetails, docConfig) => {
    let fields = programDetails?.[0].applicationForm.formDetails.fields;
    if (fields != null && fields.length) {
      let sampleDocConfig = docConfig.documents[0]
      let listofFiles = []
      fields.forEach((field) =>{
        console.log(field)
        
        sampleDocConfig['code'] = field.fieldName
          .trim()
          .split(' ') // Split by spaces
          .join('_')
          .toUpperCase(); // Join words with underscore
        sampleDocConfig['isMandatory'] = field.required
        sampleDocConfig['name'] = field.fieldName
        listofFiles.push(JSON.parse(JSON.stringify(sampleDocConfig)))
      })
      docConfig.documents = listofFiles
    }
    return docConfig;
  }

  const mutation = Digit.Hooks.useCustomAPIMutationHook(reqCreate);
  const onError = (resp) => {
    // debugger;
    history.push(`/${window.contextPath}/individual/enroll-response?isSuccess=${false}`, { message: "SUBMISSION_CREATION_FAILED" });
  };

  const onSuccess = (resp) => {
    // debugger
    history.push(`/${window.contextPath}/individual/enroll-response?appNo=${resp.Application.applicationNumber}&isSuccess=${true}`, {
      message: "SUBMISSION_CREATION_SUCCESS",
      showID: true,
      label: "SUBMISSION_ID",
    });
  };
  const submitApplication = async () => {
    await mutation.mutate(
      {
        url: `/uba-bff-service/benefit/_apply`,
        body: transformCreateData(programData, formData),
        config: {
          enable: true,
        },
      },
      {
        onSuccess,
        onError,
      }
    );
  };
  const onSubmit = async (data) => {
    setFormData(data);
    console.log('transformCreateData : ', transformCreateData(programData, data))
    setShowPopUp(true);
  };
  
  if (showLoader) return <Loader />

  return (
    <div className="enroll">
      <TextBlock
        caption=""
        captionClassName=""
        header={`${(programData && programData.length)?programData[0].basicDetails.schemeName:''}`}
        headerClasName=""
        subHeader={`Fill all the details to Apply for the scheme.`}
        subHeaderClasName=""
        body=""
        bodyClasName=""
      ></TextBlock>
      <FormComposerV2
        label={t("Apply")}
        config={newConfig.map((config) => {
          return {
            ...config,
          };
        })}
        defaultValues={{}}
        onFormValueChange={(setValue, formData, formState, reset, setError, clearErrors, trigger, getValues) => {
          console.log(formData, "formData");
        }}
        onSubmit={(data) => onSubmit(data)}
        fieldStyle={{ marginRight: 0 }}
      />
      {showPopup && (
        <ValidateOTP
          formData={formData}
          onSuccess={(succeded = true) => {
            // debugger;
            setShowPopUp(false);
            succeded && submitApplication();
          }}
        ></ValidateOTP>
      )}
    </div>
  );
};

export default IndividualCreate;
