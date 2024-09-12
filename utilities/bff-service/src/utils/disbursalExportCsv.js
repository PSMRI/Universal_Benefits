const { search_application, search_individual, search_bank_account_details } = require("../api");
const { Parser } = require('json2csv');
const fs = require('fs');

let generatePaymentCSVFromDisbursals = async (disbursals, requestInfo) => {
    let applicationIds = []
    let individualIds = []
    
    let tenantId = disbursals[0]['tenantId']
    let disbursalApplicationMap = {};
    let applicationIndividualMap = {};
    let applicationIdMap = {}
    let individualIDMap = {}
    let bankAccountDetailIndividualIDMap = {}
    // Get applications for disbursal
    disbursals.forEach(disbursal => {
        disbursalApplicationMap[disbursal.id] = disbursal.referenceId
        applicationIds.push(disbursal.referenceId)
    });
    if (applicationIds.length) {
        applications = await getApplicationDetails(requestInfo, applicationIds, tenantId)
        if (applications && applications.length) {
            applications.forEach(application => {
                individualIds.push(application.individualId)
                applicationIndividualMap[application.id] = application.individualId;
                applicationIdMap[application.id] = application
            });
        }
    }
    console.log(applications)
    // Get individual details
    if (individualIds.length) {
        let individuals = await getIndividualDetails(requestInfo, individualIds, tenantId)
        
        individuals.forEach(individual => {
            individualIDMap[individual.id] = individual;
        })
    }

    // Get bank account details
    if (individualIds.length) {
        let bankAccounts = await getBankAccountDetails(requestInfo, individualIds, tenantId)
        bankAccounts.forEach(bankAccount => {
            bankAccountDetailIndividualIDMap[bankAccount.referenceId] = bankAccount;
        })
    }

    return getCSVData(disbursals, disbursalApplicationMap, applicationIndividualMap, applicationIdMap, individualIDMap, bankAccountDetailIndividualIDMap)
}

let getApplicationDetails = async (requestInfo, applicationIds, tenantId) => {
    let applications = [];
    try {
        let request = {
            'RequestInfo': requestInfo,
            'criteria': {
                ids: applicationIds,
                tenantId
            }
        }
        let applicationsResp = await search_application(request, applicationIds.length, 0)
        return applicationsResp?.Applications;
    } catch (error) {
        
    }
}

let getIndividualDetails = async (requestInfo, individualIds, tenantId) => {
    try {
        let request = {
            'RequestInfo': requestInfo,
            'Individual': {
                ids: individualIds
            }
        }
        let individualResp = await search_individual(request, tenantId, 100, 0)
        return individualResp?.Individual;
    } catch (error) {
        
    }
}

let getBankAccountDetails = async (requestInfo, individualIds, tenantId) => {
    try {
        let request = {
            'RequestInfo': requestInfo,
            'bankAccountDetails': {
                tenantId,
                referenceId: individualIds
            }
        }
        let bankAccountResp = await search_bank_account_details(request)
        return bankAccountResp?.bankAccounts;
    } catch (error) {
        
    }
}

let getCSVData = (disbursals, disbursalApplicationMap, applicationIndividualMap, applicationIdMap, individualIDMap, bankAccountDetailIndividualIDMap) => {
    let records = []
    disbursals.forEach(disbursal => {
        let appId = disbursalApplicationMap[disbursal['id']]
        let aplicationNumber = applicationIdMap[appId]['applicationNumber'];
        let individualId = applicationIdMap[appId]['individualId']
        let aadhaarNumber = null
        individualIDMap[individualId].identifiers.forEach(identifier => {    
            if (identifier.identifierType =='AADHAAR')
                aadhaarNumber = identifier.identifierId
        })
        let rec = {
            'disbursalNumber': disbursal['disbursalNumber'],
            'applicationNumber': aplicationNumber,
            'individualName': individualIDMap[individualId]['name']['givenName'],
            'aadhaarNumber': aadhaarNumber,
            'bankAccountNumber': bankAccountDetailIndividualIDMap[individualId]['bankAccountDetails'][0]['accountNumber'],
            'bankBranchIdentifier': bankAccountDetailIndividualIDMap[individualId]['bankAccountDetails'][0]['bankBranchIdentifier']['code'],
            'bankAccountHolderName': bankAccountDetailIndividualIDMap[individualId]['bankAccountDetails'][0]['accountHolderName'],
            'amount': disbursal['totalAmount']
        }
        records.push(JSON.parse(JSON.stringify(rec)))
    }) 
    console.log(records)
    // Map the headers (current: id, name, age, city) to new headers
    const newHeaders = [
        {value: 'disbursalNumber', label: 'Disbursal Number'},
        {value: 'applicationNumber', label: 'Application Number'},
        {value: 'individualName', label: 'Student Name'},
        {value: 'aadhaarNumber', label: 'Aadhaar Number'},
        {value: 'bankAccountNumber', label: 'Account Number'},
        {value: 'bankBranchIdentifier', label: 'IFSC'},
        {value: 'bankAccountHolderName', label: 'Account Holder Name'},
        {value: 'amount', label: 'Amount'},
    ];

    // Create a new CSV parser with the updated headers
    const json2csvParser = new Parser({ fields: newHeaders });

    // Convert JSON to CSV
    const csv = json2csvParser.parse(records);

    return csv
}

module.exports = {
    generatePaymentCSVFromDisbursals
};
  