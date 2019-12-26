package com.aeropay_merchant.communication

interface ICommunicationHandler_APSDK {

    fun onSuccess(outputParms: Int)

    fun onFailure(outputParms: Int)

}