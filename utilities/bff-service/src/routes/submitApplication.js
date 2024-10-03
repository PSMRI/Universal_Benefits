var express = require("express");
var router = express.Router();
var config = require("../config");
const { asyncMiddleware } = require("../utils/asyncMiddleware");
const { search_individual, search_application, create_individual, search_bank_account_details, create_application, create_bank_account_details } = require("../api");
var logger = require("../logger").logger;

router.post('/',
    asyncMiddleware(async function (req, res, next) {
        try {    
            var requestInfo = req.body?.RequestInfo;
            var individualRequest = req.body?.Individual;
            var applicationRequest = req.body?.Application;
            var bankAccountRequest = req.body?.BankAccount;

            if (requestInfo == undefined) {
                return renderError(res, "RequestInfo can not be null");
            }
            if (individualRequest == undefined) {
                return renderError(res, "Individual can not be null");
            }
            if (applicationRequest == undefined) {
                return renderError(res, "Application can not be null");
            }
            var tenantId = individualRequest?.tenantId;
            if (!tenantId) {
                return renderError(
                    res,
                    "TenantId are mandatory to generate the group bill"
                );
            }
            let individualExists = false;
            let alreadyRegistered = false;
            let bankAccountExists = false;
            let application;
            let bankAccount;
            // get individual is already registered
            let individual = await getIndividualDetails(individualRequest);
            if (individual && individual.id) { 
                individualExists = true;
                // If individual is already registered then check already applied
                application = await getApplicationDetails(individual.id, applicationRequest.programCode)
                if (application && application.id) {
                    alreadyRegistered = true;
                }
            }
            // If individual is not regisered then register
            logger.info('Registering Individual');
            if (!individualExists) {
                individual = await createAndGetIndividualResponse(requestInfo, individualRequest);
                if (!(individual && individual.id)) {
                    logger.info('Individual creation failed!');
                    throw new Error('Individual creation failed!');
                }
                logger.info("Individual Created ----- " + JSON.stringify(individual))
            }
            // If not applied then apply
            if (!alreadyRegistered) {
                applicationRequest['individualId'] = individual.id;
                application = await createAndGetApplicationResponse(requestInfo, applicationRequest);
                logger.info("Application registered! " + JSON.stringify(application))
            }
            if (individual) {
                let bankAccounts = await getBankAccountDetail(individual.tenantId, individual.id, requestInfo)
                if (bankAccounts && bankAccounts.length) {
                    logger.info('Bank Accounts found');
                    bankAccount = bankAccounts[0]
                    bankAccountExists = true;
                } else {
                    bankAccountRequest['referenceId'] = individual.id
                    bankAccount = await createBankAccount(requestInfo, bankAccountRequest)
                }
            }
           let response = {
                'ResponseInfo': {
                    "apiId": "submitApplication","ver": "v1","ts": 0,"resMsgId": "uief87324","msgId": "submit application","status": "successful"
                },
                'Individual': individual,
                'Application': application,
                'BankAccount': bankAccount
           }
           if (alreadyRegistered && bankAccountExists) {
               response.message = 'applicant_already_registered_for_program'
           } else if (individual && application && bankAccount) {
                response.message = 'applicant_registered_for_program';
           }
           res.send(response)
        } catch (error) {
            logger.error(error.stack || error);
            return renderError(res, `Failed to register application.`);
        }

    })
)


function renderError(res, errorMessage, errorCode) {
    if (errorCode == undefined) errorCode = 500;
    res.status(errorCode).send({ errorMessage });
}

let getIndividualSearchCriteria = (individualRequest) => {
    if (individualRequest['identifiers'] && individualRequest['identifiers']?.length) {
        return {
            "tenantId": individualRequest.tenantId,
            "identifier": {
                "identifierType": "AADHAAR",
                "identifierId": individualRequest['identifiers'][0].identifierId
            }
        };
    } else {
        throw err('Identifiers not present.')
    }
}

let getIndividualDetails = async (individualRequest) => {
    let individualSearchRequest = {};
    
    individualSearchRequest.RequestInfo = getRequestInfo()
    individualSearchRequest.Individual = getIndividualSearchCriteria(individualRequest)

    let individualResponse = await search_individual(individualSearchRequest, individualRequest?.tenantId, 1, 0);

    if (individualResponse?.Individual && individualResponse.Individual.length) {
        return individualResponse.Individual[0]
    } else {
        return null
    }
}

let getApplicationDetails = async (individualId, programCode) => {
    if (!individualId || !programCode) {
        throw err('ProgramCode not present.')
    }
    let applicationSearchRequest = {};
    applicationSearchRequest.RequestInfo = getRequestInfo()
    applicationSearchRequest.criteria = {
        'individualId': individualId,
        'programCode': programCode
    }
    let applicationResponse = await search_application(applicationSearchRequest, 10, 0)
    if (applicationResponse?.Applications && applicationResponse.Applications.length) {
        return applicationResponse.Applications[0]
    } else {
        return null;
    }
}

let createAndGetIndividualResponse = async (requestInfo, individualRequest) => {
    let individualRequestInfo = requestInfo?.userInfo?.uuid ? requestInfo : getRequestInfo();
    let individualCreateRequest =  {
        'RequestInfo': individualRequestInfo,
        'Individual': individualRequest
    }
    let individualCreateResp = await create_individual(individualCreateRequest);
    if (individualCreateResp && individualCreateResp.Individual) {
        return individualCreateResp.Individual
    } else {
        return null
    }
}

let createAndGetApplicationResponse = async (requestInfo, applicationRequest) => {
    let applicationRequestInfo = requestInfo?.userInfo?.uuid ? requestInfo : getRequestInfo();
    let applicationCreateRequest =  {
        'RequestInfo': applicationRequestInfo,
        'Application': applicationRequest
    }
    let applicationCreateResp = await create_application(applicationCreateRequest);
    if (applicationCreateResp && applicationCreateResp.Applications) {
        return applicationCreateResp.Applications[0]
    } else {
        return null
    }
}

let getBankAccountDetail = async (tenantdId, individualId, requestInfo) => {
    let bankAccountRequestInfo = requestInfo?.userInfo?.uuid ? requestInfo : getRequestInfo();
    let bankAccountSearchRequest =  {
        'RequestInfo': bankAccountRequestInfo,
        "bankAccountDetails": {
            "tenantId": tenantdId,
            "referenceId": [
                individualId
            ]
        }
    }
    let bankAccountResp = await search_bank_account_details(bankAccountSearchRequest);
    if (bankAccountResp && bankAccountResp.bankAccounts && bankAccountResp.bankAccounts.length) {
        return bankAccountResp.bankAccounts
    } else {
        return null
    }
}

let createBankAccount = async (requestInfo, bankAccountRequest) => {
    let bankAccountRequestInfo = requestInfo?.userInfo?.uuid ? requestInfo : getRequestInfo();
    let bankAccountCreateRequest =  {
        "RequestInfo": bankAccountRequestInfo,
        "bankAccounts": [
            bankAccountRequest
        ]
    }
    let bankAccountCreateResp = await create_bank_account_details(bankAccountCreateRequest)
    if (bankAccountCreateResp && bankAccountCreateResp.bankAccounts && bankAccountCreateResp.bankAccounts.length) {
        return bankAccountCreateResp.bankAccounts[0]
    } else {
        return null
    }
}



let getRequestInfo = () => {
    return {
        "apiId": "Rainmaker",
        "authToken": "17b86f8a-6063-4028-abef-fbf005c1ba75",
        "userInfo": {
            "id": 618,
            "uuid": "40e3b45a-0f64-4e8c-8768-aab82c095b2d",
            "roles": [
                {
                    "name": "SUPER USER",
                    "code": "SUPERUSER",
                    "tenantId": "pg.citya"
                }
            ],
            "tenantId": "pg"
        },
        "msgId": "1700205653156|en_IN",
        "plainAccessRequest": {}
    }

}

module.exports = router;