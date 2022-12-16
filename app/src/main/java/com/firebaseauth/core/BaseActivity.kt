package com.firebaseauth.core

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import com.firebaseauth.R

open class BaseActivity: AppCompatActivity() {

    private lateinit var mProgressDialog: Dialog

    fun showProgressDialog(text: String) {
        mProgressDialog = Dialog(this)
        /* Set the screen content from a layout resource.
        The resource will be inflated, adding all top-level views to the screen */
        mProgressDialog.setContentView(R.layout.dialog_progress)

        mProgressDialog.setCancelable(false)
        mProgressDialog.setCanceledOnTouchOutside(false)

        //Start the dialog and display it on screen
        mProgressDialog.show()
    }

    fun hideProgressDialog() {
        mProgressDialog.dismiss()
    }



}