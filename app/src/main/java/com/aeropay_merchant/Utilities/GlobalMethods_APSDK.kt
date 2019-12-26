package com.aeropay_merchant.Utilities

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.ImageView
import com.aeropay_merchant.R
import com.aeropay_merchant.activity.*
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationDetails
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChallengeContinuation
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler
import com.amazonaws.regions.Regions
import java.util.regex.Pattern
import com.google.firebase.iid.FirebaseInstanceId



class GlobalMethods_APSDK {

    fun userCognitoLoginHandler(context: Context?, view: View?, userName: String, password: String)
    {
        var cognitoUserPool = CognitoUserPool(context, ConstantsStrings_APSDK().aws_userpool_id, ConstantsStrings_APSDK().aws_client_id, ConstantsStrings_APSDK().aws_client_secret_id,  Regions.US_EAST_1)
        var cognitoUser = cognitoUserPool.getUser()

        var authentication = object : AuthenticationHandler {

            override fun onSuccess(userSession: CognitoUserSession?, newDevice: CognitoDevice?) {
                idToken = userSession!!.idToken.jwtToken
                (context as SignInCredentialActivity_APSDK).onCognitoSuccess()
            }

            override fun onFailure(exception: Exception?) {
                view!!.isClickable = true
                view!!.isEnabled = true
                (context as SignInCredentialActivity_APSDK).onCognitoFailure()
            }

            override fun getAuthenticationDetails(authenticationContinuation: AuthenticationContinuation?, UserId: String?) {
                var authenticationDetails = AuthenticationDetails(userName,password,null)
                authenticationContinuation!!.setAuthenticationDetails(authenticationDetails)
                authenticationContinuation.continueTask()
            }

            override fun authenticationChallenge(continuation: ChallengeContinuation?) {

            }

            override fun getMFACode(continuation: MultiFactorAuthenticationContinuation?) {
                var code = continuation!!.parameters.attributeName
                continuation!!.setMfaCode("1111")
                continuation!!.continueTask();
            }
        }
        cognitoUser.getSessionInBackground(authentication)
    }

    fun showLoader(ctx: Context) {
        if(loader == null){
            loader = Dialog(ctx)
            loader!!.setContentView(com.aeropay_merchant.R.layout.loader_layout_apsdk)
            loader!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            loader?.setCancelable(false)
            loader!!.show()
        }
    }

    fun dismissLoader() {
        if(loader != null){
            loader!!.dismiss()
            loader = null
        }
    }

    fun showDialog(context: Context?) {
        val dialog = Dialog(context!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.custom_dialog_apsdk)
        val autoLoginBtn = dialog.findViewById(R.id.autoLoginImage) as ImageView
        val pinLoginBtn = dialog.findViewById(R.id.pinLoginImage) as ImageView
        val cancelImageView = dialog.findViewById(R.id.pinLoginText) as ImageView

        autoLoginBtn.setOnClickListener(View.OnClickListener {
            PrefKeeper_APSDK.isLoggedIn = true
            PrefKeeper_APSDK.isPinEnabled = false
            dialog.dismiss()
        })

        pinLoginBtn.setOnClickListener(View.OnClickListener {
            var pinValue = PrefKeeper_APSDK.pinValue
            if(pinValue.equals(ConstantsStrings_APSDK().noValue)){
                var intent = Intent(context,PinEnterActivity_APSDK::class.java)
                intent.putExtra(ConstantsStrings_APSDK().isPinActivityName,1)
                (context as HomeActivity_APSDK).launchActivity(PinEnterActivity_APSDK::class.java,intent)
                //(context as HomeActivity_APSDK).launchActivity(SetPinLogin::class.java)
            }
            else {
                PrefKeeper_APSDK.isPinEnabled = true
                PrefKeeper_APSDK.isLoggedIn = false
            }
            dialog.dismiss()
        })

        cancelImageView.setOnClickListener(View.OnClickListener {
            dialog.dismiss()
        })

        dialog.show()
    }

    fun autoLoginAction(context : Context?,username : String, password: String, isEntryPoint : String){
        var cognitoUserPool = CognitoUserPool(context, ConstantsStrings_APSDK().aws_userpool_id, ConstantsStrings_APSDK().aws_client_id, ConstantsStrings_APSDK().aws_client_secret_id,  Regions.US_EAST_1)
        var cognitoUser = cognitoUserPool.getUser()

        var authentication = object : AuthenticationHandler {

            override fun onSuccess(userSession: CognitoUserSession?, newDevice: CognitoDevice?) {
                idToken = userSession!!.idToken.jwtToken
                if(isEntryPoint.equals(ConstantsStrings_APSDK().isValidatePinActivity)){
                    (context as PinEnterActivity_APSDK).onCognitoSuccess()
                }
                else if(isEntryPoint.equals(ConstantsStrings_APSDK().isSplashActivity)){
                    (context as SplashActivity_APSDK).onCognitoSuccess()
                }
                else if(isEntryPoint.equals(ConstantsStrings_APSDK().isSignInActivity)){
                    (context as SignInScreenActivity_APSDK).onCognitoSuccess()
                }
            }

            override fun onFailure(exception: Exception?) {
                if(isEntryPoint.equals(ConstantsStrings_APSDK().isValidatePinActivity)){
                    (context as PinEnterActivity_APSDK).onCognitoFailure()
                }
                else if(isEntryPoint.equals(ConstantsStrings_APSDK().isSplashActivity)){
                    (context as SplashActivity_APSDK).onCognitoFailure()
                }
                else if(isEntryPoint.equals(ConstantsStrings_APSDK().isSignInActivity)){
                    (context as SignInScreenActivity_APSDK).onCognitoFailure()
                }
            }

            override fun getAuthenticationDetails(authenticationContinuation: AuthenticationContinuation?, UserId: String?) {
                var authenticationDetails = AuthenticationDetails(username,password,null)
                authenticationContinuation!!.setAuthenticationDetails(authenticationDetails)
                authenticationContinuation.continueTask()
            }

            override fun authenticationChallenge(continuation: ChallengeContinuation?) {

            }

            override fun getMFACode(continuation: MultiFactorAuthenticationContinuation?) {
                var code = continuation!!.parameters.attributeName
                continuation!!.setMfaCode("1111")
                continuation!!.continueTask();
            }
        }
        cognitoUser.getSessionInBackground(authentication)
    }

    fun isValidEmailId(email: String): Boolean {
        var isEmail = Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches()
        return isEmail
    }

    fun getDeviceToken(context : Context?) {

         //var secondApp = FirebaseApp.initializeApp(context!!, options, "second app");




        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener { task ->
            if (task.isSuccessful)
                PrefKeeper_APSDK.deviceToken = task.result!!.token
            var a = PrefKeeper_APSDK.deviceToken
            Log.d("Aeropay token",a )
        }
    }
}