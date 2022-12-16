package com.firebaseauth.core

import android.app.Activity
import android.content.Intent
import android.provider.MediaStore

object Constants {

    const val USERS: String = "users"
    const val My_Shopp_Preferences = "MyShopPalPrefs"
    const val LoggedInUsername = "LoggedInUsername"
    const val LoggedInUsernameT = "Email"
    const val ExtraUserDetails ="ExtraUserDetails"
    const val ReadStoragePermissionCode = 2
    const val PickImageRequestCode = 1

    const val Male = "Male"
    const val Female = "Female"

    const val Mobile = "Mobile"
    const val Gender = "Gender"

    fun showImageChoose(activity: Activity) {

        //An intent for launching the image selection of phone storage
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )

        //Launches the image selection of phone storage using the constant code.
        activity.startActivityForResult(galleryIntent, PickImageRequestCode)
    }
}