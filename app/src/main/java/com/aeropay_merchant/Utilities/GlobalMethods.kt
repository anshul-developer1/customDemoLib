package com.aeropay_merchant.Utilities

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
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
import com.google.firebase.FirebaseApp
import java.util.regex.Pattern
import com.google.firebase.iid.FirebaseInstanceId



class GlobalMethods {

    fun userCognitoLoginHandler(context: Context?, view: View?, userName: String, password: String)
    {
        var cognitoUserPool = CognitoUserPool(context, ConstantsStrings().aws_userpool_id, ConstantsStrings().aws_client_id, ConstantsStrings().aws_client_secret_id,  Regions.US_EAST_1)
        var cognitoUser = cognitoUserPool.getUser()

        var authentication = object : AuthenticationHandler {

            override fun onSuccess(userSession: CognitoUserSession?, newDevice: CognitoDevice?) {
                idToken = userSession!!.idToken.jwtToken
                (context as SignInCredentialActivity).onCognitoSuccess()
            }

            override fun onFailure(exception: Exception?) {
                view!!.isClickable = true
                view!!.isEnabled = true
                (context as SignInCredentialActivity).onCognitoFailure()
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
            loader!!.setContentView(com.aeropay_merchant.R.layout.loader_layout)
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
        dialog.setContentView(R.layout.custom_dialog)
        val autoLoginBtn = dialog.findViewById(R.id.autoLoginImage) as ImageView
        val pinLoginBtn = dialog.findViewById(R.id.pinLoginImage) as ImageView
        val cancelImageView = dialog.findViewById(R.id.pinLoginText) as ImageView

        autoLoginBtn.setOnClickListener(View.OnClickListener {
            PrefKeeper.isLoggedIn = true
            PrefKeeper.isPinEnabled = false
            dialog.dismiss()
        })

        pinLoginBtn.setOnClickListener(View.OnClickListener {
            var pinValue = PrefKeeper.pinValue
            if(pinValue.equals(ConstantsStrings().noValue)){
                var intent = Intent(context,PinEnterActivity::class.java)
                intent.putExtra(ConstantsStrings().isPinActivityName,1)
                (context as HomeActivity).launchActivity(PinEnterActivity::class.java,intent)
                //(context as HomeActivity).launchActivity(SetPinLogin::class.java)
            }
            else {
                PrefKeeper.isPinEnabled = true
                PrefKeeper.isLoggedIn = false
            }
            dialog.dismiss()
        })

        cancelImageView.setOnClickListener(View.OnClickListener {
            dialog.dismiss()
        })

        dialog.show()
    }

    fun autoLoginAction(context : Context?,username : String, password: String, isEntryPoint : String){
        var cognitoUserPool = CognitoUserPool(context, ConstantsStrings().aws_userpool_id, ConstantsStrings().aws_client_id, ConstantsStrings().aws_client_secret_id,  Regions.US_EAST_1)
        var cognitoUser = cognitoUserPool.getUser()

        var authentication = object : AuthenticationHandler {

            override fun onSuccess(userSession: CognitoUserSession?, newDevice: CognitoDevice?) {
                idToken = userSession!!.idToken.jwtToken
                if(isEntryPoint.equals(ConstantsStrings().isValidatePinActivity)){
                    (context as PinEnterActivity).onCognitoSuccess()
                }
                else if(isEntryPoint.equals(ConstantsStrings().isSplashActivity)){
                    (context as SplashActivity).onCognitoSuccess()
                }
                else if(isEntryPoint.equals(ConstantsStrings().isSignInActivity)){
                    (context as SignInScreenActivity).onCognitoSuccess()
                }
            }

            override fun onFailure(exception: Exception?) {
                if(isEntryPoint.equals(ConstantsStrings().isValidatePinActivity)){
                    (context as PinEnterActivity).onCognitoFailure()
                }
                else if(isEntryPoint.equals(ConstantsStrings().isSplashActivity)){
                    (context as SplashActivity).onCognitoFailure()
                }
                else if(isEntryPoint.equals(ConstantsStrings().isSignInActivity)){
                    (context as SignInScreenActivity).onCognitoFailure()
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

    fun getDeviceToken() {
        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener { task ->
            if (task.isSuccessful)
                PrefKeeper.deviceToken = task.result!!.token
        }
    }
}