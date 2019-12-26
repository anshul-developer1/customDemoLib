package com.aeropay_merchant.activity

import android.content.Intent
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.*
import com.aeropay_merchant.R
import com.aeropay_merchant.Utilities.*
import com.aeropay_merchant.communication.AWSConnectionManager_APSDK
import com.aeropay_merchant.communication.DefineID_APSDK
import com.aeropay_merchant.view.CustomTextView_APSDK

class PinEnterActivity_APSDK : BaseActivity() {

    var userEntered: String? = null
    var userPin = ""
    lateinit var backButton : ImageView
    var activityName : Int? = null

    val PIN_LENGTH = 4
    var keyPadLockedFlag = false

    var pinBox0: TextView? = null
    var pinBox1: TextView? = null
    var pinBox2: TextView? = null
    var pinBox3: TextView? = null

    var view1: View? = null
    var view2: View? = null
    var view3: View? = null
    var view4: View? = null

    var pinBoxArray: Array<TextView?>? = null
    var pinConfirmationLayout: RelativeLayout? = null

    var button0: CustomTextView_APSDK? = null
    var button1: CustomTextView_APSDK? = null
    var button2: CustomTextView_APSDK? = null
    var button3: CustomTextView_APSDK? = null
    var button4: CustomTextView_APSDK? = null
    var button5: CustomTextView_APSDK? = null
    var button6: CustomTextView_APSDK? = null
    var button7: CustomTextView_APSDK? = null
    var button8: CustomTextView_APSDK? = null
    var button9: CustomTextView_APSDK? = null
    var buttonDelete: CustomTextView_APSDK? = null
    var screenHeading: CustomTextView_APSDK? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userEntered = ""
        setContentView(R.layout.activity_validate_pin_apsdk)

        screenHeading = findViewById(R.id.screenHeading)

        var intent = intent
        activityName = intent.getIntExtra(ConstantsStrings_APSDK().isPinActivityName,0)

        if(activityName == 1){
            screenHeading!!.text = "Set pin code"
            userPin = ""
        }
        else if(activityName == 2){
            screenHeading!!.text = "Confirm pin code"
            userPin = intent.getStringExtra("PIN VALUE")
        }
        else if(activityName == 3){
            screenHeading!!.text = "Login with pin"
            userPin = PrefKeeper_APSDK.pinValue!!
        }
        initialiseControls()
    }

    private fun initialiseControls() {
        view1 = findViewById<View>(R.id.pin1)
        view2 = findViewById<View>(R.id.pin2)
        view3 = findViewById<View>(R.id.pin3)
        view4 = findViewById<View>(R.id.pin4)

        val pinButtonHandler = View.OnClickListener { v ->
            if (keyPadLockedFlag == true) {
                return@OnClickListener
            }

            val pressedButton = v as CustomTextView_APSDK

            if (userEntered!!.length < PIN_LENGTH) {
                userEntered = userEntered + pressedButton.text
                pinBoxArray!![userEntered!!.length - 1]!!.text = "8"

                var length = userEntered!!.length

                if(length == 1){
                    view1!!.visibility = View.INVISIBLE
                }
                else if(length == 2){
                    view2!!.visibility = View.INVISIBLE
                }
                else if(length == 3){
                    view3!!.visibility = View.INVISIBLE
                }
                else if(length == 4){
                    view4!!.visibility = View.INVISIBLE
                }

                if(activityName == 1){
                if (userEntered!!.length == PIN_LENGTH) {
                    var intent = Intent(this@PinEnterActivity_APSDK,PinEnterActivity_APSDK::class.java)
                    intent.putExtra("PIN VALUE",userEntered)
                    intent.putExtra(ConstantsStrings_APSDK().isPinActivityName,2)
                    launchActivity(PinEnterActivity_APSDK::class.java,intent)
                }
                }

                else {
                    if (userEntered!!.length == PIN_LENGTH) {
                        if (userEntered == userPin) {
                            keyPadLockedFlag = true
                            LockKeyPadOperation().execute("")

                            if (activityName == 2) {
                                backButton.visibility = View.GONE
                                pinConfirmationLayout!!.visibility = View.VISIBLE
                                Handler().postDelayed({
                                    moveToHomeActivity()
                                }, 2000)
                            } else if (activityName == 3) {
                                PrefKeeper_APSDK.pinValue = userPin
                                var usernameEncryptValue = PrefKeeper_APSDK.username
                                var passwordEncryptValue = PrefKeeper_APSDK.password

                                GlobalMethods_APSDK().showLoader(this)

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    var usernameIvEncryptValue = PrefKeeper_APSDK.usernameIV
                                    var passwordIvEncryptValue = PrefKeeper_APSDK.passwordIV
                                    var username = KeystoreManager_APSDK.decryptText(
                                        usernameEncryptValue!!,
                                        usernameIvEncryptValue!!,
                                        KeystoreManager_APSDK.ALIAS.USERNAME
                                    )
                                    var password = KeystoreManager_APSDK.decryptText(
                                        passwordEncryptValue!!,
                                        passwordIvEncryptValue!!,
                                        KeystoreManager_APSDK.ALIAS.PWD
                                    )

                                    GlobalMethods_APSDK().autoLoginAction(this@PinEnterActivity_APSDK, username!!, password!!, ConstantsStrings_APSDK().isValidatePinActivity)
                                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                                    var username = KeyStoreHelper_APSDK.decrypt(
                                        KeystoreManager_APSDK.ALIAS.USERNAME,
                                        usernameEncryptValue
                                    )
                                    var password = KeyStoreHelper_APSDK.decrypt(
                                        KeystoreManager_APSDK.ALIAS.PWD,
                                        passwordEncryptValue
                                    )
                                    GlobalMethods_APSDK().autoLoginAction(
                                        this@PinEnterActivity_APSDK,
                                        username!!,
                                        password!!,
                                        ConstantsStrings_APSDK().isValidatePinActivity
                                    )
                                }
                            }
                        } else {
                            keyPadLockedFlag = true
                            LockKeyPadOperation().execute("")
                            Toast.makeText(this, "Please enter a valid Pin", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            } else {
                //Roll over
                pinBoxArray!![0]!!.text = ""
                pinBoxArray!![1]!!.text = ""
                pinBoxArray!![2]!!.text = ""
                pinBoxArray!![3]!!.text = ""

                userEntered = ""

                userEntered = userEntered + pressedButton.text

                pinBoxArray!![userEntered!!.length - 1]!!.text = "8"

            }
        }

        setListeners(pinButtonHandler)


        pinBox0 = findViewById<View>(R.id.pinBox0) as TextView
        pinBox1 = findViewById<View>(R.id.pinBox1) as TextView
        pinBox2 = findViewById<View>(R.id.pinBox2) as TextView
        pinBox3 = findViewById<View>(R.id.pinBox3) as TextView
        pinConfirmationLayout = findViewById<View>(R.id.pinConfirmationLayout) as RelativeLayout


        pinBoxArray = arrayOfNulls<TextView>(PIN_LENGTH)!!
        pinBoxArray!![0] = pinBox0
        pinBoxArray!![1] = pinBox1
        pinBoxArray!![2] = pinBox2
        pinBoxArray!![3] = pinBox3

        backButton = findViewById(R.id.back_button)
        backButton.setOnClickListener(View.OnClickListener {
            if(activityName == 3){
                launchActivity(SignInScreenActivity_APSDK::class.java)
                finishAffinity()
            }
            else {
                finish()
            }
        })

        buttonDelete = findViewById<View>(R.id.buttonDeleteBack) as CustomTextView_APSDK
        buttonDelete!!.setOnClickListener(View.OnClickListener {
            if (keyPadLockedFlag == true) {
                return@OnClickListener
            }

            if (userEntered!!.length > 0) {
                userEntered = userEntered!!.substring(0, userEntered!!.length - 1)
                pinBoxArray?.get(userEntered!!.length)!!.text = ""
            }

            var length = userEntered!!.length

            if(length == 0){
                view1!!.visibility = View.VISIBLE
            }
            else if(length == 1){
                view2!!.visibility = View.VISIBLE
            }
            else if(length == 2){
                view3!!.visibility = View.VISIBLE
            }
            else if(length == 3){
                view4!!.visibility = View.VISIBLE
            }
        }
        )
    }

    // navigate to screen and storing values in shared preference
    private fun moveToHomeActivity() {
        PrefKeeper_APSDK.pinValue = userPin
        PrefKeeper_APSDK.isPinEnabled = true
        PrefKeeper_APSDK.isLoggedIn = false
        launchActivity(HomeActivity_APSDK::class.java)
    }

    private fun setListeners(pinButtonHandler: View.OnClickListener) {

        button0 = findViewById<View>(R.id.button0) as CustomTextView_APSDK
        button0!!.setOnClickListener(pinButtonHandler)

        button1 = findViewById<View>(R.id.button1) as CustomTextView_APSDK
        button1!!.setOnClickListener(pinButtonHandler)

        button2 = findViewById<View>(R.id.button2) as CustomTextView_APSDK
        button2!!.setOnClickListener(pinButtonHandler)


        button3 = findViewById<View>(R.id.button3) as CustomTextView_APSDK
        button3!!.setOnClickListener(pinButtonHandler)

        button4 = findViewById<View>(R.id.button4) as CustomTextView_APSDK
        button4!!.setOnClickListener(pinButtonHandler)

        button5 = findViewById<View>(R.id.button5) as CustomTextView_APSDK
        button5!!.setOnClickListener(pinButtonHandler)

        button6 = findViewById<View>(R.id.button6) as CustomTextView_APSDK
        button6!!.setOnClickListener(pinButtonHandler)

        button7 = findViewById<View>(R.id.button7) as CustomTextView_APSDK
        button7!!.setOnClickListener(pinButtonHandler)

        button8 = findViewById<View>(R.id.button8) as CustomTextView_APSDK
        button8!!.setOnClickListener(pinButtonHandler)

        button9 = findViewById<View>(R.id.button9) as CustomTextView_APSDK
        button9!!.setOnClickListener(pinButtonHandler)
    }

    // Login success callback method
    fun onCognitoSuccess(){
        var awsConnectionManager = AWSConnectionManager_APSDK(this)
        awsConnectionManager.hitServer(DefineID_APSDK().FETCH_MERCHANT_PROFILE,this,null)
    }

    // Login failure callback method
    fun onCognitoFailure(){
        GlobalMethods_APSDK().dismissLoader()
        Toast.makeText(this,"Pin-Login was not successfull", Toast.LENGTH_LONG).show()
        launchActivity(SignInScreenActivity_APSDK::class.java)
    }

    // locking the keypad after PIN length is matched
    private inner class LockKeyPadOperation : AsyncTask<String, Void, String>() {

        override fun doInBackground(vararg params: String): String {
            for (i in 0..1) {
                try {
                    Thread.sleep(1000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }

            }

            return "Executed"
        }

        override fun onPostExecute(result: String) {

            if(!(userEntered.equals(userPin))){
                view1!!.visibility = View.VISIBLE
                view2!!.visibility = View.VISIBLE
                view3!!.visibility = View.VISIBLE
                view4!!.visibility = View.VISIBLE

                pinBoxArray!!.get(0)!!.text = ""
                pinBoxArray!![1]!!.text = ""
                pinBoxArray!![2]!!.text = ""
                pinBoxArray!![3]!!.text = ""

                userEntered = ""

                keyPadLockedFlag = false
            }
        }

        override fun onPreExecute() {}

        override fun onProgressUpdate(vararg values: Void) {}
    }


}