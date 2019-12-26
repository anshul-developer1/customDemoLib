package com.aeropay_merchant.activity

import AP.model.MerchantLocationDevices
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.*
import com.aeropay_merchant.Model.AeropayModelManager
import com.aeropay_merchant.R
import com.aeropay_merchant.Utilities.PrefKeeper_APSDK
import com.aeropay_merchant.communication.AWSConnectionManager_APSDK
import com.aeropay_merchant.communication.DefineID_APSDK


class SettingsScreenActivity_APSDK : BaseActivity() {

    lateinit var save : Button
    lateinit var deviceNameSpinner : Spinner
    lateinit var storeLocationSpinner : Spinner
    var storeLocation : String? = null
    var deviceName : String? = null
    lateinit var backButton : ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings_screen_apsdk)

        initialiseControls()
        setListeners()

    }


    // setting listeners for UI view
    private fun setListeners() {
        backButton.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    // inflating UI controls
    private fun initialiseControls() {
        save = findViewById(R.id.saveButton)
        backButton = findViewById(R.id.back_button)
        storeLocationSpinner = findViewById(R.id.storeSpinner)
        deviceNameSpinner = findViewById(R.id.deviceSpinner)

        deviceNameSpinner.visibility = View.GONE

        createStoreSpinner(this)
    }

    // creating spinner to select store from dropdown list
    private fun createStoreSpinner(context: Context?) {

        var objModelManager = AeropayModelManager().getInstance()
        var modelOutPut = objModelManager.merchantLocationsModel

        var arraySize = modelOutPut.locations.size - 1
        var storeListName: MutableList<String> = ArrayList()

        storeListName.add(0,"Select store")
        var count = 1

        for(i in 0..arraySize){
            storeListName.add(count,modelOutPut.locations[i].name)
            count++
        }

        if (storeLocationSpinner != null) {
            val arrayAdapter = ArrayAdapter<String>(context!!,R.layout.spinner_layout_apsdk, storeListName!!)
            storeLocationSpinner?.adapter = arrayAdapter

            storeLocationSpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    if(position != 0)
                    {
                        storeLocation = storeListName[position]
                        var arrayPosition = position - 1
                        (context as SettingsScreenActivity_APSDK).onStoreSelectedEvent(arrayPosition)
                    }
                }
                override fun onNothingSelected(parent: AdapterView<*>) {

                }
            }
        }
    }

    // creating spinner to select devices from dropdown list
    fun createDeviceSpinner() {
        deviceNameSpinner.visibility = View.VISIBLE

        var objModelManager = AeropayModelManager().getInstance()
        var modelOutPut = objModelManager.merchantDevicesModel

        if(modelOutPut.devices != null){

            var arraySize = modelOutPut.devices.size - 1
            var storeListName: MutableList<String> = ArrayList()
            var count = 0

            for(i in 0..arraySize){
                storeListName.add(count,modelOutPut.devices[i].name as String)
                count++
            }

            if (deviceNameSpinner != null) {
                val devicesAdapter = ArrayAdapter<String>(this,R.layout.spinner_layout_apsdk, storeListName!!)
                deviceNameSpinner?.adapter = devicesAdapter
                deviceNameSpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                        deviceName = storeListName[position]
                        PrefKeeper_APSDK.merchantDeviceIdPosition = position
                    }

                    override fun onNothingSelected(parent: AdapterView<*>) {

                    }
                }
            }
        }
    }

    fun onSaveButtonClick(view: View) {
        if(storeLocation.isNullOrEmpty() || deviceName.isNullOrEmpty()){
            showMsgToast("Please select your store location and Device name")
        }
        else{
            PrefKeeper_APSDK.storeName = storeLocation
            PrefKeeper_APSDK.deviceName = deviceName
            finish()
        }
    }

    // saving store name and device name on save button click
    fun onStoreSelectedEvent(position : Int){
        var objModelManager = AeropayModelManager().getInstance()

        var merchantLocation = MerchantLocationDevices()
        var merchantLocationValue = objModelManager.merchantLocationsModel.locations[position].merchantLocationId as Double

        merchantLocation.locationId =  merchantLocationValue.toBigDecimal()

        var awsConnectionManager = AWSConnectionManager_APSDK(this)
        awsConnectionManager.hitServer(DefineID_APSDK().FETCH_MERCHANT_DEVICES,this,merchantLocation)
    }
}
