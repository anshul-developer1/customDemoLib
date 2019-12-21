package com.aeropay_merchant.activity

import AP.model.MerchantLocationDevices
import AP.model.RegisterMerchantDevice
import android.Manifest
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aeropay_merchant.Model.AeropayModelManager
import com.aeropay_merchant.Utilities.GlobalMethods
import com.aeropay_merchant.Utilities.PrefKeeper
import com.aeropay_merchant.adapter.HomeListRecyclerView
import com.aeropay_merchant.communication.AWSConnectionManager
import com.aeropay_merchant.communication.DefineID
import android.os.RemoteException
import android.widget.ArrayAdapter
import org.altbeacon.beacon.*
import android.Manifest.permission
import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Build
import org.altbeacon.beacon.BeaconParser
import org.altbeacon.beacon.BeaconManager
import android.R
import android.app.AlertDialog
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.widget.ListView
import com.aeropay_merchant.adapter.HomeCardRecyclerView


class HomeActivity : BaseActivity() {

    lateinit var menuButton : ImageView
    lateinit var listViewRecycler : RecyclerView
    lateinit var cardViewRecycler : RecyclerView
    lateinit var readyToPay : TextView
    lateinit var aeropayTransparent : ImageView

  /*  private var TAG = "BEACON_PROJECT"
    private var beaconList: ArrayList<String>? = null
    private var beaconListView: RecyclerView? = null
    private var adapter: ArrayAdapter<String>? = null
    private var beaconManager: BeaconManager? = null*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.aeropay_merchant.R.layout.activity_home)
        initialiseControls()
        GlobalMethods().getDeviceToken(applicationContext)
        setupView()
        setListeners()
/*
        this.beaconList = ArrayList()
        this.beaconManager = BeaconManager.getInstanceForApplication(this)
        this.beaconManager!!.beaconParsers.add(BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"))
        this.beaconManager!!.bind(this)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("This app needs location access")
                builder.setMessage("Please grant location access so this app can detect beacons")
                builder.setPositiveButton(android.R.string.ok, null)
                builder.setOnDismissListener(DialogInterface.OnDismissListener {
                    requestPermissions(
                        arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                        1
                    )
                })
                builder.show()
            }
        }*/

        maintainUserLoginCount()

        var loginCount = PrefKeeper.logInCount
        if(loginCount< 4){
            var isPin = PrefKeeper.isPinEnabled
            var isLogin = PrefKeeper.isLoggedIn
            if(!isPin && !isLogin)
                GlobalMethods().showDialog(this)
        }
    }

    //setting onClick Listeners on views
    private fun setListeners() {
        menuButton.setOnClickListener(View.OnClickListener {
            launchActivity(NavigationMenuActivity::class.java)
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

        registerMerchant.deviceId =  deviceIdValue.toBigDecimal()//merchantDevicesValue.devices[PrefKeeper.merchantDeviceIdPosition!!].merchantLocationDeviceId as BigDecimal
        registerMerchant.token = PrefKeeper.deviceToken

        var awsConnectionManager = AWSConnectionManager(this)
        awsConnectionManager.hitServer(DefineID().REGISTER_MERCHANT_LOCATION_DEVICE,this,registerMerchant)
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
        var initialLoginCount = PrefKeeper.logInCount
        var finalCount = initialLoginCount + 1
        PrefKeeper.logInCount = finalCount
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
