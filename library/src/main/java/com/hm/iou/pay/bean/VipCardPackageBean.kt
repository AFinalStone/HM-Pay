@file:JvmName("VipCardPackageBean")

package com.hm.iou.pay.bean

import android.os.Parcel
import android.os.Parcelable

class VipCardPackageBean constructor() : Parcelable {

    var actualPrice: Int? = 0
    var content: String? = null
    var outOfDay: Int? = 0
    var packageCode: String? = null
    var rechargeSign: Int? = 0
    var yuanPer: String? = null

    constructor(source: Parcel) : this() {
        actualPrice = source.readInt()
        content = source.readString()
        outOfDay = source.readInt()
        packageCode = source.readString()
        rechargeSign = source.readInt()
        yuanPer = source.readString()
    }

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(actualPrice ?: 0)
        writeString(content)
        writeInt(outOfDay ?: 0)
        writeString(packageCode)
        writeInt(rechargeSign ?: 0)
        writeString(yuanPer)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<VipCardPackageBean> = object : Parcelable.Creator<VipCardPackageBean> {
            override fun createFromParcel(source: Parcel): VipCardPackageBean = VipCardPackageBean(source)
            override fun newArray(size: Int): Array<VipCardPackageBean?> = arrayOfNulls(size)
        }
    }
}