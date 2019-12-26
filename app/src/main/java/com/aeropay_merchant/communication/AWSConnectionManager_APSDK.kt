package com.aeropay_merchant.communication

import AP.AeroPayStagingClient
import AP.model.*
import android.content.Context
import android.os.AsyncTask
import com.aeropay_merchant.Model.AeropayModelManager
import com.aeropay_merchant.Model.FetchMerchantDevicesList
import com.aeropay_merchant.Model.FetchMerchantLocationModel
import com.aeropay_merchant.Model.FetchMerchantProfileModel
import com.aeropay_merchant.Utilities.ConstantsStrings_APSDK
import com.aeropay_merchant.Utilities.GlobalMethods_APSDK
import com.aeropay_merchant.activity.idToken
import com.amazonaws.auth.CognitoCachingCredentialsProvider
import com.amazonaws.mobileconnectors.apigateway.ApiClientFactory
import com.amazonaws.regions.Regions
import com.google.gson.Gson
import android.content.pm.ActivityInfo
import android.app.Activity
import android.content.res.Configuration


class AWSConnectionManager_APSDK  {

    var mContext: Context? = null

    constructor(context: Context) {
        this.mContext = context
    }

    fun hitServer(requestID: Int, callbackHandlerAPSDK: ICommunicationHandler_APSDK, requestObject : Any?) {
        object : AsyncTask<Void, Void, Any>() {

            override fun onPreExecute() {
                super.onPreExecute()
                lockScreenOrientation()
                GlobalMethods_APSDK().showLoader(mContext!!)
            }

            override fun doInBackground(vararg p0: Void?): Any? {

                var credentialsProvider = object : CognitoCachingCredentialsProvider(mContext,
                    ConstantsStrings_APSDK().aws_identitypool_id, Regions.US_EAST_1){}

                var logins = HashMap<String,String>()
                logins.put(ConstantsStrings_APSDK().userPoolLoginType, idToken);
                credentialsProvider.setLogins(logins);

                var factory = object : ApiClientFactory(){}.credentialsProvider(credentialsProvider)

                var client = factory.build(AeroPayStagingClient::class.java)

                var output : Any? = null

                if(requestID.equals(DefineID_APSDK().FETCH_MERCHANT_PROFILE)){
                    output = client.fetchMerchantPost()
                }
                else if(requestID.equals(DefineID_APSDK().FETCH_MERCHANT_LOCATIONS)){
                    output = client.fetchMerchantLocationsPost()
                }
                else if(requestID.equals(DefineID_APSDK().FETCH_MERCHANT_DEVICES)){
                    var merchantLocationDevice = requestObject as MerchantLocationDevices
                    output = client.fetchMerchantLocationDevicesPost(merchantLocationDevice)
                }
                else if(requestID.equals(DefineID_APSDK().REGISTER_MERCHANT_LOCATION_DEVICE)){
                    var registerDevice = requestObject as RegisterMerchantDevice
                    output = client.registerMerchantLocationDevicePost(registerDevice)
                }

                return output as Any
            }

            override fun onPostExecute(result: Any?) {
                super.onPostExecute(result)
                GlobalMethods_APSDK().dismissLoader()
                var objModelManager = AeropayModelManager().getInstance()
                if(requestID.equals(DefineID_APSDK().FETCH_MERCHANT_PROFILE)){
                    var output = result as MerchantResponse
                    var statusCode = output.success.toString()
                    if(statusCode.equals("1")){
                        var stringOutput = Gson().toJson(output)
                        objModelManager.merchantProfileModel = Gson().fromJson(stringOutput,
                            FetchMerchantProfileModel::class.java)
                        callbackHandlerAPSDK.onSuccess(requestID)
                    }
                    else{
                        callbackHandlerAPSDK.onFailure(requestID)
                    }
                }
                else if(requestID.equals(DefineID_APSDK().FETCH_MERCHANT_LOCATIONS)){
                    var output = result as MerchantLocationsResponse
                    var statusCode = output.success.toString()
                    if(statusCode.equals("1")){
                        var stringOutput = Gson().toJson(output)
                        objModelManager.merchantLocationsModel = Gson().fromJson(stringOutput,
                            FetchMerchantLocationModel::class.java)
                        callbackHandlerAPSDK.onSuccess(requestID)
                    }
                    else{
                        callbackHandlerAPSDK.onFailure(requestID)
                    }
                }
                else if(requestID.equals(DefineID_APSDK().FETCH_MERCHANT_DEVICES)){
                    var output = result as MerchantLocationDevicesResponse
                    var statusCode = output.success.toString()
                    if(statusCode.equals("1")){
                        var stringOutput = Gson().toJson(output)
                        objModelManager.merchantDevicesModel = Gson().fromJson(stringOutput,
                            FetchMerchantDevicesList::class.java)
                        callbackHandlerAPSDK.onSuccess(requestID)
                    }
                    else{
                        callbackHandlerAPSDK.onFailure(requestID)
                    }
                }
                else if(requestID.equals(DefineID_APSDK().REGISTER_MERCHANT_LOCATION_DEVICE)){
                    var output = result as RegisterMerchantDeviceResponse
                    var statusCode = output.success.toString()
                    if(statusCode.equals("1")){
                        var stringOutput = Gson().toJson(output)
                        objModelManager.registerMerchantDevices = Gson().fromJson(stringOutput,
                            com.aeropay_merchant.Model.RegisterMerchantDeviceResponse::class.java)
                        callbackHandlerAPSDK.onSuccess(requestID)
                    }
                    else{
                        callbackHandlerAPSDK.onFailure(requestID)
                    }
                }
                unlockScreenOrientation()
            }
        }.execute()}

    private fun lockScreenOrientation() {
        val currentOrientation = mContext!!.getResources().getConfiguration().orientation
        if (currentOrientation == Configuration.ORIENTATION_PORTRAIT) {
            (mContext as Activity).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        } else {
            (mContext as Activity).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
        }
    }

    private fun unlockScreenOrientation() {
        (mContext as Activity).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR)
    }
}