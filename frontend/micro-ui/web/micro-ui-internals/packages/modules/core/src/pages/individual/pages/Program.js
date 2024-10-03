import React, { useRef } from "react";
import { useTranslation } from "react-i18next";
import { Loader } from "@egovernments/digit-ui-react-components";
import { Card, TextBlock, Button, ViewCardFieldPair, Tag } from "@egovernments/digit-ui-components";
import { SCHEME } from "../configs/schemeConfigs";
import { useParams, useHistory } from "react-router-dom";
import {QRCodeSVG} from 'qrcode.react';
import jsPDF from 'jspdf';
import html2canvas from 'html2canvas';  

const Program = ({}) => {
  // const Program=[]
  const history = useHistory();

  const qrRef = useRef();

  const { id } = useParams();

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
  const { isLoading, data } = Digit.Hooks.useCustomAPIHook(reqCriteria);

  const downloadPDF = (prog) => {
    const qrElement = document.getElementById('program_details_qr_container');

    // Use html2canvas to capture the SVG as an image
    html2canvas(qrElement).then(canvas => {
      const qrCodeDataURL = canvas.toDataURL('image/png');

      const pdf = new jsPDF();
      pdf.setFontSize(20);
      pdf.text(prog?.basicDetails?.schemeName, 20, 20);
      pdf.addImage(qrCodeDataURL, 'PNG', 15, 40, 200, 215); // Adjust position and size as needed
      pdf.save(`${prog?.basicDetails?.schemeName || ""} QRCODE.pdf`);
    });
  };

  if (isLoading) {
    return <Loader />;
  }

  return (
    <div className="programs-all">
      <Card type={"primary"} className="program-card-primary">
        <TextBlock
          caption=""
          captionClassName=""
          header=""
          headerClasName=""
          subHeader={data?.[0]?.id}
          subHeaderClasName=""
          body=""
          bodyClasName=""
        ></TextBlock>

        {data?.map((prog) => {
          return (
            <div className="program-list" key={prog?.id}>
              <Card type={"secondary"}>
                
                <TextBlock
                  caption={prog?.schemeContent?.briefDescription}
                  captionClassName=""
                  header={prog?.basicDetails?.schemeName}
                  headerClasName=""
                  subHeader={prog?.schemeContent?.detailedDescription?.[0]?.children?.[0]?.children?.[0]?.text}
                  subHeaderClasName=""
                  body={prog?.schemeContent?.benefits?.[0]?.children?.[0]?.text}
                  bodyClasName=""
                ></TextBlock>
                {prog?.applicationProcess?.map((applicationProcess) => (
                  <ViewCardFieldPair
                    className=""
                    label={`Mode : ${applicationProcess?.mode}`}
                    style={{}}
                    value={`${applicationProcess?.process?.[0]?.children?.[0]?.children?.[0]?.text}`}
                  />
                ))}
                <ViewCardFieldPair
                  className=""
                  label={`${prog?.basicDetails?.schemeName}`}
                  style={{}}
                  value={`${prog?.basicDetails?.schemeShortTitle}`}
                />
                <ViewCardFieldPair className="" label={`${prog?.eligibilityCriteria?.eligibilityDescription_md}`} style={{}} value={""} />
                {prog?.eligibilityCriteria?.eligibilityDescription?.map((eligibilityDescription) => {
                  return eligibilityDescription?.children?.map((eligibilityDescriptionChildren) => {
                    return eligibilityDescriptionChildren?.children?.map((obj) => {
                      return <li>{obj?.text}</li>;
                    });
                  });
                })}
                <ViewCardFieldPair
                  className=""
                  label={`${prog?.basicDetails?.offeringEntity?.name}`}
                  style={{}}
                  value={`${prog?.basicDetails?.offeringEntity?.department?.label}`}
                />
                <ViewCardFieldPair className="" label={`Related to`} style={{}} value={``} />
                <div
                  style={{
                    display: "flex",
                    columnGap: "inherit",
                  }}
                >
                  {prog?.basicDetails?.tags?.map((tagname) => {
                    return <Tag icon="" label={tagname} labelStyle={{}} showIcon={false} style={{}} type="success" />;
                  })}
                </div>

                <div className="program-apply-wrapper">
                  <Button
                    className="custom-class"
                    icon="ArrowForward"
                    iconFill=""
                    isSuffix
                    label="Apply"
                    onClick={() => {
                      history.push(`/${window?.contextPath}/individual/enroll/${id}`);
                    }}
                    options={[]}
                    optionsKey=""
                    size=""
                    style={{}}
                    title=""
                  />
                </div>
                <div style={{ height: "auto", width: "100%" }}>
                  <Button
                      className="custom-class"
                      icon="ArrowForward"
                      iconFill=""
                      isSuffix
                      label="Download QR"
                      onClick={() => {
                        downloadPDF(prog);
                      }}
                      options={[]}
                      optionsKey=""
                      size=""
                      style={{}}
                      title=""
                  />
                  <div id="program_details_qr_container" style={{ height: '200px', width: '200px' }}>
                    <QRCodeSVG 
                      value={window.location.href}
                      size={200} 
                    />
                  </div>
                </div>
              </Card>
            </div>
          );
        })}
      </Card>
    </div>
  );
};

export default Program;
