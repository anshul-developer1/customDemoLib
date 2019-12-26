package com.aeropay_merchant.activity

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.aeropay_merchant.R
import com.aeropay_merchant.communication.DefineID_APSDK
import com.aeropay_merchant.communication.ICommunicationHandler_APSDK

var loader : Dialog? = null
lateinit var idToken : String

open class BaseActivity : AppCompatActivity() , ICommunicationHandler_APSDK{

    private var mToast: Toast? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base_apsdk)
    }

    //move from one activity to other
    fun launchActivity(activityClass: Class<out BaseActivity>, intent : Intent? = null) {
        if(intent != null){
            startActivity(intent)
            overridePendingTransition(R.anim.left_enter,R.anim.right_exit)
        }
        else {
            startActivity(Intent(this, activityClass))
            overridePendingTransition(R.anim.left_enter,R.anim.right_exit)
        }
    }

    fun showMsgToast(msg: String) {
        if (mToast != null) {
            mToast?.cancel()
            mToast = null
        }
        mToast = Toast.makeText(this, msg, Toast.LENGTH_SHORT)
        mToast?.show()
    }

    // Network callback for success
    override fun onSuccess(outputParms: Int) {
        if(outputParms.equals(DefineID_APSDK().FETCH_MERCHANT_PROFILE)){
            launchActivity(HomeActivity_APSDK::class.java)
            finishAffinity()
        }
        else if(outputParms.equals(DefineID_APSDK().FETCH_MERCHANT_LOCATIONS)){
            launchActivity(SettingsScreenActivity_APSDK::class.java)
        }
        else if(outputParms.equals(DefineID_APSDK().FETCH_MERCHANT_DEVICES)){
            (this as SettingsScreenActivity_APSDK).createDeviceSpinner()
        }
        else if(outputParms.equals(DefineID_APSDK().REGISTER_MERCHANT_LOCATION_DEVICE)){
            (this as HomeActivity_APSDK).creatBeaconTransmission()
        }
    }

    // Network callback for failure
    override fun onFailure(outputParms: Int) {
        if(outputParms.equals(DefineID_APSDK().FETCH_MERCHANT_PROFILE)){
            showMsgToast("API Failure")
        }
        else if(outputParms.equals(DefineID_APSDK().FETCH_MERCHANT_LOCATIONS)){
            showMsgToast("API Failure")
        }
        else if(outputParms.equals(DefineID_APSDK().FETCH_MERCHANT_DEVICES)){
            showMsgToast("API Failure")
        }
        else if(outputParms.equals(DefineID_APSDK().REGISTER_MERCHANT_LOCATION_DEVICE)){
            showMsgToast("API Failure")
        }
    }

    // removing the android native back press functionality
    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.right_enter,R.anim.left_exit)
    }
}
