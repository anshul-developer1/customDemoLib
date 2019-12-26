package com.aeropay_merchant.activity

import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.Toast
import com.aeropay_merchant.R
import com.aeropay_merchant.Utilities.*
import com.aeropay_merchant.communication.AWSConnectionManager_APSDK
import com.aeropay_merchant.communication.DefineID_APSDK
import com.aeropay_merchant.view.CustomEditText_APSDK
import android.widget.ImageView


class SignInCredentialActivity_APSDK : BaseActivity(){

    lateinit var userNameEditAPSDK : CustomEditText_APSDK
    lateinit var passwordEditAPSDK : CustomEditText_APSDK
    lateinit var signInButton : ImageView
    lateinit var userName : String
    lateinit var password : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in_credential_apsdk)

        initialiseControls()
    }

    private fun initialiseControls() {
        userNameEditAPSDK = findViewById(R.id.userEmail)
        passwordEditAPSDK = findViewById(R.id.userPassword)
        signInButton = findViewById(R.id.signInButton)

        var userNameValue = PrefKeeper_APSDK.username
        if(!(userNameValue.equals(ConstantsStrings_APSDK().noValue))){
            var usernameEncryptValue = PrefKeeper_APSDK.username

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                var usernameIvEncryptValue = PrefKeeper_APSDK.usernameIV
                var username = KeystoreManager_APSDK.decryptText(usernameEncryptValue!!,usernameIvEncryptValue!!, KeystoreManager_APSDK.ALIAS.USERNAME)
                userNameEditAPSDK.setText(username)
            }
            else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                var usernameEncryptValue = PrefKeeper_APSDK.username
                var username = KeyStoreHelper_APSDK.decrypt(KeystoreManager_APSDK.ALIAS.USERNAME,usernameEncryptValue)
                userNameEditAPSDK.setText(username)
            }
        }
    }

    // check for email and password validations for Login
    fun createUserValidation(view: View) {
         userName = userNameEditAPSDK.text.toString()
         password = passwordEditAPSDK.text.toString()
         userName = "daniel.muller@aeropayments.com"
         password = "Password*12345"

        if(userName.trim().isNullOrEmpty() || password!!.trim().isNullOrEmpty()){
            Toast.makeText(this,"Please enter Email and password.",Toast.LENGTH_SHORT).show()
        }
        else if(userName.trim().isNullOrEmpty()){
            Toast.makeText(this,"Please enter your Email.",Toast.LENGTH_SHORT).show()
        }
        else if(password!!.trim().isNullOrEmpty()){
            Toast.makeText(this,"Please enter your password.",Toast.LENGTH_SHORT).show()
        }
        else if(!(GlobalMethods_APSDK().isValidEmailId(userName))){
            Toast.makeText(this,"Please enter a valid Email ID.",Toast.LENGTH_SHORT).show()
        }
        else
        {
        GlobalMethods_APSDK().showLoader(this)
        view.isClickable = false
        view.isEnabled = false
        GlobalMethods_APSDK().userCognitoLoginHandler(this@SignInCredentialActivity_APSDK, view,userName,password)
        }
    }

    //callback for AWS Login success
    fun onCognitoSuccess(){
        savingCredentialInkeystore()
        var awsConnectionManager = AWSConnectionManager_APSDK(this)
        awsConnectionManager.hitServer(DefineID_APSDK().FETCH_MERCHANT_PROFILE,this,null)
    }

    //saving Login Credentials in Key Store
    private fun savingCredentialInkeystore() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                var usernameArray = KeystoreEncryptor_APSDK().encryptText(KeystoreManager_APSDK.ALIAS.USERNAME, userName)
                PrefKeeper_APSDK.username = Base64.encodeToString(usernameArray, Base64.DEFAULT)
                var passwordArray = KeystoreEncryptor_APSDK().encryptText(KeystoreManager_APSDK.ALIAS.PWD, password)
                PrefKeeper_APSDK.password = Base64.encodeToString(passwordArray, Base64.DEFAULT)
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                KeyStoreHelper_APSDK.createKeys(this, KeystoreManager_APSDK.ALIAS.USERNAME)
                KeyStoreHelper_APSDK.createKeys(this, KeystoreManager_APSDK.ALIAS.PWD)
                PrefKeeper_APSDK.username = KeyStoreHelper_APSDK.encrypt(KeystoreManager_APSDK.ALIAS.USERNAME, userName)
                PrefKeeper_APSDK.password = KeyStoreHelper_APSDK.encrypt(KeystoreManager_APSDK.ALIAS.PWD, password)
            }
        }

    //callback for AWS Login failure
    fun onCognitoFailure(){
        GlobalMethods_APSDK().dismissLoader()
        Toast.makeText(this,"Invalid username or password", Toast.LENGTH_LONG).show()
    }
}
