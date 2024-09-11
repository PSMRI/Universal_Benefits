// config.js
// const env = process.env.NODE_ENV; // 'dev' or 'test'

HOST = process.env.EGOV_HOST || "localhost";


if (!HOST) {
  console.log("You need to set the HOST variable");
  process.exit(1);
}

module.exports = {
  auth_token: process.env.AUTH_TOKEN,
  KAFKA_BROKER_HOST: process.env.KAFKA_BROKER_HOST || "localhost:9092",
  KAFKA_RECEIVE_CREATE_JOB_TOPIC: process.env.KAFKA_RECEIVE_CREATE_JOB_TOPIC || "PDF_GEN_RECEIVE",
  KAFKA_BULK_PDF_TOPIC: process.env.KAFKA_BULK_PDF_TOPIC || "BULK_PDF_GEN",
  KAFKA_PAYMENT_EXCEL_GEN_TOPIC: process.env.KAFKA_PAYMENT_EXCEL_GEN_TOPIC || "PAYMENT_EXCEL_GEN",
  KAFKA_EXPENSE_PAYMENT_CREATE_TOPIC: process.env.KAFKA_EXPENSE_PAYMENT_CREATE_TOPIC || "expense-payment-create",
  PDF_BATCH_SIZE: process.env.PDF_BATCH_SIZE || 40,
  DB_USER: process.env.DB_USER || "postgres",
  DB_PASSWORD: process.env.DB_PASSWORD || "postgres",
  DB_HOST: process.env.DB_HOST || "localhost",
  DB_NAME: process.env.DB_NAME || "ubp",
  DB_PORT: process.env.DB_PORT || 5432,
  app: {
    port: parseInt(process.env.APP_PORT) || 8080,
    host: HOST,
    contextPath: process.env.CONTEXT_PATH || "/ubp-bff",
  },
  host: {
    mdms: process.env.EGOV_MDMS_HOST || 'http://localhost:8083',
    pdf: process.env.EGOV_PDF_HOST || 'http://localhost:8081',
    user: process.env.EGOV_USER_HOST || HOST,
    projectDetails: process.env.EGOV_PROJECT_HOST || 'http://localhost:8082/',
    localization: process.env.EGOV_LOCALIZATION_HOST || 'http://localhost:8088',
    filestore: process.env.EGOV_FILESTORE_SERVICE_HOST || 'http://localhost:8092',
    individual: process.env.EGOV_INDIVIDUAL_SERVICE_HOST || 'http://localhost:9001',
    bankaccount: process.env.BANKACCOUNT_SERVICE_HOST || 'http://localhost:9002',
    application: process.env.APPLICATION_SERVICE_HOST || 'http://localhost:9005',
    disbursal: process.env.DISBURSAL_SERVICE_HOST || 'http://localhost:9007',
  },
  paths: {
    pdf_create: "/pdf-service/v1/_createnosave",
    user_search: "/user/_search",
    mdms_search: "/egov-mdms-service/v1/_search",
    projectDetails_search: "/project/v1/_search",
    mdms_get: "/egov-mdms-service/v1/_get",
    disbursal_search: "/disbursal/v1/_search",
    bankaccount_search: "/bankaccount-service/bankaccount/v1/_search",
    localization_search: "/localization/messages/v1/_search",
    application_search: "/application/v1/_search",
    individual_search: "/individual/v1/_search"
  }
};
