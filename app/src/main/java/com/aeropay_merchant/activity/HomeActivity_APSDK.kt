package com.aeropay_merchant.activity

import AP.model.RegisterMerchantDevice
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aeropay_merchant.Model.AeropayModelManager
import com.aeropay_merchant.Utilities.GlobalMethods_APSDK
import com.aeropay_merchant.Utilities.PrefKeeper_APSDK
import com.aeropay_merchant.adapter.HomeListRecyclerView
import com.aeropay_merchant.communication.AWSConnectionManager_APSDK
import com.aeropay_merchant.communication.DefineID_APSDK


class HomeActivity_APSDK : BaseActivity() {

    lateinit var menuButton : ImageView
    lateinit var listViewRecycler : RecyclerView
    lateinit var cardViewRecycler : RecyclerView
    lateinit var readyToPay : TextView
    lateinit var aeropayTransparent : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.aeropay_merchant.R.layout.activity_home_apsdk)
        initialiseControls()

        GlobalMethods_APSDK().getDeviceToken(applicationContext)
        setupView()
        setListeners()
        maintainUserLoginCount()

        var loginCount = PrefKeeper_APSDK.logInCount
        if(loginCount< 4){
            var isPin = PrefKeeper_APSDK.isPinEnabled
            var isLogin = PrefKeeper_APSDK.isLoggedIn
            if(!isPin && !isLogin)
                GlobalMethods_APSDK().showDialog(this)
        }
    }

    //setting onClick Listeners on views
    private fun setListeners() {
        menuButton.setOnClickListener(View.OnClickListener {
            launchActivity(NavigationMenuActivity_APSDK::class.java)
        })
        aeropayTransparent.setOnClickListener(View.OnClickListener {
            createHitForUUID()
        })
    }

    fun createHitForUUID(){
        var objModelManager = AeropayModelManager().getInstance()

        var registerMerchant = RegisterMerchantDevice()
        var merchantDevicesValue = objModelManager.merchantDevicesModel

        var deviceIdValue = 1759.toDouble()

        registerMerchant.deviceId =  deviceIdValue.toBigDecimal()//merchantDevicesValue.devices[PrefKeeper_APSDK.merchantDeviceIdPosition!!].merchantLocationDeviceId as BigDecimal
        registerMerchant.token = PrefKeeper_APSDK.deviceToken

        var awsConnectionManager = AWSConnectionManager_APSDK(this)
        awsConnectionManager.hitServer(DefineID_APSDK().REGISTER_MERCHANT_LOCATION_DEVICE,this,registerMerchant)
    }

    //setting up hardcoded Recycler Adapter
    private fun setupView() {
        listViewRecycler.layoutManager = LinearLayoutManager(this)
        val payerName: ArrayList<String> = ArrayList()
        payerName.add("Daniel")
        payerName.add("Adam")
        listViewRecycler.adapter = HomeListRecyclerView(payerName,this)


       /* cardViewRecycler.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false)
        val userName: ArrayList<String> = ArrayList()
        userName.add("Pete")
        userName.add("Smith")
        userName.add("Tanner")
        cardViewRecycler.adapter = HomeCardRecyclerView(userName,this)*/
    }

    //inflating UI controls
    private fun initialiseControls() {
        menuButton = findViewById(com.aeropay_merchant.R.id.back_button)
        listViewRecycler = findViewById(com.aeropay_merchant.R.id.recyclerListView)
        cardViewRecycler = findViewById(com.aeropay_merchant.R.id.cardRecyclerView)
        readyToPay = findViewById(com.aeropay_merchant.R.id.readyToPayText)
        aeropayTransparent = findViewById(com.aeropay_merchant.R.id.aeropayTranparentLogo)

        var text = "<font color=#06dab3>0</font> <font color=#232323>ready to pay</font>";
        readyToPay.setText(Html.fromHtml(text));

        cardViewRecycler.visibility = View.GONE
    }

    // to check the login count of this user on this device
    private fun maintainUserLoginCount() {
        var initialLoginCount = PrefKeeper_APSDK.logInCount
        var finalCount = initialLoginCount + 1
        PrefKeeper_APSDK.logInCount = finalCount
    }

    fun creatBeaconTransmission(){

    }

  /*  override fun onBeaconServiceConnect() {
        this.beaconManager!!.setRangeNotifier(object : RangeNotifier {
            override fun didRangeBeaconsInRegion(beacons: Collection<Beacon>, region: Region) {
                if (beacons.size > 0) {
                    beaconList!!.clear()
                    val iterator = beacons.iterator()
                    while (iterator.hasNext()) {
                        beaconList!!.add(iterator.next().id1.toString())
                    }
                    runOnUiThread { adapter!!.notifyDataSetChanged() }
                }
            }
        })
        try {
            this.beaconManager!!.startRangingBeaconsInRegion(Region(null, 29, 582, 8CAF8E6D-F16B-4382-B2DB-771AE570F405))
        } catch (e: RemoteException) {
            e.printStackTrace()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        this.beaconManager!!.unbind(this)
    }*/
}
