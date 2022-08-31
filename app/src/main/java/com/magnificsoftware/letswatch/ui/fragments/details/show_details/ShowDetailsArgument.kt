package com.magnificsoftware.letswatch.ui.fragments.details.show_details

import android.os.Parcel
import android.os.Parcelable
import kotlin.properties.Delegates

class ShowDetailsArgument private constructor() : Parcelable {
    var showId by Delegates.notNull<Int>()
    lateinit var showType: ShowType

    private constructor(parcel: Parcel) : this() {
        showId = parcel.readInt()
        showType = parcel.readValue(ShowType::class.java.classLoader)!! as ShowType
    }

    constructor(showId: Int?, showType: ShowType) : this() {
        this.showId = showId ?: 1
        this.showType = showType
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(showId)
        parcel.writeValue(showType)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ShowDetailsArgument> {
        override fun createFromParcel(parcel: Parcel): ShowDetailsArgument {
            return ShowDetailsArgument(parcel)
        }

        override fun newArray(size: Int): Array<ShowDetailsArgument?> {
            return arrayOfNulls(size)
        }
    }
}