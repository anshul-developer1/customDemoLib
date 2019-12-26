package com.aeropay_merchant.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import com.aeropay_merchant.R
import com.aeropay_merchant.Utilities.*
import com.aeropay_merchant.communication.AWSConnectionManager_APSDK
import com.aeropay_merchant.communication.DefineID_APSDK

class SplashActivity_APSDK : BaseActivity() {

    private var mDelayHandler: Handler? = null
    private val SPLASH_DELAY: Long = 3000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_apsdk)
        PrefKeeper_APSDK.init(this)
        navigateToScreen()
    }

    //method to check for auto login/pin login or normal login and redirection
    private fun navigateToScreen() {
        if(PrefKeeper_APSDK.isLoggedIn){
            GlobalMethods_APSDK().showLoader(this)
            var usernameEncryptValue = PrefKeeper_APSDK.username
            var passwordEncryptValue = PrefKeeper_APSDK.password

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                var usernameIvEncryptValue = PrefKeeper_APSDK.usernameIV
                var passwordIvEncryptValue = PrefKeeper_APSDK.passwordIV
                var username = KeystoreManager_APSDK.decryptText(usernameEncryptValue!!,usernameIvEncryptValue!!,KeystoreManager_APSDK.ALIAS.USERNAME)
                var password = KeystoreManager_APSDK.decryptText(passwordEncryptValue!!,passwordIvEncryptValue!!,KeystoreManager_APSDK.ALIAS.PWD)

                GlobalMethods_APSDK().autoLoginAction(this@SplashActivity_APSDK,username!!,password!!,
                    ConstantsStrings_APSDK().isSplashActivity)
            }
            else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                var username = KeyStoreHelper_APSDK.decrypt(KeystoreManager_APSDK.ALIAS.USERNAME,usernameEncryptValue)
                var password = KeyStoreHelper_APSDK.decrypt(KeystoreManager_APSDK.ALIAS.PWD,passwordEncryptValue)
                GlobalMethods_APSDK().autoLoginAction(this@SplashActivity_APSDK,username!!,password!!,ConstantsStrings_APSDK().isSplashActivity)
            }
        }
        else{
            mDelayHandler = Handler()
            mDelayHandler!!.postDelayed(mRunnable, SPLASH_DELAY)
        }

    }

    //to show Splash for 3 seconds
    internal val mRunnable: Runnable = Runnable {
         if(PrefKeeper_APSDK.isPinEnabled) {
             var intent = Intent(this@SplashActivity_APSDK,PinEnterActivity_APSDK::class.java)
             intent.putExtra(ConstantsStrings_APSDK().isPinActivityName,3)
             launchActivity(PinEnterActivity_APSDK::class.java,intent)
             finish()
         }
        else {
             launchActivity(SignInScreenActivity_APSDK::class.java)
             finish()
         }
    }

    // to get callback for login success
    fun onCognitoSuccess(){
        var awsConnectionManager = AWSConnectionManager_APSDK(this)
        awsConnectionManager.hitServer(DefineID_APSDK().FETCH_MERCHANT_PROFILE,this,null)
    }

    // to get callback for login failure
    fun onCognitoFailure(){
        GlobalMethods_APSDK().dismissLoader()
        Toast.makeText(this,"Auto-Login was not successfull", Toast.LENGTH_LONG).show()
        launchActivity(SignInScreenActivity_APSDK::class.java)
        finish()
    }
}
