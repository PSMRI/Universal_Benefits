import React from 'react'
import { useTranslation } from 'react-i18next'
import { 
  LabelFieldPair, 
  CardLabel, 
  CardLabelError, 
  CitizenInfoLabel, 
  Header, 
  TextInput 
} from '@egovernments/digit-ui-react-components'

import { Loader } from "@egovernments/digit-ui-react-components";
import MultiUploadWrapper from './MultiUploadWrapper';
import {  Controller } from "react-hook-form";




const CustomUploadFileComposer = ({module, config,  control, register, formData, errors, localePrefix, customClass, customErrorMsg,mdmsModuleName='works'}) => {
  console.log("RENDERED ========== CustomUploadFileComposer")
  const { t } = useTranslation()
  
  //fetch mdms config based on module name
  // const tenant = Digit.ULBService.getStateId();
  // const { isLoading, data } = Digit.Hooks.useCustomMDMS(
  //     tenant,
  //     mdmsModuleName,
  //     [
  //         {
  //             "name": "DocumentConfig",
  //             "filter": `[?(@.module=='${module == null || module == undefined ? config?.module : module}')]`
  //         }
  //     ]
  // );


  // console.log('isLoading : ', isLoading)
  // console.log('data : ', data)

  let docConfig =  {}
  if (config?.DocumentConfig) {
    docConfig = config?.DocumentConfig
  } else if (data?.[mdmsModuleName]?.DocumentConfig?.[0]) {
    docConfig = data?.[mdmsModuleName]?.DocumentConfig?.[0]
  }
  
  let documentFileTypeMappings = {
    docx : "vnd.openxmlformats-officedocument.wordprocessingml.document",
    doc : "application/msword",
    png : "png",
    pdf : "pdf",
    jpeg : "jpeg",
    jpg : "jpeg",
    xls : "vnd.ms-excel", 
    xlsx : "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
    csv : "csv"
  }
  
  const getRegex = (allowedFormats) => {
    // console.log(allowedFormats);
    // if(allowedFormats?.length) {
    //   const obj = { "expression" : `/(.*?)(${allowedFormats?.join('|')})$/`}
    //   const stringified = JSON.stringify(obj);
    //   console.log(new RegExp(JSON.parse(stringified).expression.slice(1, -1)));
    //   return new RegExp(JSON.parse(stringified).expression.slice(1, -1));
    // } else if(docConfig?.allowedFileTypes?.length) {
    //   const obj = { "expression" : `/(.*?)(${docConfig?.allowedFileTypes?.join('|')})$/`}
    //   const stringified = JSON.stringify(obj);
    //   console.log(new RegExp(JSON.parse(stringified).expression.slice(1, -1)))
    //   return new RegExp(JSON.parse(stringified).expression.slice(1, -1));
    // } 
    // return /(.*?)(pdf|docx|jpeg|jpg|png|msword|openxmlformats-officedocument|wordprocessingml|document|spreadsheetml|sheet)$/
    if(allowedFormats?.length) {
      let exceptedFileTypes = [];
      allowedFormats?.forEach(allowedFormat=>{
        exceptedFileTypes.push(documentFileTypeMappings[allowedFormat]);
      });
      exceptedFileTypes = exceptedFileTypes.join("|");
      return new RegExp(`(.*?)(${exceptedFileTypes})$`)
    }
    return /(.*?)(pdf|docx|jpeg|jpg|png|msword|openxmlformats-officedocument|wordprocessingml|document|spreadsheetml|sheet)$/
  }

  // if(isLoading) return <Loader />
  // if(true) return <h1>Hello</h1>
  return (
    <React.Fragment>

      <Header styles={{fontSize: "24px", marginTop : "40px"}}>{t('WORKS_RELEVANT_DOCUMENTS')}</Header>
      <CitizenInfoLabel info={t("ES_COMMON_INFO")} text={t(docConfig?.bannerLabel)} className="doc-banner"></CitizenInfoLabel>
      {
        docConfig?.documents?.map((item, index) => {
          if(item?.active)
          return ( 
            <LabelFieldPair key={index} style={{ alignItems: item?.showTextInput? "flex-start":"center"}}>
              { item.code && (
                <CardLabel
                  className="bolder"
                  style={{ marginTop: item?.showTextInput? "10px":"" }}
                >
                  { t(`${config?.populators?.localePrefix}_${item?.code}`)} { item?.isMandatory ? " * " : null }
                </CardLabel>) 
              }
            
              <div className="field">
                {
                  item?.showTextInput ? 
                    <TextInput 
                      style={{ "marginBottom": "16px" }} 
                      name={`${config?.name}.${item?.name}_name`} 
                      placeholder={t('ES_COMMON_ENTER_NAME')}
                      inputRef={register({minLength: 2})}/> : 
                    null  
                }
                <div  style={{marginBottom: '24px'}}>
                  <Controller
                    render={({value = [], onChange}) => {
                      function getFileStoreData(filesData) {
                        const numberOfFiles = filesData.length;
                        let finalDocumentData = [];
                        if (numberOfFiles > 0) {
                          filesData.forEach((value) => {
                            finalDocumentData.push({
                              fileName: value?.[0],
                              fileStoreId: value?.[1]?.fileStoreId?.fileStoreId,
                              documentType: value?.[1]?.file?.type,
                            });
                          });
                        }
                        onChange(numberOfFiles>0?filesData:[]);
                      }
                      return (
                        <MultiUploadWrapper
                          t={t}
                          module="works"
                          getFormState={getFileStoreData}
                          setuploadedstate={value || []}
                          showHintBelow={item?.hintText ? true : false}
                          hintText={item?.hintText}
                          allowedFileTypesRegex={getRegex(item?.allowedFileTypes)}
                          allowedMaxSizeInMB={item?.maxSizeInMB || docConfig?.maxSizeInMB || 5}
                          maxFilesAllowed={item?.maxFilesAllowed || 1}
                          customErrorMsg={item?.customErrorMsg}
                          customClass={customClass}
                          tenantId={Digit.ULBService.getCurrentTenantId()}
                        /> 
                      ) 
                    }}
                    rules={{validate:(value) => {
                      return !(item?.isMandatory && value?.length === 0)
                    }}}
                    defaultValue={formData?.[item?.name]}
                    name={`${config?.name}.${item?.name}`}
                    control={control}
                  />
                   {  errors && errors[`${config?.name}`]?.[`${item?.name}`] && Object.keys(errors[`${config?.name}`]?.[`${item?.name}`]).length ? (
                      <CardLabelError style={{ fontSize: "12px"}}>
                        {t(config?.error)}
                      </CardLabelError> ) : null
                    }
                </div>
              </div>
            </LabelFieldPair>
          )
        })
          
      }   
    
    </React.Fragment>
  )
}

export default CustomUploadFileComposer