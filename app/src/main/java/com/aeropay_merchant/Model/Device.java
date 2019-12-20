
package com.aeropay_merchant.Model;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class Device {

    @SerializedName("majorID")
    private Object mMajorID;
    @SerializedName("merchantLocationDeviceId")
    private Object mMerchantLocationDeviceId;
    @SerializedName("merchantLocationId")
    private Object mMerchantLocationId;
    @SerializedName("merchantName")
    private Object mMerchantName;
    @SerializedName("minorID")
    private Object mMinorID;
    @SerializedName("name")
    private Object mName;
    @SerializedName("platform")
    private Object mPlatform;
    @SerializedName("storeName")
    private Object mStoreName;

    public Object getMajorID() {
        return mMajorID;
    }

    public void setMajorID(String majorID) {
        mMajorID = majorID;
    }

    public Object getMerchantLocationDeviceId() {
        return mMerchantLocationDeviceId;
    }

    public void setMerchantLocationDeviceId(Object merchantLocationDeviceId) {
        mMerchantLocationDeviceId = merchantLocationDeviceId;
    }

    public Object getMerchantLocationId() {
        return mMerchantLocationId;
    }

    public void setMerchantLocationId(Object merchantLocationId) {
        mMerchantLocationId = merchantLocationId;
    }

    public Object getMerchantName() {
        return mMerchantName;
    }

    public void setMerchantName(Object merchantName) {
        mMerchantName = merchantName;
    }

    public Object getMinorID() {
        return mMinorID;
    }

    public void setMinorID(Object minorID) {
        mMinorID = minorID;
    }

    public Object getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public Object getPlatform() {
        return mPlatform;
    }

    public void setPlatform(Object platform) {
        mPlatform = platform;
    }

    public Object getStoreName() {
        return mStoreName;
    }

    public void setStoreName(Object storeName) {
        mStoreName = storeName;
    }

}
