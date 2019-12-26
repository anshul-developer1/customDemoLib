package com.aeropay_merchant.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import com.aeropay_merchant.R
import com.aeropay_merchant.Utilities.ConstantsStrings_APSDK
import com.aeropay_merchant.Utilities.PrefKeeper_APSDK

class FastLoginActivity_APSDK : BaseActivity() {

    lateinit var save : Button
    lateinit var autoLoginToggle : ToggleButton
    lateinit var pinLoginToggle : ToggleButton
    lateinit var updateTextView: TextView
    lateinit var backButton : ImageView

    var isLogin : Boolean = false
    var isPin : Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fast_login_apsdk)

        initialiseControls()
        setListeners()
    }

    override fun onResume() {
        super.onResume()
        setInitialToggleStage()
    }

    // setting listeners on UI for View click
    private fun setListeners() {
        autoLoginToggle.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
                    if(p1) {
                        isLogin = true
                        isPin = false
                        pinLoginToggle.isChecked = false
                    }
                    else {
                        isLogin = false
                    }
            }
        })

        pinLoginToggle.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
                if(p1) {
                    moveToSetPin()
                }
                else {
                    isPin = false
                }
            }
        })

        updateTextView.setOnClickListener(View.OnClickListener {
            moveToSetPin()
        })
        backButton.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    // inflating UI controls
    private fun initialiseControls() {
        save = findViewById(R.id.saveButton)
        backButton = findViewById(R.id.back_button)
        autoLoginToggle = findViewById(R.id.autoLoginToggle)
        pinLoginToggle = findViewById(R.id.pinLoginToggle)
        updateTextView = findViewById(R.id.updateText)

        setInitialToggleStage()
    }

    // setting toggle button initial state
    private fun setInitialToggleStage() {
            var isLoginEnabled = PrefKeeper_APSDK.isLoggedIn
            if(isLoginEnabled){
                autoLoginToggle.isChecked = true
                isLogin = true
            }
            else{
                autoLoginToggle.isChecked = false
                isLogin = false
            }

            var isPinEnabled = PrefKeeper_APSDK.isPinEnabled
            if(isPinEnabled){
                pinLoginToggle.isChecked = true
                isPin = true
            }
            else{
                pinLoginToggle.isChecked = false
                isPin = false
            }
    }

    // save button click event
    fun onSaveButtonClick(view: View) {
        PrefKeeper_APSDK.isLoggedIn = isLogin
        PrefKeeper_APSDK.isPinEnabled = isPin
        launchActivity(NavigationMenuActivity_APSDK::class.java)
        finish()
    }

    // move to set Pin Page
    fun moveToSetPin(){
        var intent = Intent(this@FastLoginActivity_APSDK,PinEnterActivity_APSDK::class.java)
        intent.putExtra(ConstantsStrings_APSDK().isPinActivityName,1)
        launchActivity(PinEnterActivity_APSDK::class.java,intent)
    }

}
