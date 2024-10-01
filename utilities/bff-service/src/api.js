var config = require("./config");
var axios = require("axios").default;
var url = require("url");
var producer = require("./producer").producer;
var logger = require("./logger").logger;
const { Pool } = require('pg');
const get = require('lodash/get');
var FormData = require("form-data");
const uuidv4 = require("uuid/v4");

const pool = new Pool({
  user: config.DB_USER,
  host: config.DB_HOST,
  database: config.DB_NAME,
  password: config.DB_PASSWORD,
  port: config.DB_PORT,
});

auth_token = config.auth_token;

async function search_projectDetails(tenantId, requestinfo, projectId) {
  var params = {
    tenantId: tenantId,
    limit: 1,
    offset: 0
  };

  var searchEndpoint = config.paths.projectDetails_search;
  var data = {
    "Projects": [{
      "tenantId": tenantId,
      "projectNumber": projectId
    }]
  }
  return await axios({
    method: "post",
    url: url.resolve(config.host.projectDetails, searchEndpoint),
    data: Object.assign(requestinfo, data),
    params,
  });
}

async function search_user(uuid, tenantId, requestinfo) {
  return await axios({
    method: "post",
    url: url.resolve(config.host.user, config.paths.user_search),
    data: {
      RequestInfo: requestinfo.RequestInfo,
      uuid: [uuid],
      tenantId: tenantId,
    },
  });
}

async function search_mdms(request) {
  return await axios({
    method: "post",
    url: url.resolve(config.host.mdms, config.paths.mdms_search),
    data: request
  });
}

async function search_localization(request, lang, module, tenantId) {
  return await axios({
    method: "post",
    url: url.resolve(config.host.localization, config.paths.localization_search),
    data: request,
    params: {
      "locale": lang,
      "module": module,
      "tenantId": tenantId.split(".")[0]
    }
  });
}

async function create_pdf(tenantId, key, data, requestinfo) {
  var oj = Object.assign(requestinfo, data);
  console.log(Object.assign(requestinfo, data))
  return await axios({
    responseType: "stream",
    method: "post",
    url: url.resolve(config.host.pdf, config.paths.pdf_create),
    data: Object.assign(requestinfo, data),
    params: {
      tenantId: tenantId,
      key: key,
    },
  });
}

async function create_pdf_and_upload(tenantId, key, data, requestinfo) {
  return await axios({
    //responseType: "stream",
    method: "post",
    url: url.resolve(config.host.pdf, config.paths.pdf_create_upload),
    data: Object.assign(requestinfo, data),
    params: {
      tenantId: tenantId,
      key: key,
    },
  });
}

function checkIfCitizen(requestinfo) {
  if (requestinfo.RequestInfo.userInfo.type == "CITIZEN") {
    return true;
  } else {
    return false;
  }
}

function search_disbursal(request, limit, offset) {
  return new Promise((resolve, reject) => {
    let newRequest = JSON.parse(JSON.stringify(request))
    newRequest["pagination"] = { limit, offset }
    let promise = new axios({
      method: "POST",
      url: url.resolve(config.host.disbursal, config.paths.disbursal_search),
      data: newRequest,
    });
    promise.then((data) => {
      resolve(data.data)
    }).catch((err) => reject(err))
  })
}

function search_application(request, limit, offset) {
  return new Promise((resolve, reject) => {
    let newRequest = JSON.parse(JSON.stringify(request))
    newRequest["pagination"] = { limit, offset }
    let promise = new axios({
      method: "POST",
      url: url.resolve(config.host.application, config.paths.application_search),
      data: newRequest,
    });
    promise.then((data) => {
      resolve(data.data)
    }).catch((err) => reject(err))
  })
}

function create_application(request) {
  return new Promise((resolve, reject) => {
    let newRequest = JSON.parse(JSON.stringify(request))
    let promise = new axios({
      method: "POST",
      url: url.resolve(config.host.application, config.paths.application_create),
      data: newRequest,
    });
    promise.then((data) => {
      resolve(data.data)
    }).catch((err) => reject(err))
  })
}

function search_individual(request, tenantId, limit, offset) {
  return new Promise((resolve, reject) => {
    let params = encodeQueryData({tenantId, offset, limit})
    let newRequest = JSON.parse(JSON.stringify(request))
    let searchUrl = url.resolve(config.host.individual, config.paths.individual_search + "?" + params)
    let promise = new axios({
      method: "POST",
      url: searchUrl,
      data: newRequest,
    });
    promise.then((data) => {
      resolve(data.data)
    }).catch((err) => reject(err))
  })
}

function create_individual(request) {
  return new Promise((resolve, reject) => {
    let newRequest = JSON.parse(JSON.stringify(request))
    let createUrl = url.resolve(config.host.individual, config.paths.individual_create)
    let promise = new axios({
      method: "POST",
      url: createUrl,
      data: newRequest,
    });
    promise.then((data) => {
      resolve(data.data)
    }).catch((err) => reject(err))
  })
}


function encodeQueryData(data) {
  const ret = [];
  for (let d in data)
    ret.push(encodeURIComponent(d) + '=' + encodeURIComponent(data[d]));
  return ret.join('&');
}


function search_bank_account_details(request) {
  return new Promise((resolve, reject) => {
    let newRequest = JSON.parse(JSON.stringify(request))
    let promise = new axios({
      method: "POST",
      url: url.resolve(config.host.bankaccount, config.paths.bankaccount_search),
      data: newRequest,
    });
    promise.then((data) => {
      resolve(data.data)
    }).catch((err) => reject(err))
  })
}

function create_bank_account_details(request) {
  return new Promise((resolve, reject) => {
    let newRequest = JSON.parse(JSON.stringify(request))
    let promise = new axios({
      method: "POST",
      url: url.resolve(config.host.bankaccount, config.paths.bankaccount_create),
      data: newRequest,
    });
    promise.then((data) => {
      resolve(data.data)
    }).catch((err) => reject(err))
  })
}


/**
 *
 * @param {*} filename -name of localy stored temporary file
 * @param {*} tenantId - tenantID
 */
async function upload_file_using_filestore(filename, tenantId, fileData) {
  try {
    var url = `${config.host.filestore}/filestore/v1/files?tenantId=${tenantId}&module=billgen&tag=works-billgen`;
    var form = new FormData();
    form.append("file", fileData, {
      filename: filename,
      contentType: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
    });
    let response = await axios.post(url, form, {
      maxContentLength: Infinity,
      maxBodyLength: Infinity,
      headers: {
        ...form.getHeaders()
      }
    });
    return get(response.data, "files[0].fileStoreId");
  } catch (error) {
    console.log(error);
    throw(error)
  }
};

async function create_eg_payments_excel(paymentId, paymentNumber, tenantId, userId) {
  try {
    var id = uuidv4();
    const insertQuery = 'INSERT INTO eg_payments_excel(id, paymentid, paymentnumber, tenantId, status, numberofbills, numberofbeneficialy, totalamount, filestoreid, createdby, lastmodifiedby, createdtime, lastmodifiedtime) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11, $12, $13)';
    const status = 'INPROGRESS';
    const curentTimeStamp = new Date().getTime();
    await pool.query(insertQuery, [id, paymentId, paymentNumber, tenantId, status, 0, 0, 0, null, userId, userId, curentTimeStamp, curentTimeStamp]);
  } catch (error) {
    throw(error)
  }
}

async function reset_eg_payments_excel(paymentId, userId) {
  try {
    const status = 'INPROGRESS';
    const updateQuery = 'UPDATE eg_payments_excel SET status =  $1, numberofbills = $2, numberofbeneficialy = $3, totalamount = $4, filestoreid = $5, lastmodifiedby = $6, lastmodifiedtime = $7 WHERE paymentid = $8';
    const curentTimeStamp = new Date().getTime();
    await pool.query(updateQuery,[status, 0, 0, 0, null, userId, curentTimeStamp, paymentId]);
    return;
  } catch (error) {
    throw(error)
  }
}

async function exec_query_eg_payments_excel(query, queryParams) {
  try {
    return pool.query(query, queryParams);
  } catch (error) {
    throw(error)
  }
}


module.exports = {
  pool,
  create_pdf,
  create_pdf_and_upload,
  search_mdms,
  search_user,
  search_projectDetails,
  search_mdms,
  search_localization,
  search_disbursal,
  search_application,
  create_application,
  search_bank_account_details,
  create_bank_account_details,
  search_individual,
  create_individual,
  upload_file_using_filestore,
  create_eg_payments_excel,
  reset_eg_payments_excel,
  exec_query_eg_payments_excel
};
