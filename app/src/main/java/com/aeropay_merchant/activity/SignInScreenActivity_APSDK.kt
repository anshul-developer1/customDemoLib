package com.aeropay_merchant.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.aeropay_merchant.R
import com.aeropay_merchant.Utilities.*
import com.aeropay_merchant.communication.AWSConnectionManager_APSDK
import com.aeropay_merchant.communication.DefineID_APSDK

class SignInScreenActivity_APSDK : BaseActivity() {

    lateinit var signInButton : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in_screen_apsdk)

        signInButton = findViewById(R.id.signInButton)
    }

    //to perform login operationb
    fun signInButtonClick(view: View) {
        view.isEnabled = false
        view.isClickable = false

        if(PrefKeeper_APSDK.isLoggedIn){
            var usernameEncryptValue = PrefKeeper_APSDK.username
            var passwordEncryptValue = PrefKeeper_APSDK.password

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                var usernameIvEncryptValue = PrefKeeper_APSDK.usernameIV
                var passwordIvEncryptValue = PrefKeeper_APSDK.passwordIV
                var username = KeystoreManager_APSDK.decryptText(usernameEncryptValue!!,usernameIvEncryptValue!!, KeystoreManager_APSDK.ALIAS.USERNAME)
                var password = KeystoreManager_APSDK.decryptText(passwordEncryptValue!!,passwordIvEncryptValue!!, KeystoreManager_APSDK.ALIAS.PWD)

                GlobalMethods_APSDK().autoLoginAction(this@SignInScreenActivity_APSDK,username!!,password!!,ConstantsStrings_APSDK().isSignInActivity)
            }
            else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                var username = KeyStoreHelper_APSDK.decrypt(KeystoreManager_APSDK.ALIAS.USERNAME,usernameEncryptValue)
                var password = KeyStoreHelper_APSDK.decrypt(KeystoreManager_APSDK.ALIAS.PWD,passwordEncryptValue)
                GlobalMethods_APSDK().autoLoginAction(this@SignInScreenActivity_APSDK,username!!,password!!,ConstantsStrings_APSDK().isSignInActivity)
            }
        }
        else if(PrefKeeper_APSDK.isPinEnabled){
            var intent = Intent(this@SignInScreenActivity_APSDK,PinEnterActivity_APSDK::class.java)
            intent.putExtra(ConstantsStrings_APSDK().isPinActivityName,3)
            launchActivity(PinEnterActivity_APSDK::class.java,intent)
        }
        else{
            launchActivity(SignInCredentialActivity_APSDK::class.java)
        }
    }

    // to get calback for login success
    fun onCognitoSuccess(){
        var awsConnectionManager = AWSConnectionManager_APSDK(this)
        awsConnectionManager.hitServer(DefineID_APSDK().FETCH_MERCHANT_PROFILE,this,null)
    }

    // to get callback for login failure
    fun onCognitoFailure(){
        GlobalMethods_APSDK().dismissLoader()
        Toast.makeText(this,"Auto-Login was not successfull", Toast.LENGTH_LONG).show()
        launchActivity(SignInScreenActivity_APSDK::class.java)
    }

    override fun onResume() {
        super.onResume()
        signInButton.isEnabled = true
        signInButton.isClickable = true
    }
}
