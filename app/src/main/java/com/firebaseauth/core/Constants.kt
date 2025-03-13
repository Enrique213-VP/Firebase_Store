package com.firebaseauth.core

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.webkit.MimeTypeMap

object Constants {

    const val USERS: String = "users"
    const val My_Shopp_Preferences = "MyShopPalPrefs"
    const val LoggedInUsername = "LoggedInUsername"
    const val LoggedInUsernameT = "email"
    const val ExtraUserDetails ="ExtraUserDetails"
    const val ReadStoragePermissionCode = 2
    const val PickImageRequestCode = 1

    const val Male = "male"
    const val Female = "female"
    const val FirstName = "firstname"
    const val LastName = "lastname"

    const val Mobile = "mobile"
    const val Gender = "gender"
    const val Image = "image"
    const val CompleteProfile = "profileCompleted"

    const val UserProfileImage = "UserProfileImage"

    fun showImageChoose(activity: Activity) {


        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )

        //Launches the image selection of phone storage using the constant code.
        activity.startActivityForResult(galleryIntent, PickImageRequestCode)
    }

    fun getFileExtension(activity: Activity, uri: Uri?): String? {
        /*
         * MimeTypeMap: Two-way map that maps MIME-types  to file extensions and vice versa.
         *
         * getSingleton(): Get the singleton instance of MimeTypeMap
         *
         * getExtensionFromMimeType: Return the registered extension for the given MIME type
         *
         * contentResolver.getType: Return the MIME type of the given content URL
         */
        return MimeTypeMap.getSingleton()
            .getExtensionFromMimeType(activity.contentResolver.getType(uri!!))
    }
}