package com.magnificsoftware.letswatch.ui.fragments.auth

import android.os.Parcel
import android.os.Parcelable

class AuthDetailsArgument private constructor() : Parcelable {
    var authenticationToken: String? = null
    var openSignupPage: Boolean = false

    private constructor(parcel: Parcel) : this() {
        authenticationToken = parcel.readString()
        openSignupPage = parcel.readInt() == 1
    }

    constructor(token: String?, openSignupPage: Boolean = false) : this() {
        this.authenticationToken = token
        this.openSignupPage = openSignupPage
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(authenticationToken ?: "")
        parcel.writeInt(if (openSignupPage) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AuthDetailsArgument> {
        override fun createFromParcel(parcel: Parcel): AuthDetailsArgument {
            return AuthDetailsArgument(parcel)
        }

        override fun newArray(size: Int): Array<AuthDetailsArgument?> {
            return arrayOfNulls(size)
        }
    }
}