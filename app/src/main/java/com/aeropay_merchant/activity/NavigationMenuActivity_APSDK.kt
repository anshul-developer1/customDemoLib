package com.aeropay_merchant.activity

import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.*
import com.aeropay_merchant.Model.AeropayModelManager
import com.aeropay_merchant.R
import com.aeropay_merchant.Utilities.*
import com.aeropay_merchant.adapter.NavigationMenuAdapter_APSDK
import com.aeropay_merchant.communication.AWSConnectionManager_APSDK
import com.aeropay_merchant.communication.DefineID_APSDK

class NavigationMenuActivity_APSDK : BaseActivity() {

    lateinit var menuListView : ListView
    lateinit var logout : Button
    lateinit var merchantName : TextView
    lateinit var merchantEmail : TextView
    lateinit var merchantStore : TextView
    lateinit var backButton : ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation_menu_apsdk)
        initialiseControls()
        setListeners()

    }

    // setting on click listeners for UI
    private fun setListeners() {
        menuListView.setOnItemClickListener { parent, view, position, id ->
            if(position == 0){
                var awsConnectionManager = AWSConnectionManager_APSDK(this)
                awsConnectionManager.hitServer(DefineID_APSDK().FETCH_MERCHANT_LOCATIONS,this,null)
            }
            else if(position == 1){
                launchActivity(FastLoginActivity_APSDK::class.java)
            }
        }

        backButton.setOnClickListener(View.OnClickListener {
            finish()
        })
    }


    // inflating UI controls
    private fun initialiseControls() {
        menuListView = findViewById(R.id.itemsList)
        logout = findViewById(R.id.logoutButton)
        merchantName = findViewById(R.id.merchantNameText)
        merchantEmail = findViewById(R.id.merchantEmail)
        merchantStore = findViewById(R.id.storeNameText)
        backButton = findViewById(R.id.back_button)


        var itemsNameArray =  resources?.getStringArray(R.array.navigation_items)
        var itemsImageArray =  arrayOf(R.drawable.settings, R.drawable.timer)
        val itemsListView = NavigationMenuAdapter_APSDK(this,itemsNameArray!!,itemsImageArray)
        menuListView ?.adapter = itemsListView

        var objModelManager = AeropayModelManager().getInstance()
        merchantName.text = objModelManager.merchantProfileModel.merchant.name.toString()

        var usernameEncryptValue = PrefKeeper_APSDK.username

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            var usernameIvEncryptValue = PrefKeeper_APSDK.usernameIV
            var username = KeystoreManager_APSDK.decryptText(usernameEncryptValue!!,usernameIvEncryptValue!!, KeystoreManager_APSDK.ALIAS.USERNAME)
            merchantEmail.text = username
        }
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            var usernameEncryptValue = PrefKeeper_APSDK.username
            var username = KeyStoreHelper_APSDK.decrypt(KeystoreManager_APSDK.ALIAS.USERNAME,usernameEncryptValue)
            merchantEmail.text = username
        }

        var storeName = PrefKeeper_APSDK.storeName
        if(storeName.equals(ConstantsStrings_APSDK().noValue))
        {
            merchantStore.text = ""
        }
        else{
            merchantStore.text = storeName
        }
    }

    // sign out button click event
    fun onLogoutButtonClick(view: View) {
        if(PrefKeeper_APSDK.isPinEnabled || PrefKeeper_APSDK.isLoggedIn){
            finishAffinity()
            launchActivity(SignInScreenActivity_APSDK::class.java)
        }
        else{
            var usernameEncryptValue = PrefKeeper_APSDK.username

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                var usernameIvEncryptValue = PrefKeeper_APSDK.usernameIV
                PrefKeeper_APSDK.clear()
                var username = KeystoreManager_APSDK.decryptText(usernameEncryptValue!!,usernameIvEncryptValue!!, KeystoreManager_APSDK.ALIAS.USERNAME)
                savingCredentialInkeystore(username)
            }
            else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                var usernameEncryptValue = PrefKeeper_APSDK.username
                PrefKeeper_APSDK.clear()
                var username = KeyStoreHelper_APSDK.decrypt(KeystoreManager_APSDK.ALIAS.USERNAME,usernameEncryptValue)
                savingCredentialInkeystore(username)
            }

            finishAffinity()
            launchActivity(SignInScreenActivity_APSDK::class.java)
        }
    }


    private fun savingCredentialInkeystore(username: String?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            var usernameArray = KeystoreEncryptor_APSDK().encryptText(KeystoreManager_APSDK.ALIAS.USERNAME, username!!)
            PrefKeeper_APSDK.username = Base64.encodeToString(usernameArray, Base64.DEFAULT)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            KeyStoreHelper_APSDK.createKeys(this, KeystoreManager_APSDK.ALIAS.USERNAME)
            KeyStoreHelper_APSDK.createKeys(this, KeystoreManager_APSDK.ALIAS.PWD)
            PrefKeeper_APSDK.username = KeyStoreHelper_APSDK.encrypt(KeystoreManager_APSDK.ALIAS.USERNAME, username)
        }
    }
}
