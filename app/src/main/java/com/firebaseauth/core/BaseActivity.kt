package com.firebaseauth.core

import android.app.Dialog
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.firebaseauth.R
import java.util.logging.Handler

open class BaseActivity: AppCompatActivity() {

    private lateinit var mProgressDialog: Dialog

    private var doubleBackToExitPressedOnce = false

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

    fun doubleBackToExit() {

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce = true

        Toast.makeText(
            this@BaseActivity,
            resources.getString(R.string.please_click_back_again_to_exit),
            Toast.LENGTH_LONG
        ).show()

        @Suppress("Deprecated")
        (android.os.Handler()).postDelayed({doubleBackToExitPressedOnce = false}, 2000)
    }
}